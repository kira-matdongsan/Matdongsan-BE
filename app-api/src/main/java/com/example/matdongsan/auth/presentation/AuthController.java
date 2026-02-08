package com.example.matdongsan.auth.presentation;

import com.example.matdongsan.auth.application.dto.TokenServiceDto;
import com.example.matdongsan.auth.presentation.request.OauthSigninRequest;
import com.example.matdongsan.auth.presentation.request.ReissueRequest;
import com.example.matdongsan.auth.presentation.request.SigninRequest;
import com.example.matdongsan.auth.presentation.request.SignupRequest;
import com.example.matdongsan.auth.presentation.response.SigninResponse;
import com.example.matdongsan.auth.presentation.response.TermsResponse;
import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.ListResponse;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "인증 API", description = "인증 관련 API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    // 약관 목록 조회
    @Operation(summary = "약관 목록 조회", description = "전체 약관 목록 조회 (상세 내용 포함)")
    @GetMapping("/terms")
    public ResponseEntity<RestApiResponse<ListResponse<TermsResponse>>> getAllTerms() {
        List<TermsResponse> responses = authService.getAllTerms()
                .stream()
                .map(TermsResponse::from)
                .toList();
        return RestApiResponse.successList(ResponseCode.OK, responses);
    }

    // 인증 번호 메일 발송
    @Operation(summary = "인증 번호 메일 발송", description = "요청된 메일로 인증 번호 발송")
    @PostMapping("/email-verification/request")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> requestVerification(
            @Parameter(name = "templates/email", description = "인증 번호를 발송할 이메일 주소", example = "test@test.com")
            @RequestParam String email
    ) {
        authService.sendVerificationEmail(email);
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    // 인증 번호 검증
    @Operation(summary = "인증 번호 검증", description = "인증 번호를 입력하여 검증")
    @PostMapping("/email-verification/verify")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> verifyCode(
            @Parameter(name = "templates/email", description = "인증 번호를 수신한 이메일 주소", example = "test@test.com")
            @RequestParam String email,
            @Parameter(name = "code", description = "메일로 수신한 인증 번호", example = "1234")
            @RequestParam String code
    ) {
        authService.verifyCode(email, code);
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    // 이메일 중복 검사
    @Operation(summary = "이메일 중복 검사", description = "이미 가입된 이메일 주소인지 확인")
    @GetMapping("/check-email")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> checkEmail(
            @Parameter(name = "templates/email", description = "중복 검사할 이메일 주소")
            @RequestParam String email
    ) {
        return RestApiResponse.successResult(ResponseCode.OK, authService.checkEmailAvailable(email));
    }

    // 이메일 회원가입
    @Operation(summary = "이메일 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> signup(@RequestBody @Valid SignupRequest requestDto) {
        authService.signup(requestDto.toParam());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    // 이메일 로그인
    @Operation(summary = "이메일 로그인")
    @PostMapping("/signin")
    public ResponseEntity<RestApiResponse<SigninResponse>> signin(@RequestBody @Valid SigninRequest requestDto) {
        TokenServiceDto tokenDto = authService.signin(requestDto.toParam());
        return RestApiResponse.success(ResponseCode.OK, SigninResponse.from(tokenDto));
    }

    // Oauth2 로그인
    @Operation(summary = "Oauth2 (카카오/네이버) 로그인")
    @PostMapping("/oauth/signin")
    public ResponseEntity<RestApiResponse<SigninResponse>> oauthSignin(@RequestBody @Valid OauthSigninRequest requestDto) {
        TokenServiceDto tokenDto = authService.oauthSignin(requestDto.toParam());
        return RestApiResponse.success(ResponseCode.OK, SigninResponse.from(tokenDto));
    }

    // 토큰 재발급 (이메일 로그인)
    @Operation(summary = "토큰 재발급 (이메일 로그인)")
    @PostMapping("/reissue")
    public ResponseEntity<RestApiResponse<SigninResponse>> reissue(@RequestBody @Valid ReissueRequest requestDto) {
        TokenServiceDto tokenDto = authService.reissue(requestDto.toParam());
        return RestApiResponse.success(ResponseCode.OK, SigninResponse.from(tokenDto));
    }
}
