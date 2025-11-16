package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 간단한 경로 순서만 담는 DTO
 */
@Schema(description = "간단한 경로 순서 (도시명 리스트)")
public record SimpleRouteDto(
        @Schema(description = "최적화된 도시 순서", example = "[\"동두천\", \"의정부\", \"수원\"]")
        List<String> route,

        @Schema(description = "사용된 최적화 알고리즘", example = "BRUTE_FORCE")
        String algorithm,

        @Schema(description = "총 이동 거리 (km)", example = "71.32")
        double totalDistanceKm
) {}

