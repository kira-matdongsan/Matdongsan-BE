package com.example.matdongsan.auth.application.service;

import com.example.matdongsan.auth.application.dto.*;
import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.auth.repository.AuthCommandRepository;
import com.example.matdongsan.auth.repository.AuthQueryRepository;
import com.example.matdongsan.common.util.auth.JwtUtil;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.external.oauth.OauthService;
import com.example.matdongsan.external.oauth.dto.OauthResponseDto;
import com.example.matdongsan.redis.entity.EmailVerification;
import com.example.matdongsan.redis.entity.RefreshToken;
import com.example.matdongsan.redis.entity.VerifiedEmail;
import com.example.matdongsan.redis.repository.EmailVerificationRepository;
import com.example.matdongsan.redis.repository.RefreshTokenRepository;
import com.example.matdongsan.redis.repository.VerifiedEmailRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserCommandRepository;
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

    private final AuthQueryRepository authQueryRepository;
    private final AuthCommandRepository authCommandRepository;
    private final UserCommandRepository userCommandRepository;

    private final EmailVerificationRepository emailVerificationRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 약관 목록 조회
    public List<TermsServiceDto> getAllTerms() {
        return authQueryRepository.findAllActiveTerms()
                .stream()
                .map(TermsServiceDto::from)
                .toList();
    }

    // 이메일 중복 검사
    public boolean checkEmailAvailable(String email) {
        return !authQueryRepository.existsCredentialByEmail(email);
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
    public void signup(SignupParam param) {
        String email = param.getEmail();

        // 이메일 인증 확인
        VerifiedEmail verifiedEmail = verifiedEmailRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "이메일 인증이 필요합니다."));

        // 약관 동의 확인
        Set<Long> requiredTermsIds = authQueryRepository.findRequiredTermsIds();
        List<Long> agreedTermsIds = param.getTermsIds();

        boolean agreedAllRequired = new HashSet<>(agreedTermsIds).containsAll(requiredTermsIds);
        if (!agreedAllRequired) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "필수 약관에 동의해야 합니다.");
        }

        // 이메일 중복 확인
        if (authQueryRepository.existsCredentialByEmail(email)) throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        // 유저 생성 로직
        String encodedPassword = passwordEncoder.encode(param.getPassword());

        User user = User.create();
        User savedUser = userCommandRepository.save(user);

        UserProfile profile = UserProfile.createDefault(savedUser.getId());
        userCommandRepository.saveProfile(profile);

        UserLoginCredential credential = UserLoginCredential.createEmailLogin(savedUser.getId(), email, encodedPassword);
        authCommandRepository.saveCredential(credential);

        List<UserAgreement> agreements = param.getTermsIds().stream()
                .map(termsId -> UserAgreement.create(savedUser.getId(), termsId))
                .toList();
        userCommandRepository.saveAllAgreements(agreements);

        // 가입 완료 후 인증 상태 삭제
        verifiedEmailRepository.delete(verifiedEmail);
    }

    // 이메일 로그인
    public TokenServiceDto signin(SigninParam param) {
        String email = param.getEmail();
        String password = param.getPassword();

        UserLoginCredential credential = authQueryRepository.findCredentialByLoginTypeAndEmail(LoginType.EMAIL, email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, credential.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return generateTokens(credential.getUserId(), LoginType.EMAIL, email);
    }

    // Oauth2 (카카오/네이버) 로그인
    @Transactional
    public TokenServiceDto oauthSignin(OauthSigninParam param) {
        LoginType loginType = param.getLoginType();
        String token = param.getToken();

        OauthResponseDto oauthResponseDto = oauthService.signin(loginType, token);
        String email = oauthResponseDto.getEmail();

        Long userId;

        if (authQueryRepository.existsCredentialByLoginTypeAndEmail(loginType, email)) {
            // 로그인
            UserLoginCredential credential = authQueryRepository.findCredentialByLoginTypeAndEmail(loginType, email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            userId = credential.getUserId();
        } else {
            // 회원가입
            if (authQueryRepository.existsCredentialByEmail(email))
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL, "이미 회원가입한 이메일입니다. 다른 방법으로 로그인을 시도해주세요.");

            User user = User.create();
            User savedUser = userCommandRepository.save(user);
            userId = savedUser.getId();

            UserProfile profile = UserProfile.createOauth(savedUser.getId(), oauthResponseDto.getNickname(), oauthResponseDto.getProfileImageUrl());
            userCommandRepository.saveProfile(profile);

            UserLoginCredential credential = UserLoginCredential.createOauthLogin(savedUser.getId(), loginType, email, oauthResponseDto.getOauthId());
            authCommandRepository.saveCredential(credential);
        }

        return generateTokens(userId, loginType, email);
    }

    // 토큰 재발급 (이메일 로그인)
    public TokenServiceDto reissue(ReissueParam param) {
        String accessToken = param.getAccessToken();
        String refreshToken = param.getRefreshToken();

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

        return TokenServiceDto.of(newAccessToken, newRefreshToken);
    }

    private String generateCode() {
        return String.format("%04d", new Random().nextInt(9999));
    }

    private TokenServiceDto generateTokens(Long userId, LoginType loginType, String email) {
        String accessToken = jwtUtil.generateAccessToken(userId, loginType, email);
        Claims accessTokenClaim = jwtUtil.parseClaims(accessToken);
        String accessTokenJti = accessTokenClaim.getId();

        String refreshToken = jwtUtil.generateRefreshToken(userId, loginType);

        refreshTokenRepository.save(RefreshToken.of(userId, loginType, accessTokenJti, refreshToken));

        return TokenServiceDto.of(accessToken, refreshToken);
    }

}
