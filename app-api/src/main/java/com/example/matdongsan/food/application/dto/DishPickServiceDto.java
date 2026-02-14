package com.example.matdongsan.food.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class DishPickServiceDto {

    private final Integer totalVoteCount;
    private final LocalDate voteStartDate;
    private final LocalDate voteEndDate;
    private final List<DishServiceDto> contents;
}
