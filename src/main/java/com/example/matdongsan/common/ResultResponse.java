package com.example.matdongsan.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "단일 값 래핑 성공 응답")
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ResultResponse<T> {

    @Schema(description = "응답으로 반환되는 단일 값")
    private final T result;
}
