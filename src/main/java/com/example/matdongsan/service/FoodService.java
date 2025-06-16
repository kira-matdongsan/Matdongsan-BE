package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.controller.dto.*;
import com.example.matdongsan.domain.Dish;
import com.example.matdongsan.domain.DishVoteImage;
import com.example.matdongsan.domain.FeaturedFood;
import com.example.matdongsan.domain.Food;
import com.example.matdongsan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodStoryRepository foodStoryRepository;
    private final FoodStoryImageRepository foodStoryImageRepository;
    private final FeaturedFoodRepository featuredFoodRepository;
    private final DishRepository dishRepository;

    public FoodInfoResponseDto getFoodInfoById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return FoodInfoResponseDto.of(food, true);
    }

    // TODO: 맛동산 Pick 제철요리 투표 관련 기능 논의중
    public DishPickResponseDto getAllDishesByFoodId(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FeaturedFood featuredFood = featuredFoodRepository.findFirstByFoodOrderByStartAtDesc(food)
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        List<Dish> dishes = dishRepository.findAllByFeaturedFoodOrderByVoteCountDesc(featuredFood);

        List<DishResponseDto> contents =
                IntStream.range(0, dishes.size())
                        .mapToObj(i -> {
                            Dish dish = dishes.get(i);
                            List<DishVoteImage> images = dish.getImages();
                            DishVoteImage dishVoteImage = null;
                            if (!images.isEmpty()) {
                                dishVoteImage = images.get(new Random().nextInt(images.size()));
                            }

                            String thumbnailUrl = null;
                            if (dishVoteImage != null) {
                                thumbnailUrl = dishVoteImage.getThumbnailUrl() != null
                                        ? dishVoteImage.getThumbnailUrl()
                                        : dishVoteImage.getImageUrl();
                            }

                            return DishResponseDto.builder()
                                    .id(dish.getId())
                                    .name(dish.getName())
                                    .thumbnailUrl(thumbnailUrl)
                                    .rank(i + 1)
                                    .voteCount(dish.getVoteCount())
                                    .build();
                        })
                        .toList();

        return DishPickResponseDto.builder()
                .voteStartDate(featuredFood.getStartAt().toLocalDate())
                .voteEndDate(featuredFood.getEndAt().toLocalDate())
                .totalVoteCount(featuredFood.getDishVoteCount())
                .contents(contents)
                .build();
    }

    public Page<StoryResponseDto> getAllStoriesByFoodId(Long id, Pageable pageable) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return foodStoryRepository.findAllByFoodAndDeletedAtIsNull(food, pageable)
                .map(foodStory -> {
                    List<StoryImageResponseDto> images = foodStoryImageRepository.findAllByFoodStoryAndDeletedAtIsNull(foodStory)
                            .stream()
                            .map(StoryImageResponseDto::of)
                            .toList();
                    return StoryResponseDto.of(foodStory, images, true);
                });
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void likeFood(Long id) {

    }

}
