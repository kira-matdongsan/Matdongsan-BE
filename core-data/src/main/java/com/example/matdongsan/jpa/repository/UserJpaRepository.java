package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
