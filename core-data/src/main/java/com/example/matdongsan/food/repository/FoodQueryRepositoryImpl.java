package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.*;
import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoodQueryRepositoryImpl implements FoodQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final FoodMapper foodMapper;

    private static final QFoodEntity food = QFoodEntity.foodEntity;
    private static final QFeaturedFoodEntity featuredFood = QFeaturedFoodEntity.featuredFoodEntity;
    private static final QFoodStoryEntity story = QFoodStoryEntity.foodStoryEntity;
    private static final QFoodStoryImageEntity storyImage = QFoodStoryImageEntity.foodStoryImageEntity;

    @Override
    public Optional<Food> findById(Long id) {
        FoodEntity entity = queryFactory
                .selectFrom(food)
                .where(food.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(entity).map(foodMapper::toFoodDomain);
    }

    @Override
    public boolean existsByName(String name) {
        FoodEntity entity = queryFactory
                .selectFrom(food)
                .where(food.name.eq(name))
                .fetchFirst();
        return entity != null;
    }

    @Override
    public Optional<FeaturedFood> findLatestFeaturedFoodByFoodId(Long foodId) {
        FeaturedFoodEntity entity = queryFactory
                .selectFrom(featuredFood)
                .where(featuredFood.foodId.eq(foodId))
                .orderBy(featuredFood.startAt.desc())
                .fetchFirst();
        return Optional.ofNullable(entity).map(foodMapper::toFeaturedFoodDomain);
    }

    @Override
    public List<FoodStory> findAllStoriesByFoodId(Long foodId, FoodStoryType type, int page, int size) {
        List<FoodStoryEntity> entities = queryFactory
                .selectFrom(story)
                .where(
                        story.foodId.eq(foodId),
                        story.deletedAt.isNull(),
                        typeEq(type)
                )
                .orderBy(story.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
        return entities.stream().map(foodMapper::toStoryDomain).toList();
    }

    @Override
    public long countStoriesByFoodId(Long foodId, FoodStoryType type) {
        Long count = queryFactory
                .select(story.count())
                .from(story)
                .where(
                        story.foodId.eq(foodId),
                        story.deletedAt.isNull(),
                        typeEq(type)
                )
                .fetchOne();
        return count != null ? count : 0L;
    }

    private BooleanBuilder typeEq(FoodStoryType type) {
        if (type == null) return null;
        return switch (type) {
            case RECIPE -> new BooleanBuilder(story.instanceOf(FoodStoryRecipeEntity.class));
            case PLACE -> new BooleanBuilder(story.instanceOf(FoodStoryPlaceEntity.class));
            case SEASONAL_NOTE -> new BooleanBuilder(story.instanceOf(FoodStorySeasonalNoteEntity.class));
        };
    }

    @Override
    public List<FoodStoryImage> findAllImagesByStoryId(Long storyId) {
        List<FoodStoryImageEntity> entities = queryFactory
                .selectFrom(storyImage)
                .where(
                        storyImage.foodStory.id.eq(storyId),
                        storyImage.deletedAt.isNull()
                )
                .orderBy(storyImage.orderNum.asc())
                .fetch();
        return foodMapper.toStoryImageDomainList(entities);
    }

    @Override
    public Optional<Food> findCurrentFeaturedFood() {
        FoodEntity entity = queryFactory
                .selectFrom(food)
                .where(food.isFeatured.isTrue())
                .orderBy(food.lastFeaturedAt.desc())
                .fetchFirst();
        return Optional.ofNullable(entity).map(foodMapper::toFoodDomain);
    }
}
