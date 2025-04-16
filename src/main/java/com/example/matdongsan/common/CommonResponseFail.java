package com.example.matdongsan.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Schema(description = "비즈니스/유효성 오류 응답")
@Getter
@Builder
public class CommonResponseFail<T> {

    @Schema(description = "성공 여부", example = "false")
    @Builder.Default
    private boolean success = false;

    @Schema(description = "에러 코드", example = "400")
    @Builder.Default
    private int code = 400;

    @Schema(description = "에러 메시지", example = "잘못된 입력입니다.")
    private String message;

    @Schema(description = "필드 에러 정보", example = "{\"title\": \"제목은 필수입니다.\"}")
    private T errors;

    public static <T> ResponseEntity<CommonResponseFail<T>> fail(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(CommonResponseFail.<T>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static <T> ResponseEntity<CommonResponseFail<T>> fail(ErrorCode errorCode, T errors) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(CommonResponseFail.<T>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .errors(errors)
                        .build());
    }
}
