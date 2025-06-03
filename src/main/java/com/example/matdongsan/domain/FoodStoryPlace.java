package com.example.matdongsan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DiscriminatorValue("PLACE")
@Entity
public class FoodStoryPlace extends FoodStory {

    private String name;
    private String category;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String naverUrl;
}
