package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.PlaceServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "제철기록 작성 요청 DTO")
@Builder
@Getter
public class PlaceCreateRequestDto {

    @NotBlank(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "한줄평을 입력해주세요.")
    private String content;

    @NotBlank(message = "가게 카테고리를 입력해주세요.")
    private String category;

    @NotBlank(message = "가게 주소를 입력해주세요.")
    private String address;

    private String naverUrl;

    private List<String> imageUrls;

    public PlaceServiceDto toServiceDto() {
        return PlaceServiceDto.builder()
                .name(name)
                .content(content)
                .category(category)
                .address(address)
                .naverUrl(naverUrl)
                .imageUrls(imageUrls)
                .build();
    }

}
