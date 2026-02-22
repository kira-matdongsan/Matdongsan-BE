package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.mapper.DishMapper;
import com.example.matdongsan.jpa.entity.dish.DishEntity;
import com.example.matdongsan.jpa.entity.dish.QDishEntity;
import com.example.matdongsan.jpa.entity.dish.QDishVoteEntity;
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
    private static final QDishVoteEntity dishVote = QDishVoteEntity.dishVoteEntity;

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
                .leftJoin(dishVote).on(dishVote.dishId.eq(dish.id).and(dishVote.deletedAt.isNull()))
                .where(dish.featuredFoodId.eq(featuredFoodId), dish.deletedAt.isNull())
                .groupBy(dish.id)
                .orderBy(dishVote.id.count().desc())
                .fetch();
        return dishMapper.toDomainList(entities);
    }
}
