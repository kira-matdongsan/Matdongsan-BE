package com.example.matdongsan.external.opendata.converter;

import com.example.matdongsan.jpa.entity.external.ExternalFoodSourceEntity;
import com.example.matdongsan.food.domain.Food;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExternalFoodSourceToFoodConverter {

    public Food convert(ExternalFoodSourceEntity source) {
        return Food.builder()
                .name(source.getName())
                .englishName(null)
                .imageUrl(source.getImageUrl())
                .subtitle(null)
                .description(null)
                .seasonMonths(parseSeasonMonths(source.getSeasonMonths()))
                .regions(source.getRegions())
                .benefits(cleanText(source.getBenefits()))
                .buyingTips(cleanText(source.getBuyingTips()))
                .preparationTips(cleanText(source.getPreparationTips()))
                .nutrients(null)
                .build();
    }

    private List<Integer> parseSeasonMonths(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(s -> s.replaceAll("^0", ""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String cleanText(String raw) {
        if (raw == null) return null;
        return raw.replaceAll("[\\r\\n]+", " ").trim();
    }
}
