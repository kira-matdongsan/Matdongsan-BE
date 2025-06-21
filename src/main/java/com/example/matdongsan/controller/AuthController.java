package com.example.matdongsan.controller;

import com.example.matdongsan.common.response.CommonResponse;
import com.example.matdongsan.common.response.ListResponse;
import com.example.matdongsan.common.response.ResponseCode;
import com.example.matdongsan.common.response.ResultResponse;
import com.example.matdongsan.controller.dto.SignupRequestDto;
import com.example.matdongsan.controller.dto.TermsResponseDto;
import com.example.matdongsan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CommonResponse<ListResponse<TermsResponseDto>>> getAllTerms() {
        return CommonResponse.successList(ResponseCode.OK, authService.getAllTerms());
    }

    // 인증 번호 메일 발송
    @Operation(summary = "인증 번호 메일 발송", description = "요청된 메일로 인증 번호 발송")
    @PostMapping("/email-verification/request")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> requestVerification(
            @Parameter(name = "email", description = "인증 번호를 발송할 이메일 주소", example = "test@test.com")
            @RequestParam String email
    ) {
        authService.sendVerificationEmail(email);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 인증 번호 검증
    @Operation(summary = "인증 번호 검증", description = "인증 번호를 입력하여 검증")
    @PostMapping("/email-verification/verify")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> verifyCode(
            @Parameter(name = "email", description = "인증 번호를 수신한 이메일 주소", example = "test@test.com")
            @RequestParam String email,
            @Parameter(name = "code", description = "메일로 수신한 인증 번호", example = "1234")
            @RequestParam String code
    ) {
        authService.verifyCode(email, code);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 이메일 중복 검사
    @Operation(summary = "이메일 중복 검사", description = "이미 가입된 이메일 주소인지 확인")
    @GetMapping("/check-email")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> checkEmail(
            @Parameter(name = "email", description = "중복 검사할 이메일 주소")
            @RequestParam String email
    ) {
        return CommonResponse.successResult(ResponseCode.OK, authService.checkEmailAvailable(email));
    }

    // 이메일 회원가입
    @Operation(summary = "이메일 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        authService.signup(requestDto.toServiceDto());
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 로그인
}
