package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.entity.user.UserBlockEntity;
import com.example.matdongsan.jpa.repository.UserBlockJpaRepository;
import com.example.matdongsan.user.domain.UserBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserBlockCommandRepositoryImpl implements UserBlockCommandRepository {

    private final UserBlockJpaRepository userBlockJpaRepository;

    @Override
    public UserBlock save(UserBlock userBlock) {
        UserBlockEntity entity = UserBlockEntity.builder()
                .blockerId(userBlock.getBlockerId())
                .blockedId(userBlock.getBlockedId())
                .build();
        UserBlockEntity saved = userBlockJpaRepository.save(entity);
        return UserBlock.builder()
                .id(saved.getId())
                .blockerId(saved.getBlockerId())
                .blockedId(saved.getBlockedId())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .deletedAt(saved.getDeletedAt())
                .build();
    }
}
