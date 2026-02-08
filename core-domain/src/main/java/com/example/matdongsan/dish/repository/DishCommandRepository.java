package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVote;
import com.example.matdongsan.dish.domain.DishVoteImageReport;

public interface DishCommandRepository {

    Dish save(Dish dish);

    /**
     * DishVote와 내부 DishVoteImages를 함께 저장한다.
     */
    DishVote saveVote(DishVote vote);

    DishVoteImageReport saveVoteImageReport(DishVoteImageReport report);
}
