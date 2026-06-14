package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.sticker.domain.Sticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StickerServiceDto {
    private Long stickerId;
    private String imageUrl;
    private Integer displayOrder;

    public static StickerServiceDto from(Sticker s) {
        return StickerServiceDto.builder()
                .stickerId(s.getId())
                .imageUrl(s.getImageUrl())
                .displayOrder(s.getDisplayOrder())
                .build();
    }
}
