package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.entity.user.UserEntity;
import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCommandRepositoryImpl implements UserCommandRepository {

    private final UserJpaRepository userJpaRepository;
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
    public void softDeleteById(Long userId) {
        userJpaRepository.softDeleteById(userId);
    }
}
