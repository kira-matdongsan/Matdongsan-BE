package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.repository.UserBlockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserBlockQueryRepositoryImpl implements UserBlockQueryRepository {

    private final UserBlockJpaRepository userBlockJpaRepository;

    @Override
    public boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId) {
        return userBlockJpaRepository.existsByBlockerIdAndBlockedIdAndDeletedAtIsNull(blockerId, blockedId);
    }

    @Override
    public List<Long> findBlockedUserIdsByBlockerId(Long blockerId) {
        return userBlockJpaRepository.findBlockedIdsByBlockerId(blockerId);
    }
}
