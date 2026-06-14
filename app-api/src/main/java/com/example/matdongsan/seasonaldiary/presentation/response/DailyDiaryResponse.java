package com.example.matdongsan.seasonaldiary.presentation.response;

import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.seasonaldiary.application.dto.DailyDiaryServiceDto;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DailyDiaryResponse {

    private LocalDate date;
    private List<RecordResponse> records;
    private List<FoodStoryResponse> foodStories;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RecordResponse {
        private Long id;
        private SeasonalDiarySticker sticker;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FoodStoryResponse {
        private Long storyId;
        private Long foodId;
        private String foodName;
        private FoodStoryType type;
        private String message;
    }

    public static DailyDiaryResponse from(DailyDiaryServiceDto dto) {
        return DailyDiaryResponse.builder()
                .date(dto.getDate())
                .records(dto.getRecords().stream().map(r -> RecordResponse.builder()
                        .id(r.getId())
                        .sticker(r.getSticker())
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .build()).toList())
                .foodStories(dto.getFoodStories().stream().map(s -> FoodStoryResponse.builder()
                        .storyId(s.getStoryId())
                        .foodId(s.getFoodId())
                        .foodName(s.getFoodName())
                        .type(s.getType())
                        .message(s.getMessage())
                        .build()).toList())
                .build();
    }
}
