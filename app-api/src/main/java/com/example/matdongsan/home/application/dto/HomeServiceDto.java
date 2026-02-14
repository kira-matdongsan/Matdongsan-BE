package com.example.matdongsan.home.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HomeServiceDto {

    private final String weekText;
    private final Long foodId;
    private final String name;
    private final String subtitle;
    private final String thumbnail;
}
