package com.example.matdongsan.common.util.controller;

import com.example.matdongsan.common.CommonResponse;
import com.example.matdongsan.common.ListResponse;
import com.example.matdongsan.common.ResponseCode;
import com.example.matdongsan.common.ResultResponse;
import com.example.matdongsan.common.util.controller.dto.PresignedUrlListRequestDto;
import com.example.matdongsan.common.util.controller.dto.PresignedUrlResponseDto;
import com.example.matdongsan.common.util.domain.FileType;
import com.example.matdongsan.common.util.service.S3PresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "파일 업로드 API", description = "파일 업로드 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class S3PresignedUrlController {

    private final S3PresignedUrlService presignedUrlService;

    @Operation(summary = "단일 Presigned URL 발급", description = "파일 이름과 파일 타입을 받아 Presigned URL을 발급")
    @GetMapping("/presigned-url")
    public ResponseEntity<CommonResponse<ResultResponse<PresignedUrlResponseDto>>> generatePresignedUrl(
            @Parameter(description = "파일 타입", example = "FOOD_STORY")
            @RequestParam FileType fileType,
            @Parameter(description = "업로드할 파일 이름", example = "image1.jpg")
            @RequestParam String fileName) {
        PresignedUrlResponseDto result = presignedUrlService.getPresignedUrl(fileType, fileName);
        return CommonResponse.successResult(ResponseCode.OK, result);
    }

    @Operation(summary = "다중 Presigned URL 발급", description = "여러 개의 파일 이름을 받아 각각 Presigned URL을 발급")
    @PostMapping("/presigned-urls")
    public ResponseEntity<CommonResponse<ListResponse<PresignedUrlResponseDto>>> generatePresignedUrls(
            @RequestBody PresignedUrlListRequestDto request) {
        List<PresignedUrlResponseDto> result = presignedUrlService.getPresignedUrls(
                request.getFileType(), request.getFileNames());
        return CommonResponse.successList(ResponseCode.OK, result);
    }
}
