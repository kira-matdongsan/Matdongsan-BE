package com.example.matdongsan.jpa.entity.dish;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import com.example.matdongsan.jpa.entity.food.FeaturedFoodEntity;
import com.example.matdongsan.jpa.entity.food.FoodEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "dish")
public class DishEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_food_id", nullable = false)
    private FeaturedFoodEntity featuredFood;

    private String name;

    private Integer voteCount = 0;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<DishVoteImageEntity> images = new ArrayList<>();

    public void plusVoteCount() {
        voteCount++;
    }
}
