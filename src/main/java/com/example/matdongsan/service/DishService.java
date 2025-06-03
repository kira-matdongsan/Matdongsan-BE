package com.example.matdongsan.service;

import com.example.matdongsan.controller.dto.DishImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DishService {

    public List<DishImageResponseDto> getAllImagesById(Long id) {
        return List.of(DishImageResponseDto.builder().build());
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void reportVoteImage(Long imageId) {

    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void voteDish(Long id) {

    }
}
