package com.example.matdongsan.jpa.entity.food;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "featured_food")
public class FeaturedFoodEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_id")
    private Long foodId;

    private Integer year;
    private Integer week;

    @Column(nullable = false)
    private Boolean active = false;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
