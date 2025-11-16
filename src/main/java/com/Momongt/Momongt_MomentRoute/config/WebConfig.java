package com.Momongt.Momongt_MomentRoute.config;

import com.Momongt.Momongt_MomentRoute.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web 설정 클래스
 * 
 * JWT 필터 등록 및 CORS 설정을 담당합니다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final JwtTokenFilter jwtTokenFilter;

    /**
     * JWT 필터 등록
     * 
     * 모든 요청에 대해 JWT 토큰을 검증하는 필터를 등록합니다.
     * 
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtTokenFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtTokenFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // 필터 실행 순서
        registration.setName("jwtTokenFilter");
        return registration;
    }
}

