package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteAiResultDto(
        List<CityRecommendation> cities,
        String summary
) {

    public record CityRecommendation(
            String cityName,
            List<RecommendedPlace> foods,         // 음식점 2-3개
            List<RecommendedPlace> attractions,   // 관광지 2개
            List<RecommendedPlace> festivals,     // 축제 2개 (없으면 빈 배열)
            List<RecommendedPlace> exhibitions    // 전시 2개 (없으면 빈 배열)
    ) {}

    public record RecommendedPlace(
            Long placeId,
            String name,
            String type,        // RESTAURANT / ATTRACTION / FESTIVAL / EXHIBITION
            String category,    // 한식, 카페 등
            String description,
            Double latitude,
            Double longitude,
            String address,
            String imageUrl
    ) {}
}
