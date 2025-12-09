package com.example.matdongsan.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("PLACE")
@Entity
public class FoodStoryPlace extends FoodStory {

    private String placeName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String category;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String naverUrl;

    @Override
    public FoodStoryType getType() {
        return FoodStoryType.PLACE;
    }

}
