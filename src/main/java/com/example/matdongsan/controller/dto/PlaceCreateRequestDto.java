package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.PlaceServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철기록 작성 요청 DTO")
@Builder
@Getter
public class PlaceCreateRequestDto {

    @NotBlank(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "가게 카테고리를 입력해주세요.")
    private String category;

    @NotBlank(message = "가게 주소를 입력해주세요.")
    private String address;

    private String naverUrl;

    public PlaceServiceDto toServiceDto() {
        return PlaceServiceDto.builder()
                .name(name)
                .category(category)
                .address(address)
                .naverUrl(naverUrl)
                .build();
    }

}
