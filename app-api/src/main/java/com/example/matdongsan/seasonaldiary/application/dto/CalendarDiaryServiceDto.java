package com.example.matdongsan.seasonaldiary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalendarDiaryServiceDto {
    private int year;
    private int month;
    private List<Marker> days;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Marker {
        private LocalDate date;
        private Long stickerId;
        private String stickerImageUrl;
    }
}
