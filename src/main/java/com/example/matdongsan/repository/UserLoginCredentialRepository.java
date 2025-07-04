package com.example.matdongsan.repository;

import com.example.matdongsan.domain.LoginType;
import com.example.matdongsan.domain.UserLoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginCredentialRepository extends JpaRepository<UserLoginCredential, Long> {

    boolean existsByEmail(String email);
    Optional<UserLoginCredential> findByLoginTypeAndEmail(LoginType loginType, String email);

}
