package com.Momongt.Momongt_MomentRoute.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT (JSON Web Token) 서비스 클래스
 * 
 * 사용자 인증을 위한 JWT 토큰의 생성, 검증, 파싱 등을 담당합니다.
 * Access Token과 Refresh Token을 생성하고 관리합니다.
 * 
 * 주요 기능:
 * - Access Token 생성 (사용자 ID, 이메일 포함)
 * - Refresh Token 생성 (사용자 ID 포함)
 * - 토큰에서 사용자 정보 추출 (ID, 이메일, 토큰 타입)
 * - 토큰 만료 여부 확인
 * - 토큰 유효성 검증
 * 
 * 보안 특징:
 * - HMAC-SHA256 알고리즘 사용
 * - 환경변수로부터 시크릿 키 관리
 * - 토큰 타입 구분 (ACCESS/REFRESH)
 * - 설정 가능한 만료 시간
 */
@Slf4j
@Service
public class JwtService {
    
    // ==================== JWT 설정 ====================
    
    /**
     * JWT 서명에 사용할 시크릿 키
     * 환경변수 jwt.secret에서 값을 가져옴
     * 보안을 위해 충분히 길고 복잡한 키 사용 권장
     */
    @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits-long-for-HS256-algorithm}")
    private String secret;
    
    /**
     * Access Token 만료 시간 (분)
     * 환경변수 jwt.expiration에서 값을 가져옴
     * 기본값: 1440분 (24시간)
     */
    @Value("${jwt.expiration:1440}")
    private Integer expiration;
    
    /**
     * Refresh Token 만료 시간 (분)
     * 환경변수 jwt.refresh-token-expiration에서 값을 가져옴
     * 기본값: 10080분 (7일)
     */
    @Value("${jwt.refresh-token-expiration:10080}")
    private Integer refreshTokenExpiration;
    
    // ==================== 토큰 생성 메서드들 ====================
    
    /**
     * Access Token 생성
     * 
     * 사용자 ID와 이메일을 포함하여 Access Token을 생성합니다.
     * Access Token은 API 요청 시 인증에 사용되며, 상대적으로 짧은 만료 시간을 가집니다.
     * 
     * @param userId 사용자 고유 식별자
     * @param email 사용자 이메일 주소
     * @return 생성된 Access Token 문자열
     */
    public String generateAccessToken(Long userId, String email) {
        log.debug("Access Token 생성 시작 - 사용자 ID: {}, 이메일: {}", userId, email);
        
        // 토큰에 포함할 클레임 정보 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "ACCESS");
        
        // 토큰 생성 및 반환
        String token = createToken(claims, email, expiration);
        log.debug("Access Token 생성 완료 - 만료 시간: {}분", expiration);
        
        return token;
    }
    
    /**
     * Refresh Token 생성
     * 
     * 사용자 ID를 포함하여 Refresh Token을 생성합니다.
     * Refresh Token은 Access Token 갱신에 사용되며, 상대적으로 긴 만료 시간을 가집니다.
     * 
     * @param userId 사용자 고유 식별자
     * @param email 사용자 이메일 주소
     * @return 생성된 Refresh Token 문자열
     */
    public String generateRefreshToken(Long userId, String email) {
        log.debug("Refresh Token 생성 시작 - 사용자 ID: {}, 이메일: {}", userId, email);
        
        // 토큰에 포함할 클레임 정보 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "REFRESH");
        
        // 토큰 생성 및 반환
        String token = createToken(claims, email, refreshTokenExpiration);
        log.debug("Refresh Token 생성 완료 - 만료 시간: {}분", refreshTokenExpiration);
        
        return token;
    }
    
    /**
     * JWT 토큰 생성 (내부 메서드)
     * 
     * 공통 토큰 생성 로직을 처리합니다.
     * 클레임, 주제, 만료 시간을 설정하여 JWT를 생성합니다.
     * 
     * @param claims 토큰에 포함할 클레임 정보
     * @param subject 토큰의 주제 (사용자 식별자)
     * @param expirationMinutes 토큰 만료 시간 (분)
     * @return 생성된 JWT 토큰 문자열
     */
    private String createToken(Map<String, Object> claims, String subject, Integer expirationMinutes) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMinutes * 60 * 1000L);
        
        return Jwts.builder()
                .claims(claims)                    // 사용자 정의 클레임 설정
                .subject(subject)                  // 토큰 주제 설정
                .issuedAt(now)                     // 토큰 발급 시간
                .expiration(expiryDate)            // 토큰 만료 시간
                .signWith(getSigningKey())         // 서명 키
                .compact();                        // JWT 문자열 생성
    }
    
    // ==================== 토큰 파싱 메서드들 ====================
    
    /**
     * 토큰에서 사용자 ID 추출
     * 
     * JWT 토큰의 클레임에서 사용자 ID를 추출합니다.
     * 
     * @param token JWT 토큰 문자열
     * @return 추출된 사용자 ID
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }
    
    /**
     * 토큰에서 이메일 추출
     * 
     * JWT 토큰의 주제(subject)에서 사용자 이메일을 추출합니다.
     * 
     * @param token JWT 토큰 문자열
     * @return 추출된 사용자 이메일
     */
    public String extractEmail(String token) {
        return extractSubject(token);
    }
    
    /**
     * 토큰에서 토큰 타입 추출
     * 
     * JWT 토큰의 클레임에서 토큰 타입(ACCESS/REFRESH)을 추출합니다.
     * 
     * @param token JWT 토큰 문자열
     * @return 추출된 토큰 타입 ("ACCESS" 또는 "REFRESH")
     */
    public String extractTokenType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("type", String.class);
    }
    
    // ==================== 토큰 검증 메서드들 ====================
    
    /**
     * 토큰 만료 여부 확인
     * 
     * JWT 토큰의 만료 시간을 확인하여 만료되었는지 검증합니다.
     * 
     * @param token 검증할 JWT 토큰 문자열
     * @return true: 만료됨, false: 유효함
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            boolean expired = expiration.before(new Date());
            
            if (expired) {
                log.debug("토큰 만료 확인 - 만료됨");
            } else {
                log.debug("토큰 만료 확인 - 유효함, 만료 시간: {}", expiration);
            }
            
            return expired;
        } catch (Exception e) {
            log.error("토큰 만료 확인 중 오류 발생", e);
            return true; // 오류 발생 시 안전하게 만료된 것으로 처리
        }
    }
    
    /**
     * 토큰 유효성 검증
     * 
     * JWT 토큰의 전반적인 유효성을 검증합니다.
     * 현재는 만료 여부만 확인하지만, 필요에 따라 추가 검증 로직을 구현할 수 있습니다.
     * 
     * @param token 검증할 JWT 토큰 문자열
     * @return true: 유효함, false: 유효하지 않음
     */
    public boolean validateToken(String token) {
        try {
            boolean valid = !isTokenExpired(token);
            log.debug("토큰 유효성 검증 결과: {}", valid);
            return valid;
        } catch (Exception e) {
            log.error("토큰 유효성 검증 중 오류 발생", e);
            return false; // 오류 발생 시 안전하게 유효하지 않은 것으로 처리
        }
    }
    
    // ==================== 내부 헬퍼 메서드들 ====================
    
    /**
     * JWT 서명에 사용할 시크릿 키 생성
     * 
     * 환경변수에서 가져온 시크릿 문자열을 HMAC-SHA256 서명에 적합한 키로 변환합니다.
     * 
     * @return HMAC-SHA256 서명용 SecretKey 객체
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // 최소 256비트(32바이트) 필요
        if (keyBytes.length < 32) {
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            keyBytes = paddedKey;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * 토큰에서 모든 클레임 추출
     * 
     * JWT 토큰을 파싱하여 모든 클레임 정보를 추출합니다.
     * 
     * @param token 파싱할 JWT 토큰 문자열
     * @return 추출된 모든 클레임 정보
     * @throws RuntimeException 토큰 파싱 실패 시
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("토큰 클레임 추출 중 오류 발생", e);
            throw new RuntimeException("토큰 파싱에 실패했습니다.", e);
        }
    }
    
    /**
     * 토큰에서 주제(subject) 추출
     * 
     * JWT 토큰의 주제 정보를 추출합니다.
     * 
     * @param token 추출할 JWT 토큰 문자열
     * @return 추출된 주제 문자열
     */
    private String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }
}
