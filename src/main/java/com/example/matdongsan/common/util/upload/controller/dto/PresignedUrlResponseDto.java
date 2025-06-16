package com.example.matdongsan.common.util.upload.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Presigned URL 응답 DTO")
@Builder
@Getter
public class PresignedUrlResponseDto {

    @Schema(description = "요청한 원본 파일 이름", example = "image1.jpg")
    private String fileName;

    @Schema(description = "클라이언트가 사용할 S3 Presigned 업로드 URL")
    private String presignedUrl;

    @Schema(description = "업로드된 파일에 접근 가능한 공개 URL")
    private String accessUrl;
}
