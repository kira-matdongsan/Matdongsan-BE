package com.example.matdongsan.dish.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateDishParam {

    private final String name;
    private final List<String> imageUrls;
}
