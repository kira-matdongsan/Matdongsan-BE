package com.example.matdongsan.seasonaldiary.repository;

import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;

public interface SeasonalDiaryCommandRepository {

    SeasonalDiary save(SeasonalDiary diary);

    void update(Long id, Long stickerId, String content);

    void softDeleteById(Long id);
}
