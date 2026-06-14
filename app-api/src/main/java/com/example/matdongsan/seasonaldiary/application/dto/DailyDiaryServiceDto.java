package com.example.matdongsan.seasonaldiary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DailyDiaryServiceDto {
    private LocalDate date;
    private List<SeasonalDiaryServiceDto> records;
    private List<DiaryFoodStoryServiceDto> foodStories;
}
