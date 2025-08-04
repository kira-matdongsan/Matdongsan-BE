package com.example.matdongsan.redis.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "verified_email", timeToLive = 60 * 10)
public class VerifiedEmail {

    @Id
    private String email;

    public static VerifiedEmail of(String email) {
        return new VerifiedEmail(email);
    }
}
