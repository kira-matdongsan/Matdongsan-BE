package com.example.matdongsan.controller;

import com.example.matdongsan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/email-verification/request")
    public ResponseEntity<?> requestVerification(@RequestParam String email) {
        authService.createVerificationCode(email);
        return ResponseEntity.ok("인증번호 발송 완료");
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        authService.verifyCode(email, code);
        return ResponseEntity.ok("인증 성공");
    }
}
