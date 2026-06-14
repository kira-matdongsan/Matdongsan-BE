package com.example.matdongsan.seasonaldiary.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateSeasonalDiaryParam {
    private Long stickerId;
    private String content;
}
