package com.example.matdongsan.jpa.entity.auth;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import com.example.matdongsan.jpa.entity.user.UserEntity;
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
@Table(name = "user_login_credential")
public class UserLoginCredentialEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;
    private String email;
    private String password;
    private String oauthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
