package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 경로 최적화 결과만 담는 DTO
 */
@Schema(description = "경로 최적화 결과")
public record OptimizedRouteDto(
        @Schema(description = "최적화된 경로 (도시 순서)")
        List<CityInfo> route,

        @Schema(description = "사용된 최적화 알고리즘", example = "Greedy (Nearest Neighbor)")
        String algorithm,

        @Schema(description = "총 이동 거리 (km)", example = "45.2")
        double totalDistanceKm
) {
    @Schema(description = "도시 정보")
    public record CityInfo(
            @Schema(description = "도시 ID", example = "1")
            Long cityId,

            @Schema(description = "도시 이름", example = "수원")
            String cityName,

            @Schema(description = "위도", example = "37.2636")
            Double latitude,

            @Schema(description = "경도", example = "127.0286")
            Double longitude,

            @Schema(description = "방문 순서 (1부터 시작)", example = "1")
            int order
    ) {}
}

