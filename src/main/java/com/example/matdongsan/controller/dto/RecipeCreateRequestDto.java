package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.RecipeServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "제철기록 작성 요청 DTO")
@Builder
@Getter
public class RecipeCreateRequestDto {

    @NotBlank(message = "레시피명을 입력해주세요.")
    private String name;

    @NotBlank(message = "재료를 입력해주세요.")
    private String ingredients;

    @NotBlank(message = "조리방법을 입력해주세요.")
    private String instructions;

    private List<String> imageUrls;

    public RecipeServiceDto toServiceDto() {
        return RecipeServiceDto.builder()
                .name(name)
                .ingredients(ingredients)
                .instructions(instructions)
                .imageUrls(imageUrls)
                .build();
    }

}
