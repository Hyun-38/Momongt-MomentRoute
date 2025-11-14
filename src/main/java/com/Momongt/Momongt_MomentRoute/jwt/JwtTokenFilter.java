package com.Momongt.Momongt_MomentRoute.jwt;

import com.Momongt.Momongt_MomentRoute.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰 필터 클래스
 * 
 * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출하고 검증합니다.
 * 유효한 토큰인 경우 요청에 사용자 정보를 추가합니다.
 * 
 * 주요 기능:
 * - Authorization 헤더에서 Bearer 토큰 추출
 * - 토큰 유효성 검증
 * - 토큰에서 사용자 정보 추출하여 request attribute에 저장
 * - 토큰이 없거나 유효하지 않은 경우 요청 계속 진행 (인증이 필요한 경우 컨트롤러에서 처리)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // 토큰 유효성 검증
                if (jwtService.validateToken(token)) {
                    // 토큰 타입 확인 (ACCESS 토큰만 허용)
                    String tokenType = jwtService.extractTokenType(token);
                    if ("ACCESS".equals(tokenType)) {
                        // 사용자 정보 추출
                        Long userId = jwtService.extractUserId(token);
                        String email = jwtService.extractEmail(token);

                        // Request attribute에 사용자 정보 저장 (컨트롤러에서 사용 가능)
                        request.setAttribute("userId", userId);
                        request.setAttribute("email", email);

                        log.debug("JWT 토큰 검증 성공 - 사용자 ID: {}, 이메일: {}", userId, email);
                    } else {
                        log.warn("ACCESS 토큰이 아닙니다. 토큰 타입: {}", tokenType);
                    }
                } else {
                    log.warn("유효하지 않은 JWT 토큰");
                }
            }
            
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 오류 발생", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Refresh Token 검증
     * 
     * @param refreshToken 검증할 Refresh Token
     * @return true: 유효함, false: 유효하지 않음
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (jwtService.validateToken(refreshToken)) {
                String tokenType = jwtService.extractTokenType(refreshToken);
                return "REFRESH".equals(tokenType);
            }
            return false;
        } catch (Exception e) {
            log.error("Refresh Token 검증 중 오류 발생", e);
            return false;
        }
    }
}

