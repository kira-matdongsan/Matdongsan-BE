package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DishVoteImageReport {

    private Long id;
    private Long voteImageId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static DishVoteImageReport create(Long voteImageId, Long userId) {
        return DishVoteImageReport.builder()
                .voteImageId(voteImageId)
                .userId(userId)
                .build();
    }
}
