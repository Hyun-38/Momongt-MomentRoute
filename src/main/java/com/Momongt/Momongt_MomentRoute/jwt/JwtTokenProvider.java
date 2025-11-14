package com.Momongt.Momongt_MomentRoute.jwt;

import com.Momongt.Momongt_MomentRoute.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final int expiration;
    private final int refreshExpiration;
    private Key SECRET_KEY;

    public JwtTokenProvider(
            @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits-long-for-HS256-algorithm}") String secretKey,
            @Value("${jwt.expiration:1440}") int expiration,
            @Value("${jwt.refresh-token-expiration:10080}") int refreshExpiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
        
        // Base64로 인코딩된 secret이 아니면 직접 바이트로 변환
        try {
            this.SECRET_KEY = new SecretKeySpec(
                    java.util.Base64.getDecoder().decode(secretKey),
                    "HmacSHA256"
            );
        } catch (IllegalArgumentException e) {
            // Base64가 아니면 직접 바이트로 변환
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            // 최소 256비트(32바이트) 필요
            if (keyBytes.length < 32) {
                byte[] paddedKey = new byte[32];
                System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
                keyBytes = paddedKey;
            }
            this.SECRET_KEY = new SecretKeySpec(keyBytes, "HmacSHA256");
        }
    }

    // 액세스 토큰 생성
    public String createToken(Long id, String email, Role role) {
        Date now = new Date();
        
        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration * 60 * 1000L))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long id, String email, Role role) {
        Date now = new Date();

        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshExpiration * 60 * 1000L))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 토큰에서 userId 꺼내는 헬퍼
    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);
        return ((Number) claims.get("id")).longValue();
    }

    // 토큰에서 email 꺼내는 헬퍼
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // 토큰에서 role 꺼내는 헬퍼
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Claims 추출
    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getEncoded());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

