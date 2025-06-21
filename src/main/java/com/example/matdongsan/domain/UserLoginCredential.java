package com.example.matdongsan.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserLoginCredential extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LoginType loginType;
    private String email;
    private String password;
    private String oauthId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static UserLoginCredential createEmailLogin(User user, String email, String encryptedPassword) {
        return UserLoginCredential.builder()
                .loginType(LoginType.EMAIL)
                .email(email)
                .password(encryptedPassword)
                .user(user)
                .build();
    }
}
