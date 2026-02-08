package com.example.matdongsan.food.mapper;

import com.example.matdongsan.food.domain.*;
import com.example.matdongsan.jpa.entity.food.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    // === Food ===

    Food toFoodDomain(FoodEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FoodEntity toFoodEntity(Food domain);

    List<Food> toFoodDomainList(List<FoodEntity> entities);

    // === FeaturedFood ===

    @Mapping(target = "foodId", source = "food.id")
    FeaturedFood toFeaturedFoodDomain(FeaturedFoodEntity entity);

    // === FoodStory ===

    default FoodStory toStoryDomain(FoodStoryEntity entity) {
        if (entity == null) return null;

        FoodStory.FoodStoryBuilder builder = FoodStory.builder()
                .id(entity.getId())
                .foodId(entity.getFood().getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .likeCount(entity.getLikeCount())
                .reportCount(entity.getReportCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt());

        if (entity instanceof FoodStorySeasonalNoteEntity note) {
            builder.content(note.getContent());
            builder.recordedDate(note.getRecordedDate());
        } else if (entity instanceof FoodStoryRecipeEntity recipe) {
            builder.recipeName(recipe.getRecipeName());
            builder.ingredients(recipe.getIngredients());
            builder.instructions(recipe.getInstructions());
        } else if (entity instanceof FoodStoryPlaceEntity place) {
            builder.placeName(place.getPlaceName());
            builder.placeContent(place.getContent());
            builder.category(place.getCategory());
            builder.address(place.getAddress());
            builder.naverUrl(place.getNaverUrl());
        }

        return builder.build();
    }

    // === FoodStoryImage ===

    @Mapping(target = "foodStoryId", source = "foodStory.id")
    FoodStoryImage toStoryImageDomain(FoodStoryImageEntity entity);

    List<FoodStoryImage> toStoryImageDomainList(List<FoodStoryImageEntity> entities);

    @Mapping(target = "foodStory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    FoodStoryImageEntity toStoryImageEntity(FoodStoryImage domain);

    List<FoodStoryImageEntity> toStoryImageEntityList(List<FoodStoryImage> domains);
}
