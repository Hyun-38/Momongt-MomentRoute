package com.Momongt.Momongt_MomentRoute.jwt;

import com.Momongt.Momongt_MomentRoute.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKeySpec;
    private final int expiration;
    private final int refreshExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits-long-for-HS256-algorithm}") String secretKey,
            @Value("${jwt.expiration:1440}") int expiration,
            @Value("${jwt.refresh-token-expiration:10080}") int refreshExpiration) {

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            keyBytes = padded;
        }
        this.secretKeySpec = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String createAccessToken(Long id, String email, Role role) {
        return createToken(id, email, role, expiration, "ACCESS");
    }

    public String createRefreshToken(Long id, String email, Role role) {
        return createToken(id, email, role, refreshExpiration, "REFRESH");
    }

    private String createToken(Long id, String email, Role role, int expMinutes, String type) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expMinutes * 60 * 1000L);

        return Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("role", role.name())
                .claim("type", type)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKeySpec, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("id", Long.class);
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public String extractTokenType(String token) {
        return extractClaims(token).get("type", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        return "REFRESH".equals(extractTokenType(token)) && validateToken(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKeySpec)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
