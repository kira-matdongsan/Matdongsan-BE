package com.example.matdongsan.common.util.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
