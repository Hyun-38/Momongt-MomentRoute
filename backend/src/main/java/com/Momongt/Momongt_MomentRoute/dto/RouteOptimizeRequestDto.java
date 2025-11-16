package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 경로 최적화 요청 DTO (GPT 추천 없이 경로만)
 */
@Schema(description = "경로 최적화 요청")
public record RouteOptimizeRequestDto(
        @Schema(description = "경유지 도시 목록", example = "[\"동두천\", \"의정부\"]")
        List<String> viaCities,

        @Schema(description = "목적지 도시", example = "수원")
        String destinationCity
) {}

