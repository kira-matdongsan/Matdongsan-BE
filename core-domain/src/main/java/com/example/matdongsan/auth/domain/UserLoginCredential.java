package com.example.matdongsan.auth.domain;

import com.example.matdongsan.auth.enums.LoginType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserLoginCredential {

    private Long id;
    private Long userId;
    private LoginType loginType;
    private String email;
    private String password;
    private String oauthId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserLoginCredential createEmailLogin(Long userId, String email, String encryptedPassword) {
        return UserLoginCredential.builder()
                .userId(userId)
                .loginType(LoginType.EMAIL)
                .email(email)
                .password(encryptedPassword)
                .build();
    }

    public static UserLoginCredential createOauthLogin(Long userId, LoginType loginType, String email, String oauthId) {
        return UserLoginCredential.builder()
                .userId(userId)
                .loginType(loginType)
                .email(email)
                .oauthId(oauthId)
                .build();
    }
}
