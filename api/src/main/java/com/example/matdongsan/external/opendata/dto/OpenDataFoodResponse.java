package com.example.matdongsan.external.opendata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenDataFoodResponse {
    @JsonProperty("Grid_20171128000000000572_1")
    private OpenDataFoodData data;
}
