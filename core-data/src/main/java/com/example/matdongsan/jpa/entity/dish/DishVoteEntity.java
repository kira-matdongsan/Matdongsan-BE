package com.example.matdongsan.jpa.entity.dish;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "dish_vote")
public class DishVoteEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    private Long userId;

    @OneToMany(mappedBy = "dishVote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DishVoteImageEntity> images = new ArrayList<>();

    public void addImage(DishVoteImageEntity image) {
        images.add(image);
        image.setDishVote(this);
    }
}
