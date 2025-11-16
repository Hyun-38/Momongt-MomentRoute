package com.Momongt.Momongt_MomentRoute.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();

            try {
                if (tokenProvider.validateToken(token) &&
                        "ACCESS".equals(tokenProvider.extractTokenType(token))) {

                    Long userId = tokenProvider.extractUserId(token);
                    String email = tokenProvider.extractEmail(token);
                    String role = tokenProvider.extractRole(token);

                    if (role != null && !role.isBlank()) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        List.of(new SimpleGrantedAuthority(role))
                                );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    request.setAttribute("userId", userId);
                    request.setAttribute("email", email);
                    request.setAttribute("role", role);

                } else {
                    log.warn("유효하지 않은 토큰 또는 ACCESS 토큰 아님 - 토큰 타입: {}", tokenProvider.extractTokenType(token));
                }
            } catch (Exception ex) {
                log.error("JWT 처리 중 오류 발생", ex);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
