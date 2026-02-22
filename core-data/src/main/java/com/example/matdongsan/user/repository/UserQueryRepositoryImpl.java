package com.example.matdongsan.user.repository;

import com.example.matdongsan.jpa.repository.UserJpaRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toUserDomain);
    }
}
