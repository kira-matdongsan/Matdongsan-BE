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
import com.example.matdongsan.jpa.repository.DishVoteImageJpaRepository;
import com.example.matdongsan.jpa.repository.DishVoteImageReportJpaRepository;
import com.example.matdongsan.jpa.repository.DishVoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DishCommandRepositoryImpl implements DishCommandRepository {

    private final DishJpaRepository dishJpaRepository;
    private final DishVoteJpaRepository dishVoteJpaRepository;
    private final DishVoteImageJpaRepository dishVoteImageJpaRepository;
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
        // 1. Vote 엔티티 저장
        DishEntity dishRef = dishJpaRepository.getReferenceById(vote.getDishId());
        DishVoteEntity voteEntity = DishVoteEntity.builder()
                .dish(dishRef)
                .userId(vote.getUserId())
                .build();
        DishVoteEntity savedVote = dishVoteJpaRepository.save(voteEntity);

        // 2. Vote에 포함된 Images 함께 저장
        List<DishVoteImage> images = vote.getImages();
        if (images != null && !images.isEmpty()) {
            List<DishVoteImageEntity> imageEntities = images.stream()
                    .map(image -> DishVoteImageEntity.builder()
                            .dish(dishRef)
                            .dishVote(savedVote)
                            .imageUrl(image.getImageUrl())
                            .thumbnailUrl(image.getThumbnailUrl())
                            .orderNum(image.getOrderNum())
                            .reportCount(image.getReportCount() != null ? image.getReportCount() : 0)
                            .build())
                    .collect(Collectors.toList());
            dishVoteImageJpaRepository.saveAll(imageEntities);
        }

        return dishMapper.toVoteDomain(savedVote);
    }

    @Override
    public DishVoteImageReport saveVoteImageReport(DishVoteImageReport report) {
        DishVoteImageEntity voteImageRef = dishVoteImageJpaRepository.getReferenceById(report.getVoteImageId());
        DishVoteImageReportEntity entity = DishVoteImageReportEntity.builder()
                .voteImage(voteImageRef)
                .userId(report.getUserId())
                .build();
        DishVoteImageReportEntity saved = dishVoteImageReportJpaRepository.save(entity);
        return dishMapper.toVoteImageReportDomain(saved);
    }
}
