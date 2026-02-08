package com.example.matdongsan.food.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class CreateSeasonalNoteParam {

    private final String content;
    private final LocalDate recordedDate;
    private final List<String> imageUrls;
}
