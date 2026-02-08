package com.example.matdongsan.auth.domain;

import com.example.matdongsan.auth.enums.TermsType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Terms {

    private Long id;
    private TermsType type;
    private Integer version;
    private String title;
    private String content;
    private Boolean required;
    private Boolean active;
    private Integer orderNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
