package com.example.matdongsan.redis.entity;

import com.example.matdongsan.jpa.entity.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 7) // 7 days
public class RefreshToken {

    @Id
    private String id;

    private String refreshTokenValue;

    public static RefreshToken of(Long userId, LoginType loginType, String jti, String refreshTokenValue) {
        return new RefreshToken(buildKey(userId, loginType, jti), refreshTokenValue);
    }

    public static String buildKey(Long userId, LoginType loginType, String jti) {
        return String.format("%d:%s:%s", userId, loginType.name(), jti);
    }
}
