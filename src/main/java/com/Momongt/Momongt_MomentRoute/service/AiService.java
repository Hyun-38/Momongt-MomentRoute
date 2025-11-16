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
            주어진 도시별 장소 목록을 기반으로:
            - 선호 음식 카테고리를 반영하여 음식점 2개 추천
            - 관광지 / 전시 / 축제 중 1개 추천
            - 전체 여행 경로 Summary 작성

            반드시 아래 JSON 출력 형식만 응답하라:
            {
              "cityRecommendations": [
                {
                  "cityName": "",
                  "foods": [],
                  "attractions": []
                }
              ],
              "summary": ""
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
                    )
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
                    return JsonUtils.fromJson(content, RouteAiResultDto.class);
                }
            }

            throw new RuntimeException("GPT 응답이 비어있습니다");
        } catch (Exception e) {
            throw new RuntimeException("GPT 호출 실패: " + e.getMessage(), e);
        }
    }
}
