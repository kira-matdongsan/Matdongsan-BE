package com.example.matdongsan.controller;

import com.example.matdongsan.common.util.email.EmailSender;
import com.example.matdongsan.common.util.email.EmailTemplateRenderer;
import com.example.matdongsan.controller.dto.EmailRequestDto;
import com.example.matdongsan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailTemplateRenderer emailTemplateService;
    private final EmailSender emailSender;

    @PostMapping("/email-verification/send")
    public ResponseEntity<?> sendTestEmail(@RequestBody EmailRequestDto request) {
        String subject = "맛동산 이메일 인증 코드입니다.";
        String html = emailTemplateService.buildTemplate("email/verification", Map.of("code", "999999"));
        emailSender.send(request.getTo(), subject, html);
        return ResponseEntity.ok("메일 전송 완료!");
    }

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
