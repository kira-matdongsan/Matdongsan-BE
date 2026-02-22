package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.jpa.entity.auth.UserLoginCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginCredentialJpaRepository extends JpaRepository<UserLoginCredentialEntity, Long> {

    boolean existsByEmail(String email);
    boolean existsByLoginTypeAndEmail(LoginType loginType, String email);
    boolean existsByLoginTypeAndOauthId(LoginType loginType, String oauthId);

    Optional<UserLoginCredentialEntity> findByLoginTypeAndEmail(LoginType loginType, String email);
    Optional<UserLoginCredentialEntity> findByLoginTypeAndOauthId(LoginType loginType, String oauthId);

    Optional<UserLoginCredentialEntity> findFirstByUserIdOrderByIdDesc(Long userId);
}
