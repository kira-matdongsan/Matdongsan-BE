package com.example.matdongsan.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Schema(description = "성공 응답")
@Getter
@Builder
public class RestApiResponse<T> {

    @Schema(description = "성공 여부")
    @Builder.Default
    private boolean success = true;

    @Schema(description = "응답 코드")
    @Builder.Default
    private int code = 200;

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> ResponseEntity<RestApiResponse<T>> success(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(
                        RestApiResponse.<T>builder()
                                .code(responseCode.getCode())
                                .message(responseCode.getMessage())
                                .build()
                );
    }

    public static <T> ResponseEntity<RestApiResponse<T>> success(ResponseCode responseCode, T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(
                        RestApiResponse.<T>builder()
                                .code(responseCode.getCode())
                                .message(responseCode.getMessage())
                                .data(data)
                                .build()
                );
    }

    public static <T> ResponseEntity<RestApiResponse<ResultResponse<T>>> successResult(ResponseCode responseCode, T data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(
                        RestApiResponse.<ResultResponse<T>>builder()
                                .code(responseCode.getCode())
                                .message(responseCode.getMessage())
                                .data(new ResultResponse<>(data))
                                .build()
                );
    }

    public static <T> ResponseEntity<RestApiResponse<ListResponse<T>>> successList(ResponseCode responseCode, List<T> data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(
                        RestApiResponse.<ListResponse<T>>builder()
                                .code(responseCode.getCode())
                                .message(responseCode.getMessage())
                                .data(new ListResponse<>(data))
                                .build()
                );
    }

    public static <T> ResponseEntity<RestApiResponse<PageResponse<T>>> successPage(ResponseCode responseCode, Page<T> data) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(
                        RestApiResponse.<PageResponse<T>>builder()
                                .code(responseCode.getCode())
                                .message(responseCode.getMessage())
                                .data(PageResponse.of(data))
                                .build()
                );
    }

}
