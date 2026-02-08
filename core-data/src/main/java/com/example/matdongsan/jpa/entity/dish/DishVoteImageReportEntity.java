package com.example.matdongsan.jpa.entity.dish;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
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
@Entity
@Table(name = "dish_vote_image_report")
public class DishVoteImageReportEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_image_id", nullable = false)
    private DishVoteImageEntity voteImage;

    private Long userId;
}
