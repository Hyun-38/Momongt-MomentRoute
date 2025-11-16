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
            Long placeId,
            String type,
            String category,
            String name,
            String description,
            Double latitude,
            Double longitude
    ) {}
}
