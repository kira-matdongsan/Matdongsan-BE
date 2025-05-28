package com.example.matdongsan.repository;

import com.example.matdongsan.domain.DishVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishVoteRepository extends JpaRepository<DishVote, Long> {
}
