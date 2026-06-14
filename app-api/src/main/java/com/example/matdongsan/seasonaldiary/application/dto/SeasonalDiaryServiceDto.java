package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SeasonalDiaryServiceDto {
    private Long id;
    private Long stickerId;
    private String stickerImageUrl;
    private String content;
    private LocalDateTime createdAt;

    public static SeasonalDiaryServiceDto from(SeasonalDiary d, String stickerImageUrl) {
        return SeasonalDiaryServiceDto.builder()
                .id(d.getId())
                .stickerId(d.getStickerId())
                .stickerImageUrl(stickerImageUrl)
                .content(d.getContent())
                .createdAt(d.getCreatedAt())
                .build();
    }
}
