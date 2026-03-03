package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.entity.user.UserAgreementEntity;
import com.example.matdongsan.jpa.entity.user.UserEntity;
import com.example.matdongsan.jpa.entity.user.UserProfileEntity;
import com.example.matdongsan.jpa.repository.UserAgreementJpaRepository;
import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.jpa.repository.UserProfileJpaRepository;
import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserProfileCommandRepositoryImpl implements UserProfileCommandRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProfileJpaRepository profileJpaRepository;
    private final UserAgreementJpaRepository agreementJpaRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfile save(UserProfile profile) {
        UserEntity userRef = userJpaRepository.getReferenceById(profile.getUserId());

        UserProfileEntity entity = UserProfileEntity.builder()
                .nickname(profile.getNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .user(userRef)
                .build();

        UserProfileEntity saved = profileJpaRepository.save(entity);
        return userMapper.toProfileDomain(saved);
    }

    @Override
    public List<UserAgreement> saveAllAgreements(List<UserAgreement> agreements) {
        List<UserAgreementEntity> entities = agreements.stream()
                .map(agreement -> UserAgreementEntity.builder()
                        .userId(agreement.getUserId())
                        .termId(agreement.getTermsId())
                        .agreedAt(agreement.getAgreedAt())
                        .build())
                .toList();

        return agreementJpaRepository.saveAll(entities)
                .stream()
                .map(userMapper::toAgreementDomain)
                .toList();
    }

    @Override
    public void updateNickname(Long userId, String nickname) {
        profileJpaRepository.updateNicknameByUserId(userId, nickname);
    }

    @Override
    public void softDeleteByUserId(Long userId) {
        profileJpaRepository.softDeleteByUserId(userId);
    }
}
