package com.Momongt.Momongt_MomentRoute.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * JWT 관련 유틸리티 클래스
 * 
 * 컨트롤러에서 JWT 필터가 설정한 사용자 정보를 쉽게 가져올 수 있도록 도와주는 헬퍼 메서드를 제공합니다.
 */
public class JwtUtil {

    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String EMAIL_ATTRIBUTE = "email";

    /**
     * Request에서 사용자 ID 추출
     * 
     * JWT 필터가 설정한 userId를 request attribute에서 가져옵니다.
     * 
     * @param request HttpServletRequest
     * @return 사용자 ID (토큰이 없거나 유효하지 않은 경우 null)
     */
    public static Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute(USER_ID_ATTRIBUTE);
        if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }

    /**
     * Request에서 이메일 추출
     * 
     * JWT 필터가 설정한 email을 request attribute에서 가져옵니다.
     * 
     * @param request HttpServletRequest
     * @return 사용자 이메일 (토큰이 없거나 유효하지 않은 경우 null)
     */
    public static String getEmail(HttpServletRequest request) {
        Object email = request.getAttribute(EMAIL_ATTRIBUTE);
        return email != null ? email.toString() : null;
    }

    /**
     * 인증된 사용자인지 확인
     * 
     * @param request HttpServletRequest
     * @return true: 인증됨, false: 인증되지 않음
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        return getUserId(request) != null;
    }
}

