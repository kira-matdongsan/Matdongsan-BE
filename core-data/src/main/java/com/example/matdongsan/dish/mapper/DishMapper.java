package com.example.matdongsan.dish.mapper;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVote;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.domain.DishVoteImageReport;
import com.example.matdongsan.jpa.entity.dish.DishEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageReportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishMapper {

    // === Dish ===

    Dish toDomain(DishEntity entity);

    List<Dish> toDomainList(List<DishEntity> entities);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DishEntity toEntity(Dish domain);

    // === DishVote ===

    @Mapping(target = "images", source = "images")
    DishVote toVoteDomain(DishVoteEntity entity);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DishVoteEntity toVoteEntity(DishVote domain);

    // === DishVoteImage ===

    @Mapping(target = "dishVoteId", source = "dishVote.id")
    DishVoteImage toVoteImageDomain(DishVoteImageEntity entity);

    List<DishVoteImage> toVoteImageDomainList(List<DishVoteImageEntity> entities);

    @Mapping(target = "dishVote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DishVoteImageEntity toVoteImageEntity(DishVoteImage domain);

    List<DishVoteImageEntity> toVoteImageEntityList(List<DishVoteImage> domains);

    // === DishVoteImageReport ===

    DishVoteImageReport toVoteImageReportDomain(DishVoteImageReportEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DishVoteImageReportEntity toVoteImageReportEntity(DishVoteImageReport domain);
}
