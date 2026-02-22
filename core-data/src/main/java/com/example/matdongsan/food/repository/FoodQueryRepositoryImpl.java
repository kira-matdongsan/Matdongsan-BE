package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.FeaturedFoodEntity;
import com.example.matdongsan.jpa.entity.food.FoodEntity;
import com.example.matdongsan.jpa.entity.food.QFeaturedFoodEntity;
import com.example.matdongsan.jpa.entity.food.QFoodEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoodQueryRepositoryImpl implements FoodQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final FoodMapper foodMapper;

    private static final QFoodEntity food = QFoodEntity.foodEntity;
    private static final QFeaturedFoodEntity featuredFood = QFeaturedFoodEntity.featuredFoodEntity;

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
    public Optional<FeaturedFood> findFeaturedFoodById(Long id) {
        FeaturedFoodEntity entity = queryFactory
                .selectFrom(featuredFood)
                .where(featuredFood.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(entity).map(foodMapper::toFeaturedFoodDomain);
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
    public Optional<FeaturedFood> findActiveFeaturedFoodByFoodId(Long foodId) {
        FeaturedFoodEntity entity = queryFactory
                .selectFrom(featuredFood)
                .where(featuredFood.foodId.eq(foodId), featuredFood.active.isTrue())
                .fetchOne();
        return Optional.ofNullable(entity).map(foodMapper::toFeaturedFoodDomain);
    }

    @Override
    public Optional<Food> findCurrentFeaturedFood() {
        FeaturedFoodEntity ff = queryFactory
                .selectFrom(featuredFood)
                .where(featuredFood.active.isTrue())
                .fetchOne();
        if (ff == null) return Optional.empty();
        FoodEntity entity = queryFactory
                .selectFrom(food)
                .where(food.id.eq(ff.getFoodId()))
                .fetchOne();
        return Optional.ofNullable(entity).map(foodMapper::toFoodDomain);
    }
}
