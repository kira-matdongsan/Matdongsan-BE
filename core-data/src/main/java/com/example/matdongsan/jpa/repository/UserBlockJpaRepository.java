package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.user.UserBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserBlockJpaRepository extends JpaRepository<UserBlockEntity, Long> {

    boolean existsByBlockerIdAndBlockedIdAndDeletedAtIsNull(Long blockerId, Long blockedId);

    @Query("SELECT ub.blockedId FROM UserBlockEntity ub WHERE ub.blockerId = :blockerId AND ub.deletedAt IS NULL")
    List<Long> findBlockedIdsByBlockerId(@Param("blockerId") Long blockerId);
}
