package com.example.matdongsan.jpa.entity.food;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntity;
import com.example.matdongsan.jpa.converter.JsonMapConverter;
import com.example.matdongsan.jpa.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String englishName;

    private Boolean isFeatured = false;

    private LocalDateTime lastFeaturedAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    private String color;

    private String subtitle;

    private String description;

    private Integer likeCount = 0;

    @Convert(converter = JsonListConverter.class)
    @Column(columnDefinition = "json")
    private List<Integer> seasonMonths;

    private String regions;

    private String benefits;

    private String buyingTips;

    private String preparationTips;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> nutrients;

}