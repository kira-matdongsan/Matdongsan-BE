package com.example.matdongsan.home.application.service;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import com.example.matdongsan.home.application.dto.HomeServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HomeService {

    private final FoodQueryRepository foodQueryRepository;

    public HomeServiceDto getHome() {
        String weekText = calculateWeekText(LocalDate.now());

        Food food = foodQueryRepository.findCurrentFeaturedFood()
                .orElseThrow(() -> new CustomException(ErrorCode.FEATURED_FOOD_NOT_FOUND));

        return HomeServiceDto.builder()
                .weekText(weekText)
                .foodId(food.getId())
                .name(food.getName())
                .subtitle(food.getSubtitle())
                .thumbnail(food.getImageUrl())
                .build();
    }

    private String calculateWeekText(LocalDate date) {
        // ISO 8601: 목요일이 속한 달을 기준으로 주차 결정
        LocalDate thursday = date.with(DayOfWeek.THURSDAY);
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

        return month + "월 " + weekOrdinal;
    }
}
