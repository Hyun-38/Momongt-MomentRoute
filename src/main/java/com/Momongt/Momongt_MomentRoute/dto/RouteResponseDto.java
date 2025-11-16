package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteResponseDto(
        List<CityRecommendation> cities,
        String summary,
        RouteInfo routeInfo  // 경로 최적화 정보 추가
) {

    public record CityRecommendation(
            String cityName,
            List<RecommendedPlace> foods,
            List<RecommendedPlace> attractions,
            List<RecommendedPlace> festivals,
            List<RecommendedPlace> exhibitions
    ) {}

    public record RecommendedPlace(
            Long placeId,
            String name,
            String type,
            String category,
            String description,
            Double latitude,
            Double longitude,
            String address,      // 프론트엔드 카드에 표시
            String imageUrl      // 프론트엔드 카드 이미지
    ) {}

    public record RouteInfo(
            List<String> optimizedRoute,  // 최적화된 도시 순서
            String algorithm,              // 사용된 알고리즘
            Double totalDistanceKm         // 총 이동 거리
    ) {}
}
