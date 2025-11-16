package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.*;
import com.Momongt.Momongt_MomentRoute.entity.City;
import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.repository.CityRepository;
import com.Momongt.Momongt_MomentRoute.repository.PlaceRepository;
import com.Momongt.Momongt_MomentRoute.util.JsonUtils;
import com.Momongt.Momongt_MomentRoute.util.RouteOptimizer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    public RouteResponseDto recommendRoute(RouteRequestDto request) {

        // 1) 요청 도시 조회
        City destination = cityRepository.findByName(request.destinationCity())
                .orElseThrow(() -> new RuntimeException("City not found: " + request.destinationCity()));

        List<City> viaCities = new ArrayList<>();
        if (request.viaCities() != null && !request.viaCities().isEmpty()) {
            for (String cityName : request.viaCities()) {
                City city = cityRepository.findByName(cityName)
                        .orElseThrow(() -> new RuntimeException("City not found: " + cityName));
                viaCities.add(city);
            }
        }

        // 2) 경로 최적화 - 중요!
        RouteOptimizer.OptimizationResult optimizationResult = RouteOptimizer.optimizeWithInfo(viaCities, destination);
        List<City> optimizedRoute = optimizationResult.getRoute();

        System.out.println("=== 경로 최적화 결과 ===");
        System.out.println("알고리즘: " + optimizationResult.getAlgorithm());
        System.out.println("총 이동 거리: " + optimizationResult.getTotalDistanceKm() + " km");
        for (int i = 0; i < optimizedRoute.size(); i++) {
            City city = optimizedRoute.get(i);
            System.out.println((i + 1) + ". " + city.getName() +
                " (위도: " + city.getLatitude() + ", 경도: " + city.getLongitude() + ")");
        }
        System.out.println("=====================");

        // 3) DB 조회 → RouteAiPayload 생성 (최적화된 순서대로)
        List<RouteAiPayload.RouteCityPayload> cityPayloads = new ArrayList<>();

        for (City city : optimizedRoute) {
            List<Place> places = placeRepository.findByCity_Id(city.getId());

            List<RouteAiPayload.RoutePlacePayload> placePayloads =
                    places.stream()
                            .map(p -> new RouteAiPayload.RoutePlacePayload(
                                    p.getId(),
                                    p.getType().name(),
                                    p.getCategory(),
                                    p.getName(),
                                    p.getDescription(),
                                    p.getLatitude(),
                                    p.getLongitude()
                            )).toList();

            cityPayloads.add(
                    new RouteAiPayload.RouteCityPayload(city.getId(), city.getName(), placePayloads)
            );
        }

        RouteAiPayload payload = new RouteAiPayload(
                new RouteAiPayload.UserPreference(request.preferredCategories()),
                cityPayloads
        );

        // DB에서 조회한 실제 place 데이터 로깅
        System.out.println("=== GPT에 전달되는 Place 데이터 ===");
        for (RouteAiPayload.RouteCityPayload cityPayload : cityPayloads) {
            System.out.println("도시: " + cityPayload.cityName());
            System.out.println("  총 " + cityPayload.places().size() + "개의 장소");
            for (RouteAiPayload.RoutePlacePayload place : cityPayload.places()) {
                System.out.println("  - [" + place.placeId() + "] " + place.name() +
                    " (" + place.type() + ", " + place.category() +
                    ", desc: " + (place.description() != null ? place.description() : "null") + ")");
            }
        }
        System.out.println("=====================================");

        // 4) GPT 호출
        RouteAiResultDto gptResult = callGPT(payload);

        // 5) 프론트용 응답으로 변환
        List<RouteResponseDto.CityRecommendation> cityRecommendations = gptResult.cities().stream()
                .map(aiCity -> new RouteResponseDto.CityRecommendation(
                        aiCity.cityName(),
                        aiCity.foods().stream()
                                .map(p -> new RouteResponseDto.RecommendedPlace(
                                        p.placeId(),
                                        p.name(),
                                        p.type(),
                                        p.category(),
                                        p.description(),
                                        p.latitude(),
                                        p.longitude()
                                )).toList(),
                        aiCity.attractions().stream()
                                .map(p -> new RouteResponseDto.RecommendedPlace(
                                        p.placeId(),
                                        p.name(),
                                        p.type(),
                                        p.category(),
                                        p.description(),
                                        p.latitude(),
                                        p.longitude()
                                )).toList()
                )).toList();

        return new RouteResponseDto(
                cityRecommendations,
                gptResult.summary(),
                new RouteResponseDto.RouteInfo(
                        optimizedRoute.stream().map(City::getName).toList(),
                        optimizationResult.getAlgorithm(),
                        optimizationResult.getTotalDistanceKm()
                )
        );
    }

    private RouteAiResultDto callGPT(RouteAiPayload payload) {

        String systemPrompt = """
            You are an AI travel planner for Korea.
            
            CRITICAL RULES:
            1. You MUST recommend places for EVERY city in the input data (routeCities array)
            2. You MUST ONLY use places from the provided JSON input data
            3. You MUST use the EXACT placeId, name, type, category, description (including null), latitude, longitude from the input
            4. DO NOT invent or create new places
            5. DO NOT modify place names or details
            6. DO NOT skip any cities - provide recommendations for ALL cities in the input
            7. PRESERVE null values - if description is null in input, keep it null in output
            
            For EACH city in the input routeCities array:
            
            FOODS (2 places):
            - Select 2 RESTAURANT places that match user's preferred food categories (한식, 일식, 중식, 양식)
            - Use the EXACT description from input (even if null)
            
            ATTRACTIONS (1-3 places):
            - Prioritize VARIETY across different types: ATTRACTION, FESTIVAL, EXHIBITION
            - If multiple types available, select from different types (e.g., 1 ATTRACTION, 1 FESTIVAL, 1 EXHIBITION)
            - If only one type available, select 1-2 from that type
            - Aim for diverse experiences (문화유적, 공원, 박물관, 축제 등)
            
            Create a comprehensive summary covering the entire multi-city travel route.
            
            Respond ONLY with this JSON format:
            {
              "cities": [
                {
                  "cityName": "first city name",
                  "foods": [ 
                    { 
                      "placeId": 123,
                      "name": "exact name",
                      "type": "RESTAURANT",
                      "category": "exact category",
                      "description": "exact description or null",
                      "latitude": 37.123,
                      "longitude": 127.123
                    }
                  ],
                  "attractions": [ 
                    { 
                      "placeId": 456,
                      "name": "exact name",
                      "type": "ATTRACTION or FESTIVAL or EXHIBITION",
                      "category": "exact category or null",
                      "description": "exact description or null",
                      "latitude": 37.456,
                      "longitude": 127.456
                    }
                  ]
                },
                ... (continue for ALL cities)
              ],
              "summary": "Complete travel route summary for all cities"
            }
            
            CRITICAL: 
            - Number of city objects MUST equal number of input cities
            - Use EXACT values from input including null
            - Vary attraction types when possible (ATTRACTION, FESTIVAL, EXHIBITION)
            """;

        try {
            String userPrompt = "입력 데이터(JSON):\n" + JsonUtils.toJson(payload);

            // OpenAI API 직접 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    ),
                    "temperature", 0.7,
                    "response_format", Map.of("type", "json_object")
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            if (response != null && response.containsKey("choices")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");

                    // 디버깅을 위해 GPT 응답 로깅
                    System.out.println("GPT Response: " + content);


                    return JsonUtils.fromJson(content, RouteAiResultDto.class);
                }
            }

            throw new RuntimeException("GPT 응답이 비어있습니다");
        } catch (Exception e) {
            throw new RuntimeException("GPT 호출 실패: " + e.getMessage(), e);
        }
    }
}
