package com.example.matdongsan.user.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserProfile;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<User> findById(Long id);

    Optional<UserProfile> findProfileByUserId(Long userId);

    Optional<UserLoginCredential> findLoginCredentialByUserId(Long userId);
}
