package com.example.matdongsan.infrastructure.redis;

import com.example.matdongsan.domain.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisRefreshTokenStore {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "refresh";

    private static final Duration REFRESH_TTL = Duration.ofDays(7);

    public void save(Long userId, LoginType loginType, String jti, String refreshToken) {
        String key = buildKey(userId, loginType, jti);
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TTL);
    }

    public boolean isValid(Long userId, LoginType loginType, String jti, String refreshToken) {
        String key = buildKey(userId, loginType, jti);
        String stored = redisTemplate.opsForValue().get(key);
        return refreshToken.equals(stored);
    }

    public void delete(Long userId, LoginType loginType, String jti) {
        redisTemplate.delete(buildKey(userId, loginType, jti));
    }

    private String buildKey(Long userId, LoginType loginType, String jti) {
        return String.format("%s:%d:%s", KEY_PREFIX, userId, loginType.name(), jti);
    }
}
