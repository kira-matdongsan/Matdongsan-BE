package com.example.matdongsan.seasonaldiary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WeeklyDiaryServiceDto {
    private String monthLabel;
    private LocalDate baseDate;
    private List<Day> days;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Day {
        private LocalDate date;
        private String dayOfWeek;
        private boolean hasRecord;
    }
}
