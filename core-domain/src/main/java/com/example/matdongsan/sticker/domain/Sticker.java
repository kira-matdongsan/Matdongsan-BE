package com.example.matdongsan.sticker.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Sticker {

    private Long id;
    private String imageUrl;
    private String name;
    private Integer displayOrder;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}
