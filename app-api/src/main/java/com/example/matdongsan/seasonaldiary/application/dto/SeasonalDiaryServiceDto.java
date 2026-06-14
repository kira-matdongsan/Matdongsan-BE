package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SeasonalDiaryServiceDto {
    private Long id;
    private SeasonalDiarySticker sticker;
    private String content;
    private LocalDateTime createdAt;

    public static SeasonalDiaryServiceDto from(SeasonalDiary d) {
        return SeasonalDiaryServiceDto.builder()
                .id(d.getId())
                .sticker(d.getSticker())
                .content(d.getContent())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
