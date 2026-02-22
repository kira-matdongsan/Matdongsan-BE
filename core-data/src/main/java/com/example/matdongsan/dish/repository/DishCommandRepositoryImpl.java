package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVote;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.domain.DishVoteImageReport;
import com.example.matdongsan.dish.mapper.DishMapper;
import com.example.matdongsan.jpa.entity.dish.DishEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageEntity;
import com.example.matdongsan.jpa.entity.dish.DishVoteImageReportEntity;
import com.example.matdongsan.jpa.repository.DishJpaRepository;
import com.example.matdongsan.jpa.repository.DishVoteImageReportJpaRepository;
import com.example.matdongsan.jpa.repository.DishVoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishCommandRepositoryImpl implements DishCommandRepository {

    private final DishJpaRepository dishJpaRepository;
    private final DishVoteJpaRepository dishVoteJpaRepository;
    private final DishVoteImageReportJpaRepository dishVoteImageReportJpaRepository;
    private final DishMapper dishMapper;

    @Override
    public Dish save(Dish dish) {
        DishEntity entity = dishMapper.toEntity(dish);
        DishEntity saved = dishJpaRepository.save(entity);
        return dishMapper.toDomain(saved);
    }

    @Override
    public DishVote saveVote(DishVote vote) {
        DishVoteEntity voteEntity = DishVoteEntity.builder()
                .dishId(vote.getDishId())
                .userId(vote.getUserId())
                .build();

        List<DishVoteImage> images = vote.getImages();
        if (images != null && !images.isEmpty()) {
            for (DishVoteImage image : images) {
                DishVoteImageEntity imageEntity = DishVoteImageEntity.builder()
                        .imageUrl(image.getImageUrl())
                        .thumbnailUrl(image.getThumbnailUrl())
                        .orderNum(image.getOrderNum())
                        .build();
                voteEntity.addImage(imageEntity);
            }
        }

        DishVoteEntity savedVote = dishVoteJpaRepository.save(voteEntity);
        return dishMapper.toVoteDomain(savedVote);
    }

    @Override
    public DishVoteImageReport saveVoteImageReport(DishVoteImageReport report) {
        DishVoteImageReportEntity entity = DishVoteImageReportEntity.builder()
                .voteImageId(report.getVoteImageId())
                .userId(report.getUserId())
                .build();
        DishVoteImageReportEntity saved = dishVoteImageReportJpaRepository.save(entity);
        return dishMapper.toVoteImageReportDomain(saved);
    }
}
