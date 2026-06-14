package com.example.matdongsan.seasonaldiary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CreateSeasonalDiaryParam {
    private LocalDate recordDate;
    private Long stickerId;
    private String content;
}
