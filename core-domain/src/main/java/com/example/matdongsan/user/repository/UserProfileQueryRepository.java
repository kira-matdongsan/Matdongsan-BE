package com.example.matdongsan.user.repository;

import com.example.matdongsan.user.domain.UserProfile;

import java.util.Optional;

public interface UserProfileQueryRepository {

    Optional<UserProfile> findByUserId(Long userId);
}
