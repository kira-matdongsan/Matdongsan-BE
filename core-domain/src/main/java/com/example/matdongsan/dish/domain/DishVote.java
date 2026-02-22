package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Builder
public class DishVote {

    private Long id;
    private Long dishId;
    private Long userId;
    private List<DishVoteImage> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static DishVote create(Long dishId, Long userId, List<String> imageUrls) {
        LocalDateTime now = LocalDateTime.now();
        DishVote vote = DishVote.builder()
                .dishId(dishId)
                .userId(userId)
                .createdAt(now)
                .build();

        List<DishVoteImage> images = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> DishVoteImage.create(imageUrls.get(i), i + 1))
                .toList();
        vote.images = images;

        return vote;
    }
}
