package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateSeasonalDiaryParam {
    private SeasonalDiarySticker sticker;
    private String content;
}
