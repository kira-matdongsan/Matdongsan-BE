package com.example.matdongsan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("SEASONAL_NOTE")
@Entity
public class FoodStorySeasonalNote extends FoodStory {

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate recordedDate;

    @Override
    public FoodStoryType getType() {
        return FoodStoryType.SEASONAL_NOTE;
    }
}
