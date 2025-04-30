package com.example.matdongsan.external.opendata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenDataFoodItem {
    @JsonProperty("IDNTFC_NO")
    private Long externalId;

    @JsonProperty("PRDLST_NM")
    private String name;

    @JsonProperty("M_DISTCTNS")
    private String monthLabel;

    @JsonProperty("M_DISTCTNS_ITM")
    private String seasonMonths;

    @JsonProperty("PRDLST_CL")
    private String category;

    @JsonProperty("MTC_NM")
    private String regions;

    @JsonProperty("PRDCTN__ERA")
    private String harvestPeriod;

    @JsonProperty("MAIN_SPCIES_NM")
    private String varieties;

    @JsonProperty("EFFECT")
    private String benefits;

    @JsonProperty("PURCHASE_MTH")
    private String buyingTips;

    @JsonProperty("COOK_MTH")
    private String cookingTips;

    @JsonProperty("TRT_MTH")
    private String preparationTips;

    @JsonProperty("URL")
    private String detailUrl;

    @JsonProperty("IMG_URL")
    private String imageUrl;

    @JsonProperty("REGIST_DE")
    private String publishedAt;
}
