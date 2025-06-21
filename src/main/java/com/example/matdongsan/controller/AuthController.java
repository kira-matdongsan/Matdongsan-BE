package com.example.matdongsan.controller;

import com.example.matdongsan.common.response.CommonResponse;
import com.example.matdongsan.common.response.ListResponse;
import com.example.matdongsan.common.response.ResponseCode;
import com.example.matdongsan.common.response.ResultResponse;
import com.example.matdongsan.controller.dto.SignupRequestDto;
import com.example.matdongsan.controller.dto.TermsResponseDto;
import com.example.matdongsan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    // 약관 목록 조회
    @GetMapping("/terms")
    public ResponseEntity<CommonResponse<ListResponse<TermsResponseDto>>> getAllTerms() {
        return CommonResponse.successList(ResponseCode.OK, authService.getAllTerms());
    }

    // 인증 번호 메일 발송
    @PostMapping("/email-verification/request")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> requestVerification(@RequestParam String email) {
        authService.sendVerificationEmail(email);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 인증 번호 검증
    @PostMapping("/email-verification/verify")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> verifyCode(@RequestParam String email, @RequestParam String code) {
        authService.verifyCode(email, code);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> checkEmail(@RequestParam String email) {
        return CommonResponse.successResult(ResponseCode.OK, authService.checkEmailAvailable(email));
    }

    // 이메일 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> signup(@RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto.toServiceDto());
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    // 로그인
}
