package com.example.matdongsan.seasonaldiary.repository;

import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeasonalDiaryQueryRepository {

    Optional<SeasonalDiary> findById(Long id);

    List<SeasonalDiary> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);

    List<SeasonalDiary> findByUserIdAndRecordDate(Long userId, LocalDate date);
}
