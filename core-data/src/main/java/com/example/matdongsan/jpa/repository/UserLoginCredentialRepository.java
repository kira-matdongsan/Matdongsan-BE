package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.jpa.entity.auth.UserLoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginCredentialRepository extends JpaRepository<UserLoginCredential, Long> {

    boolean existsByEmail(String email);
    boolean existsByLoginTypeAndEmail(LoginType loginType, String email);
    Optional<UserLoginCredential> findByLoginTypeAndEmail(LoginType loginType, String email);

}
