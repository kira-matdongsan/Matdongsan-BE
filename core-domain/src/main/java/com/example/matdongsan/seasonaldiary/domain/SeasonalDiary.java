package com.example.matdongsan.seasonaldiary.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SeasonalDiary {

    private Long id;
    private Long userId;
    private LocalDate recordDate;
    private Long stickerId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static SeasonalDiary create(Long userId, LocalDate recordDate,
                                       Long stickerId, String content) {
        return SeasonalDiary.builder()
                .userId(userId)
                .recordDate(recordDate)
                .stickerId(stickerId)
                .content(content)
                .build();
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}
