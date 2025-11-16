package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.*;
import com.Momongt.Momongt_MomentRoute.entity.City;
import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.repository.CityRepository;
import com.Momongt.Momongt_MomentRoute.repository.PlaceRepository;
import com.Momongt.Momongt_MomentRoute.util.RouteOptimizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;
    private final AiService aiService;

    public RouteResponseDto recommendRoute(RouteRequestDto req) {
        // AiService에서 직접 처리하도록 위임
        return aiService.recommendRoute(req);
    }
}
