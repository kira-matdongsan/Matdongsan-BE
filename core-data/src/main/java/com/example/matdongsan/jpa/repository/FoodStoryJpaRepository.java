package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FoodStoryEntity;
import com.example.matdongsan.food.enums.FoodStoryVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodStoryJpaRepository extends JpaRepository<FoodStoryEntity, Long> {

    @Modifying
    @Query("UPDATE FoodStoryEntity s SET s.deletedAt = CURRENT_TIMESTAMP WHERE s.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE FoodStoryEntity s
            SET s.visibility = :visibility
            WHERE s.id = :id
              AND s.deletedAt IS NULL
              AND s.visibility <> :visibility
            """)
    void updateVisibilityById(@Param("id") Long id, @Param("visibility") FoodStoryVisibility visibility);
}
