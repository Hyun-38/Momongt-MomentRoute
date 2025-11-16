package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteAiResultDto(
        List<CityRecommendation> cityRecommendations,
        String summary
) {

    public record CityRecommendation(
            String cityName,
            List<RecommendedPlace> foods,            // 음식점 2개
            List<RecommendedPlace> attractions       // 관광/축제/전시 1개
    ) {}

    public record RecommendedPlace(
            Long placeId,
            String name,
            String type,        // RESTAURANT / ATTRACTION / FESTIVAL / EXHIBITION
            String category,    // 한식, 카페 등
            String description,
            Double latitude,
            Double longitude
    ) {}
}
