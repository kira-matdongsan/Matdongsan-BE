package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.mapper.DishMapper;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageEntity;
import com.example.matdongsan.jpa.entity.dish.QDishEntity;
import com.example.matdongsan.jpa.entity.dish.QDishVoteEntity;
import com.example.matdongsan.jpa.entity.dish.QDishVoteImageEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishVoteQueryRepositoryImpl implements DishVoteQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final DishMapper dishMapper;

    private static final QDishEntity dish = QDishEntity.dishEntity;
    private static final QDishVoteEntity dishVote = QDishVoteEntity.dishVoteEntity;
    private static final QDishVoteImageEntity voteImage = QDishVoteImageEntity.dishVoteImageEntity;

    @Override
    public long countVotesByDishId(Long dishId) {
        Long count = queryFactory
                .select(dishVote.id.count())
                .from(dishVote)
                .where(dishVote.dishId.eq(dishId), dishVote.deletedAt.isNull())
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public long countTotalVotesByFeaturedFoodId(Long featuredFoodId) {
        Long count = queryFactory
                .select(dishVote.id.count())
                .from(dishVote)
                .join(dish).on(dish.id.eq(dishVote.dishId))
                .where(dish.featuredFoodId.eq(featuredFoodId), dishVote.deletedAt.isNull())
                .fetchOne();
        return count != null ? count : 0L;
    }

    @Override
    public List<DishVoteImage> findAllActiveImagesByDishId(Long dishId) {
        List<DishVoteImageEntity> entities = queryFactory
                .selectFrom(voteImage)
                .join(dishVote).on(dishVote.id.eq(voteImage.dishVote.id))
                .where(dishVote.dishId.eq(dishId), voteImage.deletedAt.isNull())
                .orderBy(voteImage.createdAt.desc())
                .fetch();
        return dishMapper.toVoteImageDomainList(entities);
    }
}
