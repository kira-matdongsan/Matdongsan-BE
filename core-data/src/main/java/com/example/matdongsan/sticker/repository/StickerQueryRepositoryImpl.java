package com.example.matdongsan.sticker.repository;

import com.example.matdongsan.jpa.entity.sticker.QStickerEntity;
import com.example.matdongsan.jpa.entity.sticker.StickerEntity;
import com.example.matdongsan.sticker.domain.Sticker;
import com.example.matdongsan.sticker.mapper.StickerMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StickerQueryRepositoryImpl implements StickerQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final StickerMapper mapper;

    private static final QStickerEntity sticker = QStickerEntity.stickerEntity;

    @Override
    public List<Sticker> findAllActiveOrderByDisplayOrder() {
        return mapper.toDomainList(queryFactory.selectFrom(sticker)
                .where(sticker.active.isTrue())
                .orderBy(sticker.displayOrder.asc())
                .fetch());
    }

    @Override
    public Optional<Sticker> findById(Long id) {
        StickerEntity entity = queryFactory.selectFrom(sticker)
                .where(sticker.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(entity).map(mapper::toDomain);
    }

    @Override
    public List<Sticker> findAllByIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return mapper.toDomainList(queryFactory.selectFrom(sticker)
                .where(sticker.id.in(ids))
                .fetch());
    }
}
