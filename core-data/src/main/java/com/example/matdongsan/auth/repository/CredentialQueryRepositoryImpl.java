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
        return credentialJpaRepository.findByLoginTypeAndEmailAndDeletedAtIsNull(loginType, email)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public Optional<UserLoginCredential> findByLoginTypeAndOauthId(LoginType loginType, String oauthId) {
        return credentialJpaRepository.findByLoginTypeAndOauthIdAndDeletedAtIsNull(loginType, oauthId)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public Optional<UserLoginCredential> findByUserId(Long userId) {
        return credentialJpaRepository.findFirstByUserIdAndDeletedAtIsNullOrderByIdDesc(userId)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return credentialJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public boolean existsByLoginTypeAndEmail(LoginType loginType, String email) {
        return credentialJpaRepository.existsByLoginTypeAndEmailAndDeletedAtIsNull(loginType, email);
    }

    @Override
    public boolean existsByLoginTypeAndOauthId(LoginType loginType, String oauthId) {
        return credentialJpaRepository.existsByLoginTypeAndOauthIdAndDeletedAtIsNull(loginType, oauthId);
    }
}
