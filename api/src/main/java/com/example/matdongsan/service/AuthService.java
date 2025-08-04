package com.example.matdongsan.service;

import com.example.matdongsan.controller.dto.SigninResponseDto;
import com.example.matdongsan.controller.dto.TermsResponseDto;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.external.oauth.OauthService;
import com.example.matdongsan.external.oauth.dto.OauthResponseDto;
import com.example.matdongsan.jpa.entity.*;
import com.example.matdongsan.jpa.repository.*;
import com.example.matdongsan.redis.entity.EmailVerification;
import com.example.matdongsan.redis.entity.RefreshToken;
import com.example.matdongsan.redis.entity.VerifiedEmail;
import com.example.matdongsan.redis.repository.EmailVerificationRepository;
import com.example.matdongsan.redis.repository.RefreshTokenRepository;
import com.example.matdongsan.redis.repository.VerifiedEmailRepository;
import com.example.matdongsan.service.dto.OauthSigninServiceDto;
import com.example.matdongsan.service.dto.ReissueServiceDto;
import com.example.matdongsan.service.dto.SigninServiceDto;
import com.example.matdongsan.service.dto.SignupServiceDto;
import com.example.matdongsan.util.auth.JwtUtil;
import com.example.matdongsan.util.email.EmailSender;
import com.example.matdongsan.util.email.EmailTemplateRenderer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final EmailTemplateRenderer emailTemplateRenderer;
    private final EmailSender emailSender;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final OauthService oauthService;

    private final TermsRepository termsRepository;
    private final UserRepository userRepository;
    private final UserLoginCredentialRepository userLoginCredentialRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final UserProfileRepository userProfileRepository;

    private final EmailVerificationRepository emailVerificationRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 약관 목록 조회
    public List<TermsResponseDto> getAllTerms() {
        List<Terms> terms = termsRepository.findAllByActiveTrueOrderByOrderNumAsc();
        return terms.stream().map(TermsResponseDto::of).toList();
    }

    // 이메일 중복 검사
    public boolean checkEmailAvailable(String email) {
        return !userLoginCredentialRepository.existsByEmail(email);
    }

    // 인증 번호 메일 발송
    public void sendVerificationEmail(String email) {
        if (!checkEmailAvailable(email)) throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        String code = generateCode();

        String subject = "맛동산 이메일 인증 코드입니다.";
                String html = emailTemplateRenderer.buildTemplate("email/verification", Map.of("code", code));
        emailSender.send(email, subject, html);

        emailVerificationRepository.save(EmailVerification.of(email, code));
    }

    // 인증 번호 검증
    public void verifyCode(String email, String inputCode) {
        EmailVerification emailVerification = emailVerificationRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "이메일 발송을 재시도하세요."));
        String savedCode = emailVerification.getCode();

        if (!savedCode.equals(inputCode)) {
            emailVerification.incrementFailCount();
            emailVerificationRepository.save(emailVerification);

            long failCount = emailVerification.getFailCount();

            if (failCount >= 5) {
                emailVerificationRepository.delete(emailVerification);
                throw new CustomException(ErrorCode.BAD_REQUEST, "인증 시도 횟수 초과 : " + failCount);
            }
            throw new CustomException(ErrorCode.BAD_REQUEST, "인증번호 불일치");
        }

        // 인증 성공 → verified_email:{email} 저장 (10분 유지)
        verifiedEmailRepository.save(VerifiedEmail.of(email));

        // 기존 인증번호 + 실패 카운트는 삭제
        emailVerificationRepository.delete(emailVerification);
    }

    // 이메일 회원가입
    @Transactional
    public void signup(SignupServiceDto serviceDto) {
        String email = serviceDto.getEmail();

        // 이메일 인증 확인
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "이메일 인증이 필요합니다."));

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
        verifiedEmailRepository.delete(verifiedEmail);
    }

    // 이메일 로그인
    public SigninResponseDto signin(SigninServiceDto serviceDto) {
        String email = serviceDto.getEmail();
        String password = serviceDto.getPassword();

        UserLoginCredential userLoginCredential = userLoginCredentialRepository.findByLoginTypeAndEmail(LoginType.EMAIL, email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User user = userLoginCredential.getUser();

        if (!passwordEncoder.matches(password, userLoginCredential.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        String[] tokens = generateTokens(user, LoginType.EMAIL, email);

        return SigninResponseDto.builder()
                .accessToken(tokens[0])
                .refreshToken(tokens[1])
                .build();
    }

    // Oauth2 (카카오/네이버) 로그인
    @Transactional
    public SigninResponseDto oauthSignin(OauthSigninServiceDto serviceDto) {
        LoginType loginType = serviceDto.getLoginType();
        String token = serviceDto.getToken();

        OauthResponseDto oauthResponseDto = oauthService.signin(loginType, token);
        String email = oauthResponseDto.getEmail();

        User user;

        if (userLoginCredentialRepository.existsByLoginTypeAndEmail(loginType, email)) {
            // 로그인
            UserLoginCredential userLoginCredential = userLoginCredentialRepository.findByLoginTypeAndEmail(loginType, email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            user = userLoginCredential.getUser();
        } else {
            // 회원가입
            if (userLoginCredentialRepository.existsByEmail(email))
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL, "이미 회원가입한 이메일입니다. 다른 방법으로 로그인을 시도해주세요.");

            user = User.create();
            UserProfile profile = UserProfile.createOauth(user, oauthResponseDto.getNickname(), oauthResponseDto.getProfileImageUrl());
            UserLoginCredential credential = UserLoginCredential.createOauthLogin(user, loginType, email, oauthResponseDto.getOauthId());

            user.setProfile(profile);
            user.setLoginCredentials(List.of(credential));

            userRepository.save(user);
        }

        String[] tokens = generateTokens(user, loginType, email);

        return SigninResponseDto.builder()
                .accessToken(tokens[0])
                .refreshToken(tokens[1])
                .build();
    }

    // 토큰 재발급 (이메일 로그인)
    public SigninResponseDto reissue(ReissueServiceDto serviceDto) {
        String accessToken = serviceDto.getAccessToken();
        String refreshToken = serviceDto.getRefreshToken();

        Long userId = jwtUtil.getUserId(accessToken);
        LoginType loginType = jwtUtil.getLoginType(accessToken);

        Claims accessClaims = jwtUtil.parseClaims(accessToken);
        String accessJti = accessClaims.getId();

        RefreshToken storedRefreshToken = refreshTokenRepository.findById(RefreshToken.buildKey(userId, loginType, accessJti))
                .orElseThrow(() -> new RuntimeException("Expired refresh token"));
        if (!refreshToken.equals(storedRefreshToken.getRefreshTokenValue())) {
            throw new RuntimeException("Invalid refresh token");
        }

        refreshTokenRepository.deleteById(RefreshToken.buildKey(userId, loginType, accessJti));

        String newAccessToken = jwtUtil.generateAccessToken(userId, loginType, jwtUtil.getEmail(accessToken));
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, loginType);

        Claims newAccessClaims = jwtUtil.parseClaims(newAccessToken);
        String newAccessJti = newAccessClaims.getId();

        refreshTokenRepository.save(RefreshToken.of(userId, loginType, newAccessJti, newRefreshToken));

        return SigninResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String generateCode() {
        return String.format("%04d", new Random().nextInt(9999));
    }

    private String[] generateTokens(User user, LoginType loginType, String email) {
        String accessToken = jwtUtil.generateAccessToken(user.getId(), loginType, email);
        Claims accessTokenClaim = jwtUtil.parseClaims(accessToken);
        String accessTokenJti = accessTokenClaim.getId();

        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), loginType);

        refreshTokenRepository.save(RefreshToken.of(user.getId(), loginType, accessTokenJti, refreshToken));

        return new String[]{accessToken, refreshToken};
    }

}
