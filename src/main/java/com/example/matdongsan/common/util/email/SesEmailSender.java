package com.example.matdongsan.common.util.email;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class SesEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${mail.from.email}")
    private String fromEmail;

    @Value("${mail.from.name}")
    private String fromName;

    @Override
    public void send(String to, String subject, String htmlContent) {
        log.info("[SesEmailSender] Sending email to {}", to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("SES 메일 전송 실패", e);
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
