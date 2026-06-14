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
import com.example.matdongsan.seasonaldiary.repository.SeasonalDiaryCommandRepository;
import com.example.matdongsan.seasonaldiary.repository.SeasonalDiaryQueryRepository;
import com.example.matdongsan.sticker.domain.Sticker;
import com.example.matdongsan.sticker.repository.StickerQueryRepository;
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
    private final StickerQueryRepository stickerQueryRepository;
    private final FoodStoryQueryRepository foodStoryQueryRepository;
    private final FoodQueryRepository foodQueryRepository;

    private static final String[] KOR_DOW = {"월", "화", "수", "목", "금", "토", "일"};

    // ===== 스티커 팔레트 (공개) =====
    public List<StickerServiceDto> getActiveStickers() {
        return stickerQueryRepository.findAllActiveOrderByDisplayOrder()
                .stream().map(StickerServiceDto::from).toList();
    }

    // ===== CRUD =====
    @Transactional
    public Long create(Long userId, CreateSeasonalDiaryParam param) {
        requireActiveSticker(param.getStickerId());
        SeasonalDiary saved = commandRepository.save(
                SeasonalDiary.create(userId, param.getRecordDate(), param.getStickerId(), param.getContent()));
        return saved.getId();
    }

    @Transactional
    public void update(Long userId, Long id, UpdateSeasonalDiaryParam param) {
        SeasonalDiary diary = loadOwned(userId, id);
        requireActiveSticker(param.getStickerId());
        commandRepository.update(diary.getId(), param.getStickerId(), param.getContent());
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

    private void requireActiveSticker(Long stickerId) {
        Sticker sticker = stickerQueryRepository.findById(stickerId)
                .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_FOUND));
        if (!sticker.isActive()) {
            throw new CustomException(ErrorCode.STICKER_NOT_AVAILABLE);
        }
    }

    // ===== 조회 =====
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
        Map<LocalDate, Long> latestStickerIdByDate = new LinkedHashMap<>();
        for (SeasonalDiary d : queryRepository.findByUserIdAndDateBetween(userId, first, last)) {
            latestStickerIdByDate.putIfAbsent(d.getRecordDate(), d.getStickerId());
        }
        Map<Long, String> imageById = resolveStickerImages(latestStickerIdByDate.values());

        List<CalendarDiaryServiceDto.Marker> markers = latestStickerIdByDate.entrySet().stream()
                .map(e -> CalendarDiaryServiceDto.Marker.builder()
                        .date(e.getKey())
                        .stickerId(e.getValue())
                        .stickerImageUrl(imageById.get(e.getValue()))
                        .build())
                .toList();
        return CalendarDiaryServiceDto.builder().year(year).month(month).days(markers).build();
    }

    public DailyDiaryServiceDto getDaily(Long userId, LocalDate date) {
        List<SeasonalDiary> diaries = queryRepository.findByUserIdAndRecordDate(userId, date);
        Map<Long, String> imageById = resolveStickerImages(
                diaries.stream().map(SeasonalDiary::getStickerId).toList());
        List<SeasonalDiaryServiceDto> records = diaries.stream()
                .map(d -> SeasonalDiaryServiceDto.from(d, imageById.get(d.getStickerId())))
                .toList();

        List<DiaryFoodStoryServiceDto> stories = foodStoryQueryRepository
                .findByUserIdAndEffectiveDate(userId, date).stream()
                .map(this::toDiaryFoodStory).toList();

        return DailyDiaryServiceDto.builder().date(date).records(records).foodStories(stories).build();
    }

    private Map<Long, String> resolveStickerImages(java.util.Collection<Long> stickerIds) {
        return stickerQueryRepository.findAllByIdIn(stickerIds.stream().distinct().toList()).stream()
                .collect(Collectors.toMap(Sticker::getId, Sticker::getImageUrl));
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
