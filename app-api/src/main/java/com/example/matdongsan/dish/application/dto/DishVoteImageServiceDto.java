package com.example.matdongsan.dish.application.dto;

import com.example.matdongsan.dish.domain.DishVoteImage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DishVoteImageServiceDto {

    private final Long id;
    private final String imageUrl;

    public static DishVoteImageServiceDto from(DishVoteImage dishVoteImage) {
        return DishVoteImageServiceDto.builder()
                .id(dishVoteImage.getId())
                .imageUrl(dishVoteImage.getImageUrl())
                .build();
    }
}
