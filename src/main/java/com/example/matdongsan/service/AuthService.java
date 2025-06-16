package com.example.matdongsan.service;

import com.example.matdongsan.common.util.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final StringRedisTemplate redisTemplate;

    private final EmailSender emailSender;

    public void sendVerificationEmail(String email, String code) {
        String subject = "[맛동산] 이메일 인증번호 안내";
        String body = String.format("인증번호는 [%s] 입니다. 5분 이내에 입력해주세요.", code);
        emailSender.send(email, subject, body);
    }

    // TODO: 회원가입 로직은 나중에 구현!
    public void register(String email, String password) {
        String verifiedKey = getVerifiedEmailKey(email);
        String isVerified = redisTemplate.opsForValue().get(verifiedKey);

        if (!"true".equals(isVerified)) {
            throw new RuntimeException("이메일 인증이 필요합니다.");
        }

        // 유저 생성 로직
        // userRepository.save(...)

        // 가입 완료 후 인증 상태 삭제
        redisTemplate.delete(verifiedKey);
    }

    public void createVerificationCode(String email) {
        String code = generateCode(); // 예: 6자리 숫자 코드
        String key = getVerificationKey(email);

        redisTemplate.opsForValue()
                .set(key, code, Duration.ofMinutes(5));

        // TODO: 메일 발송 로직은 나중에 구현!
        System.out.printf("Generated code for %s: %s\n", email, code);
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void verifyCode(String email, String inputCode) {
        String key = getVerificationKey(email);
        String savedCode = redisTemplate.opsForValue().get(key);

        if (!Objects.equals(savedCode, inputCode)) {
            throw new RuntimeException("인증번호 불일치");
        }

        // 인증 성공 → verified_email:{email} 저장 (10분 유지)
        String verifiedKey = getVerifiedEmailKey(email);
        redisTemplate.opsForValue().set(verifiedKey, "true", Duration.ofMinutes(10));

        // 기존 인증번호는 삭제
        redisTemplate.delete(key);
    }

    private String getVerificationKey(String email) {
        return "email_verification:" + email;
    }

    private String getVerifiedEmailKey(String email) {
        return "verified_email:" + email;
    }
}
