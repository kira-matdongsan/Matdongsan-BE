package com.example.matdongsan.user.repository;

import com.example.matdongsan.user.domain.User;

public interface UserCommandRepository {

    User save(User user);

    void softDeleteById(Long userId);
}
