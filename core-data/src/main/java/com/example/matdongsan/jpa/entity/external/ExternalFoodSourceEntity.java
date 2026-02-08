package com.example.matdongsan.jpa.entity.external;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "external_food_source")
public class ExternalFoodSourceEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long externalId;
    private String name;
    private String monthLabel;
    private String seasonMonths;
    private String category;
    private String regions;
    private String harvestPeriod;
    private String varieties;
    private String benefits;
    private String buyingTips;
    private String cookingTips;
    private String preparationTips;
    private String detailUrl;
    private String imageUrl;
    private String publishedAt;

}
