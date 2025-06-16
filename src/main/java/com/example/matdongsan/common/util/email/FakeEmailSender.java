package com.example.matdongsan.common.util.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
public class FakeEmailSender implements EmailSender {

    @Override
    public void send(String to, String subject, String htmlContent) {
        log.info("[FakeEmailSender] 메일 전송 생략: to={}, subject={}", to, subject);
    }
}
