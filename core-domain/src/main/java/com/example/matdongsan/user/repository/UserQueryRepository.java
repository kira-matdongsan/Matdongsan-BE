package com.example.matdongsan.user.repository;

import com.example.matdongsan.user.domain.User;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<User> findById(Long id);
}
