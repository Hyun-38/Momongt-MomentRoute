package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.RouteRequestDto;
import com.Momongt.Momongt_MomentRoute.dto.RouteResponseDto;
import com.Momongt.Momongt_MomentRoute.entity.City;
import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.repository.CityRepository;
import com.Momongt.Momongt_MomentRoute.repository.PlaceRepository;
import com.Momongt.Momongt_MomentRoute.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;

    @PostMapping("/recommend")
    public ResponseEntity<RouteResponseDto> recommendRoute(@RequestBody RouteRequestDto request) {
        return ResponseEntity.ok(aiService.recommendRoute(request));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @GetMapping("/cities/{cityId}/places")
    public ResponseEntity<List<Place>> getPlacesByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(placeRepository.findByCity_Id(cityId));
    }
}
