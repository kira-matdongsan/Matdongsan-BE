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
import com.example.matdongsan.food.application.dto.StoryParam;
import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
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

        String weekText = foodQueryRepository.findLatestFeaturedFoodByFoodId(id)
                .map(ff -> calculateWeekText(ff.getYear(), ff.getWeek()))
                .orElse(null);

        return FoodServiceDto.from(food, weekText);
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

    public Page<FoodStoryServiceDto> getAllStoriesByFoodId(Long id, StoryParam param) {
        foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        List<FoodStory> stories = foodQueryRepository.findAllStoriesByFoodId(id, param.getType(), param.getPage(), param.getSize());
        long totalCount = foodQueryRepository.countStoriesByFoodId(id, param.getType());

        List<FoodStoryServiceDto> dtos = stories.stream()
                .map(story -> {
                    List<FoodStoryImage> images = foodQueryRepository.findAllImagesByStoryId(story.getId());
                    return FoodStoryServiceDto.from(story, images);
                })
                .toList();

        return new PageImpl<>(dtos, PageRequest.of(param.getPage(), param.getSize()), totalCount);
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void likeFood(Long id) {

    }

    private String calculateWeekText(int year, int weekOfYear) {
        // ISO 8601: 목요일이 속한 달을 기준으로 주차 결정
        // 1월 4일은 항상 ISO 1주차에 속함
        LocalDate thursday = LocalDate.of(year, 1, 4)
                .with(DayOfWeek.THURSDAY)
                .plusWeeks(weekOfYear - 1);

        int month = thursday.getMonthValue();
        int weekOfMonth = thursday.get(WeekFields.ISO.weekOfMonth());

        String weekOrdinal = switch (weekOfMonth) {
            case 1 -> "첫째주";
            case 2 -> "둘째주";
            case 3 -> "셋째주";
            case 4 -> "넷째주";
            case 5 -> "다섯째주";
            default -> weekOfMonth + "째주";
        };

        return year + "년 " + month + "월 " + weekOrdinal;
    }
}
