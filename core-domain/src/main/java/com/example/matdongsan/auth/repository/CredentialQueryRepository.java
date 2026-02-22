package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;

import java.util.Optional;

public interface CredentialQueryRepository {

    Optional<UserLoginCredential> findByLoginTypeAndEmail(LoginType loginType, String email);

    Optional<UserLoginCredential> findByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByLoginTypeAndEmail(LoginType loginType, String email);
}
