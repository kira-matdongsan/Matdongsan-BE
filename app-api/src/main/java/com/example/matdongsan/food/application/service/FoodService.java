package com.example.matdongsan.food.application.service;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.presentation.response.*;
import com.example.matdongsan.jpa.entity.Dish;
import com.example.matdongsan.jpa.entity.DishVoteImage;
import com.example.matdongsan.jpa.entity.FeaturedFood;
import com.example.matdongsan.jpa.entity.Food;
import com.example.matdongsan.jpa.repository.*;
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

    public FoodInfoResponse getFoodInfoById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return FoodInfoResponse.of(food, true);
    }

    // TODO: 맛동산 Pick 제철요리 투표 관련 기능 논의중
    public DishPickResponse getAllDishesByFoodId(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FeaturedFood featuredFood = featuredFoodRepository.findFirstByFoodOrderByStartAtDesc(food)
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        List<Dish> dishes = dishRepository.findAllByFeaturedFoodOrderByVoteCountDesc(featuredFood);

        List<DishResponse> contents =
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

                            return DishResponse.builder()
                                    .id(dish.getId())
                                    .name(dish.getName())
                                    .thumbnailUrl(thumbnailUrl)
                                    .rank(i + 1)
                                    .voteCount(dish.getVoteCount())
                                    .build();
                        })
                        .toList();

        return DishPickResponse.builder()
                .voteStartDate(featuredFood.getStartAt().toLocalDate())
                .voteEndDate(featuredFood.getEndAt().toLocalDate())
                .totalVoteCount(featuredFood.getDishVoteCount())
                .contents(contents)
                .build();
    }

    public Page<StoryResponse> getAllStoriesByFoodId(Long id, Pageable pageable) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return foodStoryRepository.findAllByFoodAndDeletedAtIsNull(food, pageable)
                .map(foodStory -> {
                    List<StoryImageResponse> images = foodStoryImageRepository.findAllByFoodStoryAndDeletedAtIsNull(foodStory)
                            .stream()
                            .map(StoryImageResponse::of)
                            .toList();
                    return StoryResponse.of(foodStory, images, true);
                });
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void likeFood(Long id) {

    }

}
