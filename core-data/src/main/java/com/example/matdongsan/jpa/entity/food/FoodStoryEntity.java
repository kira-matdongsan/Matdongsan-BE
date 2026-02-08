package com.example.matdongsan.jpa.entity.food;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import com.example.matdongsan.food.enums.FoodStoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Entity
@Table(name = "food_story")
public abstract class FoodStoryEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodEntity food;

    private Long userId;

    private Integer likeCount = 0;

    private Integer reportCount = 0;

    public abstract FoodStoryType getType();
}
