package com.example.matdongsan.infrastructure.redis;

import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisEmailVerificationStore {

    private final StringRedisTemplate redisTemplate;

    private static final String EMAIL_VERIFICATION_KEY = "email_verification:";
    private static final String EMAIL_VERIFICATION_FAIL_COUNT_KEY = "email_verification_fail_count:";
    private static final String VERIFIED_EMAIL_KEY = "verified_email:";

    private static final Duration EMAIL_VERIFICATION_TTL = Duration.ofMinutes(5);
    private static final Duration VERIFIED_EMAIL_TTL = Duration.ofMinutes(10);

    // 인증번호 저장
    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(EMAIL_VERIFICATION_KEY + email, code, EMAIL_VERIFICATION_TTL);

        String failKey = EMAIL_VERIFICATION_FAIL_COUNT_KEY + email;
        redisTemplate.opsForValue().set(failKey, "0", EMAIL_VERIFICATION_TTL);
    }

    // 인증번호 가져오기
    public String getCode(String email) {
        if(!redisTemplate.hasKey(EMAIL_VERIFICATION_KEY + email))
            throw new CustomException(ErrorCode.BAD_REQUEST, "이메일 발송을 재시도하세요.");
        return redisTemplate.opsForValue().get(EMAIL_VERIFICATION_KEY + email);
    }

    // 인증번호 삭제
    public void deleteCode(String email) {
        redisTemplate.delete(EMAIL_VERIFICATION_KEY + email);
    }

    // 인증번호 검증 실패 카운트 증가 및 조회
    public long incrementFailCount(String email) {
        String key = EMAIL_VERIFICATION_FAIL_COUNT_KEY + email;
        Long count = redisTemplate.opsForValue().increment(key);
        return count != null ? count : 0;
    }

    // 인증번호 검증 실패 카운트 삭제
    public void deleteFailCount(String email) {
        redisTemplate.delete(EMAIL_VERIFICATION_FAIL_COUNT_KEY + email);
    }

    // 인증 여부 저장
    public void markVerified(String email) {
        redisTemplate.opsForValue().set(VERIFIED_EMAIL_KEY + email, "true", VERIFIED_EMAIL_TTL);
    }

    // 인증 여부 가져오기
    public boolean isVerified(String email) {
        return redisTemplate.hasKey(VERIFIED_EMAIL_KEY + email);
    }

    // 인증 여부 삭제
    public void deleteVerified(String email) {
        redisTemplate.delete(VERIFIED_EMAIL_KEY + email);
    }


}
