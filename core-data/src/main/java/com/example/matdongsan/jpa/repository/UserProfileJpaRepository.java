package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long> {
}
