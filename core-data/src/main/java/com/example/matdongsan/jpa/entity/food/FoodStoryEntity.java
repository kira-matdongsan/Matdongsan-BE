package com.example.matdongsan.jpa.entity.food;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import com.example.matdongsan.food.enums.FoodStoryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    private Long userId;

    @OneToMany(mappedBy = "foodStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FoodStoryImageEntity> images = new ArrayList<>();

    public abstract FoodStoryType getType();

    public void addImage(FoodStoryImageEntity image) {
        images.add(image);
        image.setFoodStory(this);
    }
}
