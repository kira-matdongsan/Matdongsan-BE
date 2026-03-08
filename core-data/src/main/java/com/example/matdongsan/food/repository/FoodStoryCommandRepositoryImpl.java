package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.domain.FoodStoryReport;
import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.food.enums.FoodStoryVisibility;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.FoodStoryEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryImageEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryPlaceEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryRecipeEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryReportEntity;
import com.example.matdongsan.jpa.entity.food.FoodStorySeasonalNoteEntity;
import com.example.matdongsan.jpa.repository.FoodStoryImageJpaRepository;
import com.example.matdongsan.jpa.repository.FoodStoryJpaRepository;
import com.example.matdongsan.jpa.repository.FoodStoryPlaceJpaRepository;
import com.example.matdongsan.jpa.repository.FoodStoryRecipeJpaRepository;
import com.example.matdongsan.jpa.repository.FoodStoryReportJpaRepository;
import com.example.matdongsan.jpa.repository.FoodStorySeasonalNoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FoodStoryCommandRepositoryImpl implements FoodStoryCommandRepository {

    private final FoodStoryJpaRepository storyJpaRepository;
    private final FoodStorySeasonalNoteJpaRepository seasonalNoteJpaRepository;
    private final FoodStoryRecipeJpaRepository recipeJpaRepository;
    private final FoodStoryPlaceJpaRepository placeJpaRepository;
    private final FoodStoryImageJpaRepository storyImageJpaRepository;
    private final FoodStoryReportJpaRepository storyReportJpaRepository;
    private final FoodMapper foodMapper;

    @Override
    public FoodStory save(FoodStory story) {
        FoodStoryEntity savedEntity;

        if (story.getType() == FoodStoryType.SEASONAL_NOTE) {
            FoodStorySeasonalNoteEntity entity = FoodStorySeasonalNoteEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .visibility(story.getVisibility())
                    .content(story.getContent())
                    .recordedDate(story.getRecordedDate())
                    .build();
            savedEntity = seasonalNoteJpaRepository.save(entity);
        } else if (story.getType() == FoodStoryType.RECIPE) {
            FoodStoryRecipeEntity entity = FoodStoryRecipeEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .visibility(story.getVisibility())
                    .recipeName(story.getRecipeName())
                    .ingredients(story.getIngredients())
                    .instructions(story.getInstructions())
                    .build();
            savedEntity = recipeJpaRepository.save(entity);
        } else {
            FoodStoryPlaceEntity entity = FoodStoryPlaceEntity.builder()
                    .foodId(story.getFoodId())
                    .userId(story.getUserId())
                    .visibility(story.getVisibility())
                    .placeName(story.getPlaceName())
                    .content(story.getPlaceContent())
                    .category(story.getCategory())
                    .address(story.getAddress())
                    .naverUrl(story.getNaverUrl())
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
    public FoodStoryReport saveReport(FoodStoryReport report) {
        FoodStoryReportEntity entity = FoodStoryReportEntity.builder()
                .foodStoryId(report.getFoodStoryId())
                .userId(report.getUserId())
                .reason(report.getReason())
                .build();
        FoodStoryReportEntity saved = storyReportJpaRepository.saveAndFlush(entity);
        return foodMapper.toStoryReportDomain(saved);
    }

    @Override
    public void updateVisibility(Long storyId, FoodStoryVisibility visibility) {
        storyJpaRepository.updateVisibilityById(storyId, visibility);
    }

    @Override
    public void deleteById(Long storyId) {
        storyJpaRepository.softDeleteById(storyId);
    }
}
