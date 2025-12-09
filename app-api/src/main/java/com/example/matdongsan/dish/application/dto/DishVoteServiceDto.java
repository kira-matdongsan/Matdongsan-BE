package com.example.matdongsan.dish.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DishVoteServiceDto{

    private final List<String> imageUrls;

}
