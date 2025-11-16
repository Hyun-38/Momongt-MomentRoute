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
                    " (" + place.type() + ", " + place.category() + ")");
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
            1. You MUST ONLY recommend places from the provided JSON input data
            2. You MUST use the EXACT placeId, name, type, category, latitude, longitude from the input
            3. DO NOT invent or create new places
            4. DO NOT modify place names or details
            5. Only SELECT from the given places based on user preferences
            
            Based on the provided place data:
            - Recommend 2 RESTAURANT places matching user's food categories
            - Recommend 1 place from ATTRACTION/EXHIBITION/FESTIVAL types
            - Create a summary of the travel route
            
            Respond ONLY with this JSON format:
            {
              "cities": [
                {
                  "cityName": "city name from input",
                  "foods": [
                    {
                      "placeId": <exact ID from input>,
                      "name": "<exact name from input>",
                      "type": "<exact type from input>",
                      "category": "<exact category from input>",
                      "description": "<exact description from input>",
                      "latitude": <exact latitude from input>,
                      "longitude": <exact longitude from input>
                    }
                  ],
                  "attractions": [
                    {
                      "placeId": <exact ID from input>,
                      "name": "<exact name from input>",
                      "type": "<exact type from input>",
                      "category": "<exact category from input>",
                      "description": "<exact description from input>",
                      "latitude": <exact latitude from input>,
                      "longitude": <exact longitude from input>
                    }
                  ]
                }
              ],
              "summary": "Korean travel route summary"
            }
            
            REMEMBER: Use ONLY the places provided in the input JSON. Do NOT create new places.
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
