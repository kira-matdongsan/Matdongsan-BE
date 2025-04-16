package com.example.matdongsan.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Schema(description = "서버 내부 오류 응답")
@Getter
@Builder
public class CommonResponseError<T> {

    @Schema(description = "성공 여부", example = "false")
    @Builder.Default
    private boolean success = false;

    @Schema(description = "에러 코드", example = "500")
    @Builder.Default
    private int code = 500;

    @Schema(description = "에러 메시지", example = "예상치 못한 오류가 발생했습니다.")
    private String message;

    @Schema(description = "에러 상세 정보", nullable = true)
    private T errors;

    public static <T> ResponseEntity<CommonResponseError<T>> error(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(CommonResponseError.<T>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
