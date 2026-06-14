package com.example.matdongsan.seasonaldiary.application.service;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.enums.FoodStoryType;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import com.example.matdongsan.food.repository.FoodStoryQueryRepository;
import com.example.matdongsan.seasonaldiary.application.dto.*;
import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import com.example.matdongsan.seasonaldiary.repository.SeasonalDiaryCommandRepository;
import com.example.matdongsan.seasonaldiary.repository.SeasonalDiaryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeasonalDiaryService {

    private final SeasonalDiaryCommandRepository commandRepository;
    private final SeasonalDiaryQueryRepository queryRepository;
    private final FoodStoryQueryRepository foodStoryQueryRepository;
    private final FoodQueryRepository foodQueryRepository;

    private static final String[] KOR_DOW = {"월", "화", "수", "목", "금", "토", "일"};

    @Transactional
    public Long create(Long userId, CreateSeasonalDiaryParam param) {
        SeasonalDiary saved = commandRepository.save(
                SeasonalDiary.create(userId, param.getRecordDate(), param.getSticker(), param.getContent()));
        return saved.getId();
    }

    @Transactional
    public void update(Long userId, Long id, UpdateSeasonalDiaryParam param) {
        SeasonalDiary diary = loadOwned(userId, id);
        commandRepository.update(diary.getId(), param.getSticker(), param.getContent());
    }

    @Transactional
    public void delete(Long userId, Long id) {
        SeasonalDiary diary = loadOwned(userId, id);
        commandRepository.softDeleteById(diary.getId());
    }

    private SeasonalDiary loadOwned(Long userId, Long id) {
        SeasonalDiary diary = queryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SEASONAL_DIARY_NOT_FOUND));
        if (!diary.isOwnedBy(userId)) {
            throw new CustomException(ErrorCode.SEASONAL_DIARY_NOT_OWNER);
        }
        return diary;
    }

    public WeeklyDiaryServiceDto getWeekly(Long userId, LocalDate baseDate) {
        LocalDate from = baseDate.minusDays(3);
        LocalDate to = baseDate.plusDays(3);
        Set<LocalDate> daysWithRecord = queryRepository.findByUserIdAndDateBetween(userId, from, to)
                .stream().map(SeasonalDiary::getRecordDate).collect(Collectors.toSet());

        List<WeeklyDiaryServiceDto.Day> days = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            days.add(WeeklyDiaryServiceDto.Day.builder()
                    .date(d)
                    .dayOfWeek(korDow(d.getDayOfWeek()))
                    .hasRecord(daysWithRecord.contains(d))
                    .build());
        }
        return WeeklyDiaryServiceDto.builder()
                .monthLabel(baseDate.getMonthValue() + "월")
                .baseDate(baseDate)
                .days(days)
                .build();
    }

    public CalendarDiaryServiceDto getCalendar(Long userId, int year, int month) {
        LocalDate first = LocalDate.of(year, month, 1);
        LocalDate last = first.withDayOfMonth(first.lengthOfMonth());
        // recordDate asc, createdAt desc → 각 날짜의 첫 항목이 최근 기록
        Map<LocalDate, SeasonalDiarySticker> latestByDate = new LinkedHashMap<>();
        for (SeasonalDiary d : queryRepository.findByUserIdAndDateBetween(userId, first, last)) {
            latestByDate.putIfAbsent(d.getRecordDate(), d.getSticker());
        }
        List<CalendarDiaryServiceDto.Marker> markers = latestByDate.entrySet().stream()
                .map(e -> CalendarDiaryServiceDto.Marker.builder()
                        .date(e.getKey()).sticker(e.getValue()).build())
                .toList();
        return CalendarDiaryServiceDto.builder().year(year).month(month).days(markers).build();
    }

    public DailyDiaryServiceDto getDaily(Long userId, LocalDate date) {
        List<SeasonalDiaryServiceDto> records = queryRepository.findByUserIdAndRecordDate(userId, date)
                .stream().map(SeasonalDiaryServiceDto::from).toList();

        List<DiaryFoodStoryServiceDto> stories = foodStoryQueryRepository
                .findByUserIdAndEffectiveDate(userId, date).stream()
                .map(this::toDiaryFoodStory).toList();

        return DailyDiaryServiceDto.builder().date(date).records(records).foodStories(stories).build();
    }

    private DiaryFoodStoryServiceDto toDiaryFoodStory(FoodStory story) {
        String foodName = foodQueryRepository.findById(story.getFoodId())
                .map(Food::getName).orElse(null);
        return DiaryFoodStoryServiceDto.builder()
                .storyId(story.getId())
                .foodId(story.getFoodId())
                .foodName(foodName)
                .type(story.getType())
                .message(message(story.getType()))
                .build();
    }

    private String message(FoodStoryType type) {
        return switch (type) {
            case RECIPE -> "레시피를 남겼어요";
            case PLACE -> "플레이스를 남겼어요";
            case SEASONAL_NOTE -> "제철기록을 남겼어요";
        };
    }

    private String korDow(DayOfWeek dow) {
        return KOR_DOW[dow.getValue() - 1];
    }
}
