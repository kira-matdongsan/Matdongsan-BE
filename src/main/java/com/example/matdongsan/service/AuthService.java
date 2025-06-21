package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.common.util.email.EmailSender;
import com.example.matdongsan.common.util.email.EmailTemplateRenderer;
import com.example.matdongsan.controller.dto.TermsResponseDto;
import com.example.matdongsan.domain.*;
import com.example.matdongsan.infrastructure.redis.RedisEmailVerificationStore;
import com.example.matdongsan.repository.*;
import com.example.matdongsan.service.dto.SignupServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final RedisEmailVerificationStore redisEmailVerificationStore;
    private final EmailTemplateRenderer emailTemplateRenderer;
    private final EmailSender emailSender;

    private final PasswordEncoder passwordEncoder;

    private final TermsRepository termsRepository;
    private final UserRepository userRepository;
    private final UserLoginCredentialRepository userLoginCredentialRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final UserProfileRepository userProfileRepository;

    // 약관 목록 조회
    public List<TermsResponseDto> getAllTerms() {
        List<Terms> terms = termsRepository.findAllByActiveTrueOrderByOrderNumAsc();
        return terms.stream().map(TermsResponseDto::of).toList();
    }

    // 인증 번호 메일 발송
    public void sendVerificationEmail(String email) {
        if (!checkEmailAvailable(email)) throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        String code = generateCode();

        String subject = "맛동산 이메일 인증 코드입니다.";
        String html = emailTemplateRenderer.buildTemplate("email/verification", Map.of("code", code));
        emailSender.send(email, subject, html);

        redisEmailVerificationStore.saveCode(email, code);
    }

    // 인증 번호 검증
    public void verifyCode(String email, String inputCode) {
        String savedCode = redisEmailVerificationStore.getCode(email);

        if (!Objects.equals(savedCode, inputCode)) {
            long fails = redisEmailVerificationStore.incrementFailCount(email);
            if (fails >= 5) {
                redisEmailVerificationStore.deleteCode(email);
                redisEmailVerificationStore.deleteFailCount(email);
                throw new CustomException(ErrorCode.BAD_REQUEST, "인증 시도 횟수 초과 : " + fails);
            }
            throw new CustomException(ErrorCode.BAD_REQUEST, "인증번호 불일치");
        }

        // 인증 성공 → verified_email:{email} 저장 (10분 유지)
        redisEmailVerificationStore.markVerified(email);

        // 기존 인증번호 + 실패 카운트는 삭제
        redisEmailVerificationStore.deleteCode(email);
        redisEmailVerificationStore.deleteFailCount(email);
    }

    // 이메일 중복 검사
    public boolean checkEmailAvailable(String email) {
        return !userLoginCredentialRepository.existsByEmail(email);
    }

    // 회원가입
    @Transactional
    public void signup(SignupServiceDto serviceDto) {
        String email = serviceDto.getEmail();
        boolean isVerified = redisEmailVerificationStore.isVerified(email);

        // 이메일 인증 확인
        if (!isVerified) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "이메일 인증이 필요합니다.");
        }

        // 약관 동의 확인
        Set<Long> requiredTermsIds = termsRepository.findRequiredTermsIds();
        List<Long> agreedTermsIds = serviceDto.getTermsIds();

        boolean agreedAllRequired = new HashSet<>(agreedTermsIds).containsAll(requiredTermsIds);
        if (!agreedAllRequired) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "필수 약관에 동의해야 합니다.");
        }

        // 이메일 중복 확인
        if (userLoginCredentialRepository.existsByEmail(email)) throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        // 유저 생성 로직
        String encodedPassword = passwordEncoder.encode(serviceDto.getPassword());

        User user = User.create();
        UserProfile profile = UserProfile.createDefault(user);
        UserLoginCredential credential = UserLoginCredential.createEmailLogin(user, email, encodedPassword);
        List<UserAgreement> agreements = termsRepository.findAllById(serviceDto.getTermsIds())
                .stream()
                .map(term -> UserAgreement.from(user, term))
                .toList();

        user.setProfile(profile);
        user.setLoginCredentials(List.of(credential));
        user.setAgreements(agreements);

        userRepository.save(user);

        // 가입 완료 후 인증 상태 삭제
        redisEmailVerificationStore.deleteVerified(email);
    }

    private String generateCode() {
        return String.format("%04d", new Random().nextInt(9999));
    }
}
