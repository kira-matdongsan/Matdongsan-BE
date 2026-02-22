package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long> {

    Optional<UserProfileEntity> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE UserProfileEntity p SET p.nickname = :nickname WHERE p.user.id = :userId")
    void updateNicknameByUserId(@Param("userId") Long userId, @Param("nickname") String nickname);
}
