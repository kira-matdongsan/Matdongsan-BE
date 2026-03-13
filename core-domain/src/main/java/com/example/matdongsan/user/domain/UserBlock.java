package com.example.matdongsan.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserBlock {

    private Long id;
    private Long blockerId;
    private Long blockedId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserBlock create(Long blockerId, Long blockedId) {
        return UserBlock.builder()
                .blockerId(blockerId)
                .blockedId(blockedId)
                .build();
    }
}
