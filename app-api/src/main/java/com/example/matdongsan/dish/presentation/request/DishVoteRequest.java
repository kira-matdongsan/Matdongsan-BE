package com.example.matdongsan.dish.presentation.request;

import com.example.matdongsan.dish.application.dto.VoteDishParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "제철요리 투표 요청 DTO")
@Builder
@Getter
public class DishVoteRequest {

    private List<String> imageUrls;

    public VoteDishParam toParam() {
        return VoteDishParam.builder()
                .imageUrls(imageUrls)
                .build();
    }
}
