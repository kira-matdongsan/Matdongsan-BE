package com.example.matdongsan.user.repository;

import java.util.List;

public interface UserBlockQueryRepository {

    boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    List<Long> findBlockedUserIdsByBlockerId(Long blockerId);
}
