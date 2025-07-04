package com.example.matdongsan.common.util;

import com.example.matdongsan.domain.LoginType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expire}") long accessTokenExpTime,
            @Value("${jwt.refresh-token-expire}") long refreshTokenExpTime
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String generateAccessToken(Long userId, LoginType loginType, String email) {
        return createToken(userId, Map.of("loginType", loginType, "email", email), accessTokenExpTime);
    }

    public String generateRefreshToken(Long userId, LoginType loginType) {
        return createToken(userId, Map.of("loginType", loginType), refreshTokenExpTime);
    }

    private String createToken(Long userId, Map<String, Object> claims, long expireIn) {
        String jti = UUID.randomUUID().toString();
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime exp = now.plusSeconds(expireIn);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claims(claims)
                .id(jti)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(exp.toInstant()))
                .signWith(key)
                .compact();
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public LoginType getLoginType(String token) {
        String typeStr = parseClaims(token).get("loginType", String.class);
        return LoginType.valueOf(typeStr);
    }

    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 토큰이 만료되었더라도 클레임은 얻을 수 있음
        }
    }
}
