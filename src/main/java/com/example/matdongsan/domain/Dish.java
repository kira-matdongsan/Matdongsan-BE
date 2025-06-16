package com.example.matdongsan.domain;

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
public class Dish extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_food_id", nullable = false)
    private FeaturedFood featuredFood;

    private String name;

    private Integer voteCount = 0;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<DishVoteImage> images = new ArrayList<>();

    public void plusVoteCount() {
        voteCount++;
    }
}
