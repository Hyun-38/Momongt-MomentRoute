package com.Momongt.Momongt_MomentRoute.dto;

import java.util.List;

public record RouteRequestDto(
        List<String> viaCities,
        String destinationCity,
        List<String> preferredCategories
) {}