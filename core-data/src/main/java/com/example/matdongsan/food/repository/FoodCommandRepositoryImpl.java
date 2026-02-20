package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.*;
import com.example.matdongsan.jpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FoodCommandRepositoryImpl implements FoodCommandRepository {

    private final FoodJpaRepository foodJpaRepository;
    private final FoodStoryJpaRepository storyJpaRepository;
    private final FoodStorySeasonalNoteJpaRepository seasonalNoteJpaRepository;
    private final FoodStoryRecipeJpaRepository recipeJpaRepository;
    private final FoodStoryPlaceJpaRepository placeJpaRepository;
    private final FoodStoryImageJpaRepository storyImageJpaRepository;
    private final FeaturedFoodJpaRepository featuredFoodJpaRepository;
    private final FoodMapper foodMapper;

    @Override
    public Food save(Food food) {
        FoodEntity entity = foodMapper.toFoodEntity(food);
        FoodEntity saved = foodJpaRepository.save(entity);
        return foodMapper.toFoodDomain(saved);
    }

    @Override
    public List<Food> saveAll(List<Food> foods) {
        List<FoodEntity> entities = foods.stream()
                .map(foodMapper::toFoodEntity)
                .collect(Collectors.toList());
        List<FoodEntity> saved = foodJpaRepository.saveAll(entities);
        return saved.stream().map(foodMapper::toFoodDomain).toList();
    }

    @Override
    public FoodStory saveStory(FoodStory story) {
        FoodStoryEntity savedEntity;

        if (story.getType() == FoodStoryType.SEASONAL_NOTE) {
            FoodStorySeasonalNoteEntity entity = FoodStorySeasonalNoteEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .content(story.getContent())
                    .recordedDate(story.getRecordedDate())
                    .likeCount(0)
                    .reportCount(0)
                    .build();
            savedEntity = seasonalNoteJpaRepository.save(entity);
        } else if (story.getType() == FoodStoryType.RECIPE) {
            FoodStoryRecipeEntity entity = FoodStoryRecipeEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .recipeName(story.getRecipeName())
                    .ingredients(story.getIngredients())
                    .instructions(story.getInstructions())
                    .likeCount(0)
                    .reportCount(0)
                    .build();
            savedEntity = recipeJpaRepository.save(entity);
        } else {
            FoodStoryPlaceEntity entity = FoodStoryPlaceEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .placeName(story.getPlaceName())
                    .content(story.getPlaceContent())
                    .category(story.getCategory())
                    .address(story.getAddress())
                    .naverUrl(story.getNaverUrl())
                    .likeCount(0)
                    .reportCount(0)
                    .build();
            savedEntity = placeJpaRepository.save(entity);
        }

        return foodMapper.toStoryDomain(savedEntity);
    }

    @Override
    public List<FoodStoryImage> saveAllImages(List<FoodStoryImage> images) {
        if (images == null || images.isEmpty()) return List.of();

        List<FoodStoryImageEntity> entities = images.stream()
                .map(image -> {
                    FoodStoryEntity storyRef = storyJpaRepository.getReferenceById(image.getFoodStoryId());
                    return FoodStoryImageEntity.builder()
                            .foodStory(storyRef)
                            .imageUrl(image.getImageUrl())
                            .thumbnailUrl(image.getThumbnailUrl())
                            .orderNum(image.getOrderNum())
                            .build();
                })
                .collect(Collectors.toList());
        List<FoodStoryImageEntity> saved = storyImageJpaRepository.saveAll(entities);
        return foodMapper.toStoryImageDomainList(saved);
    }

    @Override
    public void deleteStory(Long storyId) {
        FoodStoryEntity entity = storyJpaRepository.getReferenceById(storyId);
        entity.softDelete();
    }

    @Override
    public FeaturedFood saveFeaturedFood(FeaturedFood featuredFood) {
        FeaturedFoodEntity entity = foodMapper.toFeaturedFoodEntity(featuredFood);
        FeaturedFoodEntity saved = featuredFoodJpaRepository.save(entity);
        return foodMapper.toFeaturedFoodDomain(saved);
    }
}
