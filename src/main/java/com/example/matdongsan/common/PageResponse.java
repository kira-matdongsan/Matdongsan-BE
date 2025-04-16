package com.example.matdongsan.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "페이지네이션 성공 응답")
@Getter
@Builder
public class PageResponse<T> {

    @Schema(description = "페이지의 내용")
    private final List<T> content;

    @Schema(description = "현재 페이지 번호")
    private final int currentPage;

    @Schema(description = "총 페이지 수")
    private final int totalPages;

    @Schema(description = "총 요소 수")
    private final long totalElements;

    @Schema(description = "다음 페이지 존재 여부")
    private final boolean hasNext;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .build();
    }
}
