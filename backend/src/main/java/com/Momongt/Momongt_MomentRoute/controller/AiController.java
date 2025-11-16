package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.OptimizedRouteDto;
import com.Momongt.Momongt_MomentRoute.dto.RouteOptimizeRequestDto;
import com.Momongt.Momongt_MomentRoute.dto.RouteRequestDto;
import com.Momongt.Momongt_MomentRoute.dto.RouteResponseDto;
import com.Momongt.Momongt_MomentRoute.dto.SimpleRouteDto;
import com.Momongt.Momongt_MomentRoute.entity.City;
import com.Momongt.Momongt_MomentRoute.entity.Place;
import com.Momongt.Momongt_MomentRoute.repository.CityRepository;
import com.Momongt.Momongt_MomentRoute.repository.PlaceRepository;
import com.Momongt.Momongt_MomentRoute.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
@Tag(name = "경로 추천 API", description = "여행 경로 최적화 및 AI 추천")
public class AiController {

    private final AiService aiService;
    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;

    @PostMapping("/simple")
    @Operation(
            summary = "간단한 경로 순서만 받기 (초고속 ~50ms)",
            description = """
                    최적화된 경로를 도시명 리스트로만 반환합니다. 가장 빠릅니다.
                    
                    **사용 시나리오**: 프론트에서 경로 순서만 간단하게 받아서 처리하고 싶을 때
                    
                    **요청 예시**:
                    ```json
                    {
                      "viaCities": ["동두천", "의정부"],
                      "destinationCity": "수원"
                    }
                    ```
                    
                    **응답 예시**:
                    ```json
                    {
                      "route": ["동두천", "의정부", "수원"],
                      "algorithm": "BRUTE_FORCE",
                      "totalDistanceKm": 71.32
                    }
                    ```
                    
                    **특징**:
                    - ✅ 가장 빠른 응답 (~50ms)
                    - ✅ 도시명만 리스트로 반환
                    - ✅ 프론트에서 바로 사용 가능
                    """
    )
    public ResponseEntity<SimpleRouteDto> getSimpleRoute(@RequestBody RouteOptimizeRequestDto request) {
        return ResponseEntity.ok(aiService.getSimpleRoute(request));
    }

    @PostMapping("/optimize")
    @Operation(
            summary = "경로 최적화만 수행 (빠름 ~100ms)",
            description = """
                    경유지와 목적지 간의 최적 경로만 계산합니다. GPT 호출이 없어 빠릅니다.
                    
                    **사용 시나리오**: 사용자가 선택한 경유지와 목적지의 최적 경로를 먼저 지도에 표시하고 싶을 때
                    
                    **요청 예시**:
                    ```json
                    {
                      "viaCities": ["동두천", "의정부"],
                      "destinationCity": "수원"
                    }
                    ```
                    
                    **응답 내용**:
                    - route: 최적화된 도시 순서 (위도/경도 포함)
                    - algorithm: 사용된 최적화 알고리즘
                    - totalDistanceKm: 총 이동 거리(km)
                    """
    )
    public ResponseEntity<OptimizedRouteDto> optimizeRoute(@RequestBody RouteOptimizeRequestDto request) {
        return ResponseEntity.ok(aiService.optimizeRouteOnly(request));
    }

    @PostMapping("/recommend")
    @Operation(
            summary = "AI 여행 추천 (느림 ~3-5초)",
            description = """
                    경로 최적화 + GPT 기반 장소 추천을 함께 제공합니다.
                    
                    **사용 시나리오**: 최적 경로와 각 도시별 추천 장소를 한 번에 받고 싶을 때
                    
                    **요청 예시**:
                    ```json
                    {
                      "viaCities": ["동두천", "의정부"],
                      "destinationCity": "수원",
                      "preferredCategories": ["양식", "한식"]
                    }
                    ```
                    
                    **응답 내용**:
                    - cities: 각 도시별 추천 장소 (음식점, 관광지, 축제, 전시)
                    - summary: GPT가 생성한 전체 여행 요약
                    - routeInfo: 경로 최적화 정보 (경로, 알고리즘, 총 거리)
                    
                    **추천 장소 구성**:
                    - foods: 음식점 2-3개 (preferredCategories 기반)
                    - attractions: 관광지 2개
                    - festivals: 축제 2개
                    - exhibitions: 전시 2개
                    """
    )
    public ResponseEntity<RouteResponseDto> recommendRoute(@RequestBody RouteRequestDto request) {
        return ResponseEntity.ok(aiService.recommendRoute(request));
    }

    @GetMapping("/cities")
    @Operation(
            summary = "사용 가능한 도시 목록 조회",
            description = """
                    데이터베이스에 등록된 모든 도시 목록을 조회합니다.
                    
                    **응답 내용**:
                    - id: 도시 ID
                    - name: 도시명 (예: 수원, 동두천, 의정부)
                    - latitude: 위도
                    - longitude: 경도
                    """
    )
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @GetMapping("/cities/{cityId}/places")
    @Operation(
            summary = "특정 도시의 장소 목록 조회",
            description = """
                    특정 도시에 등록된 모든 장소(음식점, 관광지, 축제, 전시)를 조회합니다.
                    
                    **경로 파라미터**:
                    - cityId: 도시 ID (예: 1, 2, 3)
                    
                    **응답 내용**:
                    - id: 장소 ID
                    - name: 장소명
                    - type: 장소 타입 (RESTAURANT, ATTRACTION, FESTIVAL, EXHIBITION)
                    - category: 카테고리 (한식, 양식, 문화 등)
                    - description: 상세 설명
                    - address: 주소
                    - latitude/longitude: 위도/경도
                    - imageUrl: 이미지 URL
                    """
    )
    public ResponseEntity<List<Place>> getPlacesByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(placeRepository.findByCity_Id(cityId));
    }
}
