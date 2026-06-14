package com.example.matdongsan.seasonaldiary.repository;

import com.example.matdongsan.jpa.entity.seasonaldiary.SeasonalDiaryEntity;
import com.example.matdongsan.jpa.repository.SeasonalDiaryJpaRepository;
import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import com.example.matdongsan.seasonaldiary.mapper.SeasonalDiaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SeasonalDiaryCommandRepositoryImpl implements SeasonalDiaryCommandRepository {

    private final SeasonalDiaryJpaRepository jpaRepository;
    private final SeasonalDiaryMapper mapper;

    @Override
    public SeasonalDiary save(SeasonalDiary diary) {
        SeasonalDiaryEntity saved = jpaRepository.save(mapper.toEntity(diary));
        return mapper.toDomain(saved);
    }

    @Override
    public void update(Long id, SeasonalDiarySticker sticker, String content) {
        jpaRepository.updateById(id, sticker, content);
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }
}
