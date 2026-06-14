package com.example.matdongsan.seasonaldiary.presentation.response;

import com.example.matdongsan.seasonaldiary.application.dto.CalendarDiaryServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalendarDiaryResponse {

    private int year;
    private int month;
    private List<MarkerResponse> days;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MarkerResponse {
        private LocalDate date;
        private Long stickerId;
        private String stickerImageUrl;
    }

    public static CalendarDiaryResponse from(CalendarDiaryServiceDto dto) {
        return CalendarDiaryResponse.builder()
                .year(dto.getYear())
                .month(dto.getMonth())
                .days(dto.getDays().stream().map(m -> MarkerResponse.builder()
                        .date(m.getDate())
                        .stickerId(m.getStickerId())
                        .stickerImageUrl(m.getStickerImageUrl())
                        .build()).toList())
                .build();
    }
}
