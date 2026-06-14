package com.example.matdongsan.seasonaldiary.presentation.response;

import com.example.matdongsan.seasonaldiary.application.dto.WeeklyDiaryServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WeeklyDiaryResponse {

    private String monthLabel;
    private LocalDate baseDate;
    private List<DayResponse> days;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DayResponse {
        private LocalDate date;
        private String dayOfWeek;
        private boolean hasRecord;
    }

    public static WeeklyDiaryResponse from(WeeklyDiaryServiceDto dto) {
        return WeeklyDiaryResponse.builder()
                .monthLabel(dto.getMonthLabel())
                .baseDate(dto.getBaseDate())
                .days(dto.getDays().stream().map(d -> DayResponse.builder()
                        .date(d.getDate())
                        .dayOfWeek(d.getDayOfWeek())
                        .hasRecord(d.isHasRecord())
                        .build()).toList())
                .build();
    }
}
