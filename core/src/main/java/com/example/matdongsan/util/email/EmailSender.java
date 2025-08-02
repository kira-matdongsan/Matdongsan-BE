package com.example.matdongsan.util.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
