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
@RedisHash(value = "email_verification", timeToLive = 60 * 5)
public class EmailVerification {

    @Id
    private String email;

    private String code;

    private long failCount;

    public static EmailVerification of(String email, String code) {
        return new EmailVerification(email, code, 0);
    }

    public void incrementFailCount() {
        this.failCount++;
    }
}
