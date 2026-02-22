package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.repository.UserProfileJpaRepository;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserProfileQueryRepositoryImpl implements UserProfileQueryRepository {

    private final UserProfileJpaRepository profileJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserProfile> findByUserId(Long userId) {
        return profileJpaRepository.findByUserId(userId)
                .map(userMapper::toProfileDomain);
    }
}
