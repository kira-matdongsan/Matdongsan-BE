package com.example.matdongsan.external.opendata;

import com.example.matdongsan.domain.ExternalFoodSource;
import com.example.matdongsan.external.opendata.dto.OpenDataFoodItem;
import org.springframework.stereotype.Component;

@Component
public class OpenDataFoodMapper {

    public ExternalFoodSource toEntity(OpenDataFoodItem item) {
        return ExternalFoodSource.builder()
                .externalId(item.getExternalId())
                .name(item.getName())
                .monthLabel(item.getMonthLabel())
                .seasonMonths(item.getSeasonMonths())
                .category(item.getCategory())
                .regions(item.getRegions())
                .harvestPeriod(item.getHarvestPeriod())
                .varieties(item.getVarieties())
                .benefits(item.getBenefits())
                .buyingTips(item.getBuyingTips())
                .cookingTips(item.getCookingTips())
                .preparationTips(item.getPreparationTips())
                .detailUrl(item.getDetailUrl())
                .imageUrl(item.getImageUrl())
                .publishedAt(item.getPublishedAt())
                .build();
    }
}
