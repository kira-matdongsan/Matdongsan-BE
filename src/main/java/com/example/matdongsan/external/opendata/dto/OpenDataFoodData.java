package com.example.matdongsan.external.opendata.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenDataFoodData {
    private int totalCnt;
    private int startRow;
    private int endRow;
    private List<OpenDataFoodItem> row;
}
