package com.example.matdongsan.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "리스트 성공 응답")
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ListResponse<T> {

    @Schema(description = "응답에 포함된 항목 목록")
    private final List<T> contents;
}
