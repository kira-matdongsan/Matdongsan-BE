package com.example.matdongsan.repository;

import com.example.matdongsan.domain.UserLoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginCredentialRepository extends JpaRepository<UserLoginCredential, Long> {
}
