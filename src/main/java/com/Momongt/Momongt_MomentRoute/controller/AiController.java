package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.RouteRequestDto;
import com.Momongt.Momongt_MomentRoute.dto.RouteResponseDto;
import com.Momongt.Momongt_MomentRoute.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/recommend")
    public ResponseEntity<RouteResponseDto> recommendRoute(@RequestBody RouteRequestDto request) {
        return ResponseEntity.ok(aiService.recommendRoute(request));
    }
}
