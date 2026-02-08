package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.dish.DishVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishVoteJpaRepository extends JpaRepository<DishVoteEntity, Long> {
}
