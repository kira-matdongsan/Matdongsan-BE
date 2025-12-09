package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FoodStory;
import com.example.matdongsan.jpa.entity.food.FoodStoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodStoryImageRepository extends JpaRepository<FoodStoryImage, Long> {

    List<FoodStoryImage> findAllByFoodStoryAndDeletedAtIsNull(FoodStory foodStory);
}
