package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.DishVote;
import com.example.matdongsan.dish.domain.DishVoteImageReport;

public interface DishVoteCommandRepository {

    DishVote save(DishVote vote);

    DishVoteImageReport saveImageReport(DishVoteImageReport report);
}
