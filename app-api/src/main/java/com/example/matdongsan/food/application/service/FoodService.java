package com.example.matdongsan.food.application.service;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.repository.DishQueryRepository;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.application.dto.DishPickServiceDto;
import com.example.matdongsan.food.application.dto.DishServiceDto;
import com.example.matdongsan.food.application.dto.FoodServiceDto;
import com.example.matdongsan.food.application.dto.FoodStoryServiceDto;
import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final FoodQueryRepository foodQueryRepository;
    private final DishQueryRepository dishQueryRepository;

    public FoodServiceDto getFoodInfoById(Long id) {
        Food food = foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return FoodServiceDto.from(food);
    }

    // TODO: 맛동산 Pick 제철요리 투표 관련 기능 논의중
    public DishPickServiceDto getAllDishesByFoodId(Long id) {
        Food food = foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FeaturedFood featuredFood = foodQueryRepository.findLatestFeaturedFoodByFoodId(food.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        List<Dish> dishes = dishQueryRepository.findAllByFeaturedFoodIdOrderByVoteCountDesc(featuredFood.getId());

        List<DishServiceDto> contents =
                IntStream.range(0, dishes.size())
                        .mapToObj(i -> {
                            Dish dish = dishes.get(i);
                            List<DishVoteImage> images = dishQueryRepository.findAllActiveImagesByDishId(dish.getId());
                            DishVoteImage dishVoteImage = null;
                            if (images != null && !images.isEmpty()) {
                                dishVoteImage = images.get(new Random().nextInt(images.size()));
                            }

                            String thumbnailUrl = null;
                            if (dishVoteImage != null) {
                                thumbnailUrl = dishVoteImage.getThumbnailUrl() != null
                                        ? dishVoteImage.getThumbnailUrl()
                                        : dishVoteImage.getImageUrl();
                            }

                            return DishServiceDto.builder()
                                    .id(dish.getId())
                                    .name(dish.getName())
                                    .thumbnailUrl(thumbnailUrl)
                                    .rank(i + 1)
                                    .voteCount(dish.getVoteCount())
                                    .build();
                        })
                        .toList();

        return DishPickServiceDto.builder()
                .voteStartDate(featuredFood.getStartAt().toLocalDate())
                .voteEndDate(featuredFood.getEndAt().toLocalDate())
                .totalVoteCount(featuredFood.getDishVoteCount())
                .contents(contents)
                .build();
    }

    public Page<FoodStoryServiceDto> getAllStoriesByFoodId(Long id, Pageable pageable) {
        foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        List<FoodStory> stories = foodQueryRepository.findAllStoriesByFoodId(id, pageable.getPageNumber(), pageable.getPageSize());
        long totalCount = foodQueryRepository.countStoriesByFoodId(id);

        List<FoodStoryServiceDto> dtos = stories.stream()
                .map(story -> {
                    List<FoodStoryImage> images = foodQueryRepository.findAllImagesByStoryId(story.getId());
                    return FoodStoryServiceDto.from(story, images);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, totalCount);
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void likeFood(Long id) {

    }

}
