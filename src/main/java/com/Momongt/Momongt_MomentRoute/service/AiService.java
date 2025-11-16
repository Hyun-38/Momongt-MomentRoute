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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    /**
     * 간단한 경로 순서만 반환 (도시명 리스트)
     */
    public SimpleRouteDto getSimpleRoute(RouteOptimizeRequestDto request) {
        // 1) 요청 도시 조회
        City destination = cityRepository.findByName(request.destinationCity())
                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + request.destinationCity()));

        List<City> viaCities = new ArrayList<>();
        if (request.viaCities() != null && !request.viaCities().isEmpty()) {
            for (String cityNameRaw : request.viaCities()) {
                // 콤마로 구분된 여러 도시명 처리
                String[] cityNames = cityNameRaw.split(",");
                for (String cityName : cityNames) {
                    String trimmedName = cityName.trim();
                    if (!trimmedName.isEmpty()) {
                        City city = cityRepository.findByName(trimmedName)
                                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + trimmedName));
                        viaCities.add(city);
                    }
                }
            }
        }

        // 2) 경로 최적화
        RouteOptimizer.OptimizationResult optimizationResult = RouteOptimizer.optimizeWithInfo(viaCities, destination);
        List<City> optimizedRoute = optimizationResult.getRoute();

        // 3) 도시명 리스트로 변환
        List<String> routeNames = optimizedRoute.stream()
                .map(City::getName)
                .toList();

        return new SimpleRouteDto(
                routeNames,
                optimizationResult.getAlgorithm(),
                optimizationResult.getTotalDistanceKm()
        );
    }

    /**
     * 경로 최적화만 수행 (상세 정보 포함)
     */
    public OptimizedRouteDto optimizeRouteOnly(RouteOptimizeRequestDto request) {
        // 1) 요청 도시 조회
        City destination = cityRepository.findByName(request.destinationCity())
                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + request.destinationCity()));

        List<City> viaCities = new ArrayList<>();
        if (request.viaCities() != null && !request.viaCities().isEmpty()) {
            for (String cityNameRaw : request.viaCities()) {
                // 콤마로 구분된 여러 도시명 처리
                String[] cityNames = cityNameRaw.split(",");
                for (String cityName : cityNames) {
                    String trimmedName = cityName.trim();
                    if (!trimmedName.isEmpty()) {
                        City city = cityRepository.findByName(trimmedName)
                                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + trimmedName));
                        viaCities.add(city);
                    }
                }
            }
        }

        // 2) 경로 최적화
        RouteOptimizer.OptimizationResult optimizationResult = RouteOptimizer.optimizeWithInfo(viaCities, destination);
        List<City> optimizedRoute = optimizationResult.getRoute();

        // 3) 결과 변환
        List<OptimizedRouteDto.CityInfo> routeInfo = new ArrayList<>();
        for (int i = 0; i < optimizedRoute.size(); i++) {
            City city = optimizedRoute.get(i);
            routeInfo.add(new OptimizedRouteDto.CityInfo(
                    city.getId(),
                    city.getName(),
                    city.getLatitude(),
                    city.getLongitude(),
                    i + 1  // 1부터 시작하는 순서
            ));
        }

        return new OptimizedRouteDto(
                routeInfo,
                optimizationResult.getAlgorithm(),
                optimizationResult.getTotalDistanceKm()
        );
    }

    /**
     * GPT 추천 포함 전체 경로 추천
     */
    public RouteResponseDto recommendRoute(RouteRequestDto request) {

        // 1) 요청 도시 조회
        City destination = cityRepository.findByName(request.destinationCity())
                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + request.destinationCity()));

        List<City> viaCities = new ArrayList<>();
        if (request.viaCities() != null && !request.viaCities().isEmpty()) {
            for (String cityNameRaw : request.viaCities()) {
                // 콤마로 구분된 여러 도시명 처리 (예: "동두천, 의정부" → ["동두천", "의정부"])
                String[] cityNames = cityNameRaw.split(",");
                for (String cityName : cityNames) {
                    String trimmedName = cityName.trim();
                    if (!trimmedName.isEmpty()) {
                        City city = cityRepository.findByName(trimmedName)
                                .orElseThrow(() -> new IllegalArgumentException("도시를 찾을 수 없습니다: " + trimmedName + " (사용 가능한 도시 목록을 확인해주세요)"));
                        viaCities.add(city);
                    }
                }
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
                                    p.getDescription()
                                    // address, imageUrl, latitude, longitude 제외!
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

        // 5) GPT가 추천한 placeId로 DB에서 실제 데이터 조회
        Set<Long> allPlaceIds = gptResult.cities().stream()
                .flatMap(city -> {
                    List<Long> ids = new ArrayList<>();
                    city.foods().forEach(p -> ids.add(p.placeId()));
                    city.attractions().forEach(p -> ids.add(p.placeId()));
                    if (city.festivals() != null) city.festivals().forEach(p -> ids.add(p.placeId()));
                    if (city.exhibitions() != null) city.exhibitions().forEach(p -> ids.add(p.placeId()));
                    return ids.stream();
                })
                .collect(Collectors.toSet());

        Map<Long, Place> placeMap = placeRepository.findAllById(allPlaceIds).stream()
                .collect(Collectors.toMap(Place::getId, p -> p));

        // 6) GPT 설명 + DB 데이터 조합
        List<RouteResponseDto.CityRecommendation> cityRecommendations = gptResult.cities().stream()
                .map(aiCity -> new RouteResponseDto.CityRecommendation(
                        aiCity.cityName(),
                        // 음식점
                        aiCity.foods().stream()
                                .map(gptPlace -> {
                                    Place dbPlace = placeMap.get(gptPlace.placeId());
                                    return new RouteResponseDto.RecommendedPlace(
                                            dbPlace.getId(),
                                            dbPlace.getName(),
                                            dbPlace.getType().name(),
                                            dbPlace.getCategory(),
                                            gptPlace.description(),  // GPT가 생성한 풍부한 설명
                                            dbPlace.getLatitude(),
                                            dbPlace.getLongitude(),
                                            dbPlace.getAddress(),
                                            dbPlace.getImageUrl()
                                    );
                                }).toList(),
                        // 관광지
                        aiCity.attractions().stream()
                                .map(gptPlace -> {
                                    Place dbPlace = placeMap.get(gptPlace.placeId());
                                    return new RouteResponseDto.RecommendedPlace(
                                            dbPlace.getId(),
                                            dbPlace.getName(),
                                            dbPlace.getType().name(),
                                            dbPlace.getCategory(),
                                            gptPlace.description(),
                                            dbPlace.getLatitude(),
                                            dbPlace.getLongitude(),
                                            dbPlace.getAddress(),
                                            dbPlace.getImageUrl()
                                    );
                                }).toList(),
                        // 축제
                        aiCity.festivals() != null ? aiCity.festivals().stream()
                                .map(gptPlace -> {
                                    Place dbPlace = placeMap.get(gptPlace.placeId());
                                    return new RouteResponseDto.RecommendedPlace(
                                            dbPlace.getId(),
                                            dbPlace.getName(),
                                            dbPlace.getType().name(),
                                            dbPlace.getCategory(),
                                            gptPlace.description(),
                                            dbPlace.getLatitude(),
                                            dbPlace.getLongitude(),
                                            dbPlace.getAddress(),
                                            dbPlace.getImageUrl()
                                    );
                                }).toList() : List.of(),
                        // 전시
                        aiCity.exhibitions() != null ? aiCity.exhibitions().stream()
                                .map(gptPlace -> {
                                    Place dbPlace = placeMap.get(gptPlace.placeId());
                                    return new RouteResponseDto.RecommendedPlace(
                                            dbPlace.getId(),
                                            dbPlace.getName(),
                                            dbPlace.getType().name(),
                                            dbPlace.getCategory(),
                                            gptPlace.description(),
                                            dbPlace.getLatitude(),
                                            dbPlace.getLongitude(),
                                            dbPlace.getAddress(),
                                            dbPlace.getImageUrl()
                                    );
                                }).toList() : List.of()
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
            1. You MUST recommend places for EVERY city in the input routeCities array
            2. You MUST ONLY use placeId from the provided input data
            3. DO NOT invent new places - only select from the given list
            4. Create RICH, ENGAGING descriptions (2-3 sentences in Korean) for each place
            5. DO NOT skip any cities
            
            Input data contains: placeId, type, category, name, description (basic info)
            You will use this to SELECT places and create rich descriptions.
            
            For EACH city in the optimized route order:
            
            FOODS (2-3 places):
            - Select 2-3 RESTAURANT places matching user's food categories (한식, 일식, 중식, 양식)
            - If user wants "한식", select only 한식 restaurants
            - Create engaging descriptions explaining why this place is recommended
            
            ATTRACTIONS (2 places each type):
            - Select 2 ATTRACTION places (문화유적, 공원, 관광지 등)
            - Select 2 FESTIVAL places if available (don't invent if none exist)
            - Select 2 EXHIBITION places if available (don't invent if none exist)
            - Prioritize variety in experiences
            
            IMPORTANT:
            - Only return placeId and description
            - Description should be rich and engaging (2-3 sentences in Korean)
            - If a type (FESTIVAL/EXHIBITION) is not available, return empty array
            - Do NOT create fake places
            
            Response JSON format:
            {
              "cities": [
                {
                  "cityName": "city name",
                  "foods": [
                    {
                      "placeId": 123,
                      "description": "풍부한 2-3문장 설명"
                    }
                  ],
                  "attractions": [
                    {
                      "placeId": 456,
                      "description": "풍부한 2-3문장 설명"
                    }
                  ],
                  "festivals": [
                    {
                      "placeId": 789,
                      "description": "풍부한 2-3문장 설명"
                    }
                  ],
                  "exhibitions": [
                    {
                      "placeId": 999,
                      "description": "풍부한 2-3문장 설명"
                    }
                  ]
                }
              ],
              "summary": "전체 여행 경로에 대한 스토리텔링 형식의 요약 (한국어, 5-7문장)"
            }
            
            CRITICAL:
            - Only use placeId from input
            - Create rich Korean descriptions
            - Don't invent places - if no FESTIVAL exists, return []
            - Match city count in response to input count
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
