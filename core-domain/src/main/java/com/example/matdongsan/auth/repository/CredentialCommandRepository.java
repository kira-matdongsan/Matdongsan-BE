package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;

public interface CredentialCommandRepository {

    UserLoginCredential save(UserLoginCredential credential);

    void softDeleteByUserId(Long userId);
}
