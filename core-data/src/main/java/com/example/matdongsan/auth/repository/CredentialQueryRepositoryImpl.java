package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.auth.mapper.AuthMapper;
import com.example.matdongsan.jpa.repository.UserLoginCredentialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CredentialQueryRepositoryImpl implements CredentialQueryRepository {

    private final UserLoginCredentialJpaRepository credentialJpaRepository;
    private final AuthMapper authMapper;

    @Override
    public Optional<UserLoginCredential> findByLoginTypeAndEmail(LoginType loginType, String email) {
        return credentialJpaRepository.findByLoginTypeAndEmail(loginType, email)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public Optional<UserLoginCredential> findByLoginTypeAndOauthId(LoginType loginType, String oauthId) {
        return credentialJpaRepository.findByLoginTypeAndOauthId(loginType, oauthId)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public Optional<UserLoginCredential> findByUserId(Long userId) {
        return credentialJpaRepository.findFirstByUserIdOrderByIdDesc(userId)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return credentialJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByLoginTypeAndEmail(LoginType loginType, String email) {
        return credentialJpaRepository.existsByLoginTypeAndEmail(loginType, email);
    }

    @Override
    public boolean existsByLoginTypeAndOauthId(LoginType loginType, String oauthId) {
        return credentialJpaRepository.existsByLoginTypeAndOauthId(loginType, oauthId);
    }
}
