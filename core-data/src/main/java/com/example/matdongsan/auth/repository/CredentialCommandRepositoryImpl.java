package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.mapper.AuthMapper;
import com.example.matdongsan.jpa.entity.auth.UserLoginCredentialEntity;
import com.example.matdongsan.jpa.entity.user.UserEntity;
import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.jpa.repository.UserLoginCredentialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CredentialCommandRepositoryImpl implements CredentialCommandRepository {

    private final UserLoginCredentialJpaRepository credentialJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AuthMapper authMapper;

    @Override
    public UserLoginCredential save(UserLoginCredential credential) {
        UserEntity userRef = userJpaRepository.getReferenceById(credential.getUserId());

        UserLoginCredentialEntity entity = UserLoginCredentialEntity.builder()
                .loginType(credential.getLoginType())
                .email(credential.getEmail())
                .password(credential.getPassword())
                .oauthId(credential.getOauthId())
                .user(userRef)
                .build();

        UserLoginCredentialEntity saved = credentialJpaRepository.save(entity);
        return authMapper.toCredentialDomain(saved);
    }
}
