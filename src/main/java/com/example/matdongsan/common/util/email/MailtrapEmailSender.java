package com.example.matdongsan.common.util.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class MailtrapEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    private final EmailTemplateRenderer templateService;

    @Override
    public void send(String to, String subject, String htmlContent) {
        log.info("[MailtrapEmailSender] Sending email to {}", to);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
