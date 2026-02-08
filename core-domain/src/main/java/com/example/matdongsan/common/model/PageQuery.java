package com.example.matdongsan.common.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageQuery {

    private final int page;
    private final int size;
    private final String sortBy;
    private final boolean ascending;

    public static PageQuery of(int page, int size) {
        return PageQuery.builder()
                .page(page)
                .size(size)
                .build();
    }

    public static PageQuery of(int page, int size, String sortBy, boolean ascending) {
        return PageQuery.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .ascending(ascending)
                .build();
    }
}
