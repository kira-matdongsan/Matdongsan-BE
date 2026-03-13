package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.food.enums.FoodStoryVisibility;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.FoodStoryEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryImageEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryPlaceEntity;
import com.example.matdongsan.jpa.entity.food.FoodStoryRecipeEntity;
import com.example.matdongsan.jpa.entity.food.FoodStorySeasonalNoteEntity;
import com.example.matdongsan.jpa.entity.food.QFoodStoryEntity;
import com.example.matdongsan.jpa.entity.food.QFoodStoryImageEntity;
import com.example.matdongsan.jpa.entity.food.QFoodStoryLikeEntity;
import com.example.matdongsan.jpa.entity.food.QFoodStoryReportEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoodStoryQueryRepositoryImpl implements FoodStoryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final FoodMapper foodMapper;

    private static final QFoodStoryEntity story = QFoodStoryEntity.foodStoryEntity;
    private static final QFoodStoryImageEntity storyImage = QFoodStoryImageEntity.foodStoryImageEntity;
    private static final QFoodStoryLikeEntity storyLike = QFoodStoryLikeEntity.foodStoryLikeEntity;
    private static final QFoodStoryReportEntity storyReport = QFoodStoryReportEntity.foodStoryReportEntity;

    @Override
    public List<FoodStory> findAllByFoodId(Long foodId, FoodStoryType type, int page, int size, List<Long> blockedUserIds) {
        List<FoodStoryEntity> entities = queryFactory
                .selectFrom(story)
                .where(
                        story.foodId.eq(foodId),
                        story.deletedAt.isNull(),
                        story.visibility.eq(FoodStoryVisibility.VISIBLE),
                        typeEq(type),
                        blockedUsersNotIn(blockedUserIds)
                )
                .orderBy(story.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
        return entities.stream().map(foodMapper::toStoryDomain).toList();
    }

    @Override
    public long countByFoodId(Long foodId, FoodStoryType type, List<Long> blockedUserIds) {
        Long count = queryFactory
                .select(story.count())
                .from(story)
                .where(
                        story.foodId.eq(foodId),
                        story.deletedAt.isNull(),
                        story.visibility.eq(FoodStoryVisibility.VISIBLE),
                        typeEq(type),
                        blockedUsersNotIn(blockedUserIds)
                )
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public long countLikesById(Long storyId) {
        Long count = queryFactory
                .select(storyLike.id.count())
                .from(storyLike)
                .where(storyLike.foodStoryId.eq(storyId), storyLike.deletedAt.isNull())
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public long countReportsById(Long storyId) {
        Long count = queryFactory
                .select(storyReport.id.count())
                .from(storyReport)
                .where(storyReport.foodStoryId.eq(storyId), storyReport.deletedAt.isNull())
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public boolean existsReportByStoryIdAndUserId(Long storyId, Long userId) {
        Integer fetched = queryFactory
                .selectOne()
                .from(storyReport)
                .where(
                        storyReport.foodStoryId.eq(storyId),
                        storyReport.userId.eq(userId),
                        storyReport.deletedAt.isNull()
                )
                .fetchFirst();
        return fetched != null;
    }

    @Override
    public Optional<FoodStory> findById(Long storyId) {
        FoodStoryEntity entity = queryFactory
                .selectFrom(story)
                .where(
                        story.id.eq(storyId),
                        story.deletedAt.isNull()
                )
                .fetchOne();
        return Optional.ofNullable(entity).map(foodMapper::toStoryDomain);
    }

    @Override
    public List<FoodStoryImage> findAllImagesById(Long storyId) {
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

    private BooleanBuilder typeEq(FoodStoryType type) {
        if (type == null) return null;
        return switch (type) {
            case RECIPE -> new BooleanBuilder(story.instanceOf(FoodStoryRecipeEntity.class));
            case PLACE -> new BooleanBuilder(story.instanceOf(FoodStoryPlaceEntity.class));
            case SEASONAL_NOTE -> new BooleanBuilder(story.instanceOf(FoodStorySeasonalNoteEntity.class));
        };
    }

    private BooleanBuilder blockedUsersNotIn(List<Long> blockedUserIds) {
        if (blockedUserIds == null || blockedUserIds.isEmpty()) return null;
        return new BooleanBuilder(story.userId.notIn(blockedUserIds));
    }
}
