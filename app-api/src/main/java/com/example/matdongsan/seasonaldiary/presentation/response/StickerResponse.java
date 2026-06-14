package com.example.matdongsan.seasonaldiary.presentation.response;

import com.example.matdongsan.seasonaldiary.application.dto.StickerServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StickerResponse {

    private Long stickerId;
    private String imageUrl;
    private Integer displayOrder;

    public static StickerResponse from(StickerServiceDto dto) {
        return StickerResponse.builder()
                .stickerId(dto.getStickerId())
                .imageUrl(dto.getImageUrl())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }
}
