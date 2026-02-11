package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.mapper.DishMapper;
import com.example.matdongsan.jpa.entity.dish.DishEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageEntity;
import com.example.matdongsan.jpa.entity.dish.QDishEntity;
import com.example.matdongsan.jpa.entity.dish.QDishVoteImageEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DishQueryRepositoryImpl implements DishQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DishMapper dishMapper;

    private static final QDishEntity dish = QDishEntity.dishEntity;
    private static final QDishVoteImageEntity voteImage = QDishVoteImageEntity.dishVoteImageEntity;

    @Override
    public Optional<Dish> findById(Long id) {
        DishEntity entity = queryFactory
                .selectFrom(dish)
                .where(dish.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(entity).map(dishMapper::toDomain);
    }

    @Override
    public List<Dish> findAllByFeaturedFoodIdOrderByVoteCountDesc(Long featuredFoodId) {
        List<DishEntity> entities = queryFactory
                .selectFrom(dish)
                .where(dish.featuredFoodId.eq(featuredFoodId))
                .orderBy(dish.voteCount.desc())
                .fetch();
        return dishMapper.toDomainList(entities);
    }

    @Override
    public List<DishVoteImage> findAllActiveImagesByDishId(Long dishId) {
        List<DishVoteImageEntity> entities = queryFactory
                .selectFrom(voteImage)
                .where(
                        voteImage.dishId.eq(dishId),
                        voteImage.deletedAt.isNull()
                )
                .orderBy(voteImage.createdAt.desc())
                .fetch();
        return dishMapper.toVoteImageDomainList(entities);
    }
}
