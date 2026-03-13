package com.example.matdongsan.food.application.service;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVoteImage;
import com.example.matdongsan.dish.repository.DishQueryRepository;
import com.example.matdongsan.dish.repository.DishVoteQueryRepository;
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
import com.example.matdongsan.food.repository.FoodStoryQueryRepository;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserBlockQueryRepository;
import com.example.matdongsan.user.repository.UserProfileQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodService {

    private final FoodQueryRepository foodQueryRepository;
    private final FoodStoryQueryRepository foodStoryQueryRepository;
    private final DishQueryRepository dishQueryRepository;
    private final DishVoteQueryRepository dishVoteQueryRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserBlockQueryRepository userBlockQueryRepository;

    public FoodServiceDto getFoodInfoById(Long id) {
        Food food = foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        Optional<FeaturedFood> latestFf = foodQueryRepository.findLatestFeaturedFoodByFoodId(id);
        String weekText = latestFf.map(ff -> calculateWeekText(ff.getYear(), ff.getWeek())).orElse(null);
        boolean isFeatured = latestFf.map(ff -> Boolean.TRUE.equals(ff.getActive())).orElse(false);
        LocalDateTime lastFeaturedAt = latestFf.map(FeaturedFood::getStartAt).orElse(null);

        return FoodServiceDto.from(food, weekText, isFeatured, lastFeaturedAt);
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
                            List<DishVoteImage> images = dishVoteQueryRepository.findAllActiveImagesByDishId(dish.getId());
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

                            long voteCount = dishVoteQueryRepository.countVotesByDishId(dish.getId());

                            return DishServiceDto.builder()
                                    .id(dish.getId())
                                    .name(dish.getName())
                                    .thumbnailUrl(thumbnailUrl)
                                    .rank(i + 1)
                                    .voteCount((int) voteCount)
                                    .build();
                        })
                        .toList();

        long totalVoteCount = dishVoteQueryRepository.countTotalVotesByFeaturedFoodId(featuredFood.getId());

        return DishPickServiceDto.builder()
                .voteStartDate(featuredFood.getStartAt().toLocalDate())
                .voteEndDate(featuredFood.getEndAt().toLocalDate())
                .totalVoteCount((int) totalVoteCount)
                .contents(contents)
                .build();
    }

    public Page<FoodStoryServiceDto> getAllStoriesByFoodId(Long id, Long requestUserId, StoryParam param) {
        foodQueryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        List<Long> blockedUserIds = requestUserId != null
                ? userBlockQueryRepository.findBlockedUserIdsByBlockerId(requestUserId)
                : List.of();

        List<FoodStory> stories = foodStoryQueryRepository.findAllByFoodId(id, param.getType(), param.getPage(), param.getSize(), blockedUserIds);
        long totalCount = foodStoryQueryRepository.countByFoodId(id, param.getType(), blockedUserIds);

        List<FoodStoryServiceDto> dtos = stories.stream()
                .map(story -> {
                    List<FoodStoryImage> images = foodStoryQueryRepository.findAllImagesById(story.getId());
                    long likeCount = foodStoryQueryRepository.countLikesById(story.getId());
                    UserProfile profile = userProfileQueryRepository.findByUserId(story.getUserId()).orElse(null);
                    String nickname = (profile != null && profile.getNickname() != null) ? profile.getNickname() : "도란도란";
                    String profileImageUrl = (profile != null && profile.getProfileImageUrl() != null) ? profile.getProfileImageUrl() : "https://matdongsan-dev-bucket.s3.ap-northeast-2.amazonaws.com/public/profile-image/default.png";
                    return FoodStoryServiceDto.from(story, images, (int) likeCount, nickname, profileImageUrl);
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
