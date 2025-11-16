package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.*;
import com.Momongt.Momongt_MomentRoute.entity.City;
import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.repository.CityRepository;
import com.Momongt.Momongt_MomentRoute.repository.PlaceRepository;
import com.Momongt.Momongt_MomentRoute.util.JsonUtils;

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

        // 1) 요청 도시 순서 정리
        List<String> ordered = new ArrayList<>();
        if (request.viaCities() != null) ordered.addAll(request.viaCities());
        ordered.add(request.destinationCity());

        // 2) DB 조회 → RouteAiPayload 생성
        List<RouteAiPayload.RouteCityPayload> cityPayloads = new ArrayList<>();

        for (String cityName : ordered) {
            City city = cityRepository.findByName(cityName)
                    .orElseThrow(() -> new RuntimeException("City not found: " + cityName));

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

        // 3) GPT 호출
        RouteAiResultDto gptResult = callGPT(payload);

        // 4) 프론트용 응답으로 변환
        List<RouteResponseDto.CityRecommendation> cityRecommendations = gptResult.cityRecommendations().stream()
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
                gptResult.summary()
        );
    }

    private RouteAiResultDto callGPT(RouteAiPayload payload) {

        String systemPrompt = """
            너는 한국 여행 루트를 구성하는 AI 플래너이다.
            주어진 도시별 장소 목록(JSON)을 기반으로:
            1. 사용자가 선호하는 음식 카테고리를 반영하여 음식점(RESTAURANT) 2개 추천
            2. 관광지(ATTRACTION) / 전시(EXHIBITION) / 축제(FESTIVAL) 중 1개 추천
            3. 전체 여행 경로에 대한 Summary 작성
            
            중요: 반드시 입력된 JSON의 placeId를 사용하여 추천해야 한다.
            
            반드시 아래 JSON 형식으로만 응답하라. 다른 텍스트는 포함하지 말 것:
            {
              "cityRecommendations": [
                {
                  "cityName": "도시명",
                  "foods": [
                    {
                      "placeId": 123,
                      "name": "장소명",
                      "type": "RESTAURANT",
                      "category": "카테고리",
                      "description": "설명",
                      "latitude": 37.123,
                      "longitude": 127.123
                    }
                  ],
                  "attractions": [
                    {
                      "placeId": 456,
                      "name": "장소명",
                      "type": "ATTRACTION",
                      "category": "카테고리",
                      "description": "설명",
                      "latitude": 37.123,
                      "longitude": 127.123
                    }
                  ]
                }
              ],
              "summary": "전체 여행 경로 요약"
            }
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
