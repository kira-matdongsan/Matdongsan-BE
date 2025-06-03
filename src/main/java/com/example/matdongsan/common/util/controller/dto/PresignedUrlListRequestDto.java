package com.example.matdongsan.common.util.controller.dto;

import com.example.matdongsan.common.util.domain.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Schema(description = "여러 파일에 대한 Presigned URL 요청 DTO")
@Getter
public class PresignedUrlListRequestDto {

    @Schema(description = "파일 타입 (예: FOOD_STORY, DISH, PROFILE_IMAGE)", example = "FOOD_STORY")
    @NotNull
    private FileType fileType;

    @Schema(description = "업로드할 파일 이름 리스트", example = "[\"image1.jpg\", \"image2.png\"]")
    @NotEmpty
    private List<@NotBlank String> fileNames;
}
