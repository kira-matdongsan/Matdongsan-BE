package com.example.matdongsan.jpa.entity.seasonaldiary;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "seasonal_diary")
public class SeasonalDiaryEntity extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "sticker_id", nullable = false)
    private Long stickerId;

    @Column(name = "content", nullable = false, length = 40)
    private String content;
}
