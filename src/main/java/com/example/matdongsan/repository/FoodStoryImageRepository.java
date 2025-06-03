package com.example.matdongsan.repository;

import com.example.matdongsan.domain.FoodStory;
import com.example.matdongsan.domain.FoodStoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodStoryImageRepository extends JpaRepository<FoodStoryImage, Long> {

    List<FoodStoryImage> findAllByFoodStoryAndDeletedAtIsNull(FoodStory foodStory);
}
