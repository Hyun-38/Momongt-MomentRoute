package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.MainPageDto;
import com.Momongt.Momongt_MomentRoute.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Tag(name = "Main Page", description = "메인 페이지 API")
public class MainPageController {

    private final MainPageService mainPageService;

    @Operation(summary = "메인 페이지 조회", description = "이벤트 목록과 환영 메시지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/mainpage")
    public ResponseEntity<MainPageDto> getMainPage() {
        MainPageDto response = mainPageService.getMainPageInfo();
        return ResponseEntity.ok(response);
    }
}