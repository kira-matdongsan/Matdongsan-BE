package com.example.matdongsan.seasonaldiary.repository;

import com.example.matdongsan.jpa.entity.seasonaldiary.QSeasonalDiaryEntity;
import com.example.matdongsan.jpa.entity.seasonaldiary.SeasonalDiaryEntity;
import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import com.example.matdongsan.seasonaldiary.mapper.SeasonalDiaryMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeasonalDiaryQueryRepositoryImpl implements SeasonalDiaryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final SeasonalDiaryMapper mapper;

    private static final QSeasonalDiaryEntity diary = QSeasonalDiaryEntity.seasonalDiaryEntity;

    @Override
    public Optional<SeasonalDiary> findById(Long id) {
        SeasonalDiaryEntity entity = queryFactory.selectFrom(diary)
                .where(diary.id.eq(id), diary.deletedAt.isNull())
                .fetchOne();
        return Optional.ofNullable(entity).map(mapper::toDomain);
    }

    @Override
    public List<SeasonalDiary> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        return mapper.toDomainList(queryFactory.selectFrom(diary)
                .where(diary.userId.eq(userId),
                        diary.recordDate.between(from, to),
                        diary.deletedAt.isNull())
                .orderBy(diary.recordDate.asc(), diary.createdAt.desc())
                .fetch());
    }

    @Override
    public List<SeasonalDiary> findByUserIdAndRecordDate(Long userId, LocalDate date) {
        return mapper.toDomainList(queryFactory.selectFrom(diary)
                .where(diary.userId.eq(userId),
                        diary.recordDate.eq(date),
                        diary.deletedAt.isNull())
                .orderBy(diary.createdAt.desc())
                .fetch());
    }
}
