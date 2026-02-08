package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.entity.auth.TermsEntity;
import com.example.matdongsan.jpa.entity.user.UserAgreementEntity;
import com.example.matdongsan.jpa.entity.user.UserEntity;
import com.example.matdongsan.jpa.entity.user.UserProfileEntity;
import com.example.matdongsan.jpa.repository.TermsJpaRepository;
import com.example.matdongsan.jpa.repository.UserAgreementJpaRepository;
import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.jpa.repository.UserProfileJpaRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserCommandRepositoryImpl implements UserCommandRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserProfileJpaRepository profileJpaRepository;
    private final UserAgreementJpaRepository agreementJpaRepository;
    private final TermsJpaRepository termsJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .isBlocked(user.isBlocked())
                .lastLoggedInAt(user.getLastLoggedInAt())
                .build();

        UserEntity saved = userJpaRepository.save(entity);
        return userMapper.toUserDomain(saved);
    }

    @Override
    public UserProfile saveProfile(UserProfile profile) {
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
                .map(agreement -> {
                    UserEntity userRef = userJpaRepository.getReferenceById(agreement.getUserId());
                    TermsEntity termsRef = termsJpaRepository.getReferenceById(agreement.getTermsId());

                    return UserAgreementEntity.builder()
                            .user(userRef)
                            .term(termsRef)
                            .agreedAt(agreement.getAgreedAt())
                            .build();
                })
                .toList();

        return agreementJpaRepository.saveAll(entities)
                .stream()
                .map(userMapper::toAgreementDomain)
                .toList();
    }
}
