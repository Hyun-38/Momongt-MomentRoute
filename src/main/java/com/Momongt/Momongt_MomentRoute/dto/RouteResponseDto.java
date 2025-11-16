package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteResponseDto(
        List<CityRecommendation> cities,
        String summary
) {

    public record CityRecommendation(
            String cityName,
            List<RecommendedPlace> foods,
            List<RecommendedPlace> attractions
    ) {}

    public record RecommendedPlace(
            Long placeId,
            String name,
            String type,
            String category,
            String description,
            Double latitude,
            Double longitude
    ) {}
}
