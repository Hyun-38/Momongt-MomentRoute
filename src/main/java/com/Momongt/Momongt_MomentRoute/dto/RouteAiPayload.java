package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteAiPayload(
        UserPreference userPreference,
        List<RouteCityPayload> routeCities
) {
    public record UserPreference(List<String> foodCategories) {}

    public record RouteCityPayload(
            Long cityId,
            String cityName,
            List<RoutePlacePayload> places
    ) {}

    public record RoutePlacePayload(
            Long placeId,        // 선택을 위한 ID
            String type,         // RESTAURANT/ATTRACTION/FESTIVAL/EXHIBITION
            String category,     // 한식, 일식, 중식, 양식 등
            String name,         // 장소 이름 (GPT가 선택 판단용)
            String description   // 간단한 설명 (GPT가 풍부하게 확장할 베이스)
    ) {}
}
