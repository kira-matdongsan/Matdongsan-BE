package com.example.matdongsan.seasonaldiary.domain;

import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
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
    private SeasonalDiarySticker sticker;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static SeasonalDiary create(Long userId, LocalDate recordDate,
                                       SeasonalDiarySticker sticker, String content) {
        return SeasonalDiary.builder()
                .userId(userId)
                .recordDate(recordDate)
                .sticker(sticker)
                .content(content)
                .build();
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}
