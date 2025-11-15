package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import com.Momongt.Momongt_MomentRoute.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/travel")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    /** 여행 저장 */
    @PostMapping("/save")
    public ResponseEntity<TravelDto.RecommendedCourseResponse> saveTrip(
            @RequestAttribute("userId") Long userId,
            @RequestBody TravelDto request
    ) {
        // Service 반환 타입이 RecommendedCourseResponse이므로 DTO 타입을 그대로 사용
        return ResponseEntity.ok(travelService.saveTrip(userId, request));
    }

    /** 여행 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity<TravelDto.RecommendedCourseResponse> list(
            @RequestAttribute("userId") Long userId
    ) {
        // Service.listTrips() 반환 타입과 맞춤
        return ResponseEntity.ok(travelService.listTrips(userId));
    }

    /** 여행 상세 조회 */
    @GetMapping("/{tripId}")
    public ResponseEntity<TravelDto.RecommendedCourse> detail(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long tripId
    ) {
        // Service.detail() 반환 타입과 맞춤
        return ResponseEntity.ok(travelService.detail(userId, tripId));
    }
}
