package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.seasonaldiary.SeasonalDiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeasonalDiaryJpaRepository extends JpaRepository<SeasonalDiaryEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SeasonalDiaryEntity d SET d.stickerId = :stickerId, d.content = :content WHERE d.id = :id")
    void updateById(@Param("id") Long id,
                    @Param("stickerId") Long stickerId,
                    @Param("content") String content);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SeasonalDiaryEntity d SET d.deletedAt = CURRENT_TIMESTAMP WHERE d.id = :id")
    void softDeleteById(@Param("id") Long id);
}
