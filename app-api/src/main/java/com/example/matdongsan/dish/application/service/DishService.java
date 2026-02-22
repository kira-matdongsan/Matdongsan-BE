package com.example.matdongsan.dish.application.service;

import com.example.matdongsan.dish.application.dto.CreateDishParam;
import com.example.matdongsan.dish.application.dto.DishVoteImageServiceDto;
import com.example.matdongsan.dish.application.dto.VoteDishParam;
import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVote;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.repository.DishCommandRepository;
import com.example.matdongsan.dish.repository.DishQueryRepository;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DishService {

    private final FoodQueryRepository foodQueryRepository;
    private final DishCommandRepository dishCommandRepository;
    private final DishQueryRepository dishQueryRepository;

    public List<DishVoteImageServiceDto> getAllImagesById(Long id) {
        Dish dish = dishQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DISH_NOT_FOUND));

        List<DishVoteImage> dishVoteImages = dishQueryRepository.findAllActiveImagesByDishId(dish.getId());
        return dishVoteImages.stream().map(DishVoteImageServiceDto::from).toList();
    }

    @Transactional
    public void createDish(Long foodId, Long userId, CreateDishParam param) {
        FeaturedFood featuredFood = foodQueryRepository.findActiveFeaturedFoodByFoodId(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        Dish dish = Dish.create(featuredFood.getId(), param.getName());
        Dish savedDish = dishCommandRepository.save(dish);

        DishVote vote = DishVote.create(savedDish.getId(), userId, param.getImageUrls());
        dishCommandRepository.saveVote(vote);
    }

    @Transactional
    public void voteDish(Long id, Long userId, VoteDishParam param) {
        Dish dish = dishQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DISH_NOT_FOUND));

        DishVote vote = DishVote.create(dish.getId(), userId, param.getImageUrls());
        dishCommandRepository.saveVote(vote);
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void reportVoteImage(Long imageId) {

    }
}
