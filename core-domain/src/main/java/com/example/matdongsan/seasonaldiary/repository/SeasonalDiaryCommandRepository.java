package com.example.matdongsan.seasonaldiary.repository;

import com.example.matdongsan.seasonaldiary.domain.SeasonalDiary;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;

public interface SeasonalDiaryCommandRepository {

    SeasonalDiary save(SeasonalDiary diary);

    void update(Long id, SeasonalDiarySticker sticker, String content);

    void softDeleteById(Long id);
}
