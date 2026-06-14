package com.example.matdongsan.seasonaldiary.mapper;

import com.example.matdongsan.jpa.entity.seasonaldiary.SeasonalDiaryEntity;
import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeasonalDiaryMapper {

    SeasonalDiary toDomain(SeasonalDiaryEntity entity);

    List<SeasonalDiary> toDomainList(List<SeasonalDiaryEntity> entities);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    SeasonalDiaryEntity toEntity(SeasonalDiary domain);
}
