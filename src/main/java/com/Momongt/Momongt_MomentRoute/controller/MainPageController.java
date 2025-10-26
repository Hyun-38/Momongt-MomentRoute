package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.MainPageResponseDto;
import com.Momongt.Momongt_MomentRoute.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/main")
    public MainPageResponseDto getMainPageInfo() {
        return mainPageService.getMainPageInfo();
    }
}
