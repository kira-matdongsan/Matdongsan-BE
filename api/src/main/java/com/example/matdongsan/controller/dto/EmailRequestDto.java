package com.example.matdongsan.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailRequestDto {
    private String to;
    private String subject;
    private String content;
}
