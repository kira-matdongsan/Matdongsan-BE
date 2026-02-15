package com.example.matdongsan.user.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.mapper.AuthMapper;
import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.jpa.repository.UserLoginCredentialJpaRepository;
import com.example.matdongsan.jpa.repository.UserProfileJpaRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProfileJpaRepository profileJpaRepository;
    private final UserLoginCredentialJpaRepository credentialJpaRepository;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toUserDomain);
    }

    @Override
    public Optional<UserProfile> findProfileByUserId(Long userId) {
        return profileJpaRepository.findByUserId(userId)
                .map(userMapper::toProfileDomain);
    }

    @Override
    public Optional<UserLoginCredential> findLoginCredentialByUserId(Long userId) {
        return credentialJpaRepository.findFirstByUserIdOrderByIdDesc(userId)
                .map(authMapper::toCredentialDomain);
    }
}
