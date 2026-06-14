package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CreateSeasonalDiaryParam {
    private LocalDate recordDate;
    private SeasonalDiarySticker sticker;
    private String content;
}
