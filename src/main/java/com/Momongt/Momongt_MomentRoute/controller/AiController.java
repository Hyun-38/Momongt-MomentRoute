package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.TravelDto;
import com.Momongt.Momongt_MomentRoute.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/recommend")
    public ResponseEntity<TravelDto.RecommendedCourseResponse> recommendCourse(
            @RequestBody TravelDto request
    ) {
        return ResponseEntity.ok(aiService.recommendCourse(request));
    }
}
