package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.DishServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "제철 요리 등록 및 투표 요청 DTO")
@Builder
@Getter
public class DishRequestDto {

    @NotBlank(message = "제철 요리 이름을 입력해주세요.")
    private String name;

    private List<String> imageUrls;

    public DishServiceDto toServiceDto() {
        return DishServiceDto.builder()
                .name(name)
                .imageUrls(imageUrls)
                .build();
    }
}
