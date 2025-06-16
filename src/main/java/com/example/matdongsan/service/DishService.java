package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.controller.dto.DishVoteImageResponseDto;
import com.example.matdongsan.domain.*;
import com.example.matdongsan.repository.*;
import com.example.matdongsan.service.dto.DishServiceDto;
import com.example.matdongsan.service.dto.DishVoteServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DishService {

    private final FoodRepository foodRepository;
    private final FeaturedFoodRepository featuredFoodRepository;
    private final DishRepository dishRepository;
    private final DishVoteRepository dishVoteRepository;
    private final DishVoteImageRepository dishVoteImageRepository;

    public List<DishVoteImageResponseDto> getAllImagesById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.DISH_NOT_FOUND));

        List<DishVoteImage> dishVoteImages = dishVoteImageRepository.findAllByDishAndDeletedAtIsNullOrderByCreatedAtDesc(dish);
        return dishVoteImages.stream().map(DishVoteImageResponseDto::of).toList();
    }

    @Transactional
    public void createDish(Long foodId, DishServiceDto serviceDto) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        if (!food.getIsFeatured()) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        FeaturedFood featuredFood = featuredFoodRepository.findFirstByFoodOrderByStartAtDesc(food)
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        Dish dish = serviceDto.toDish(food, featuredFood);
        dishRepository.save(dish);

        DishVote dishVote = DishVote.builder()
                .dish(dish)
                .userId(1L)
                .build();
        dishVoteRepository.save(dishVote);

        List<DishVoteImage> images = IntStream.range(0, serviceDto.getImageUrls().size()).mapToObj(i -> DishVoteImage.builder()
                .dish(dish)
                .dishVote(dishVote)
                .imageUrl(serviceDto.getImageUrls().get(i))
                .orderNum(i + 1)
                .build()).collect(Collectors.toList());
        dishVoteImageRepository.saveAll(images);
    }

    @Transactional
    public void voteDish(Long id, DishVoteServiceDto serviceDto) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.DISH_NOT_FOUND));
        Food food = dish.getFood();

        DishVote dishVote = DishVote.builder()
                .dish(dish)
                .userId(1L)
                .build();
        dishVoteRepository.save(dishVote);

        List<DishVoteImage> images = IntStream.range(0, serviceDto.getImageUrls().size()).mapToObj(i -> {
            return DishVoteImage.builder()
                    .dish(dish)
                    .dishVote(dishVote)
                    .imageUrl(serviceDto.getImageUrls().get(i))
                    .orderNum(i + 1)
                    .build();
        }).collect(Collectors.toList());
        dishVoteImageRepository.saveAll(images);

        dish.plusVoteCount();
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void reportVoteImage(Long imageId) {

    }

}
