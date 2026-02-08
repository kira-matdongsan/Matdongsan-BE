package com.example.matdongsan.food.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreatePlaceParam {

    private final String name;
    private final String content;
    private final String category;
    private final String address;
    private final String naverUrl;
    private final List<String> imageUrls;
}
