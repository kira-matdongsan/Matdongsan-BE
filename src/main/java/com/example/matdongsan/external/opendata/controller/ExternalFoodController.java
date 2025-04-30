package com.example.matdongsan.external.opendata.controller;

import com.example.matdongsan.external.opendata.service.ExternalFoodSyncService;
import com.example.matdongsan.external.opendata.service.FoodGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/opendata/foods")
public class ExternalFoodController {

    private final ExternalFoodSyncService syncService;
    private final FoodGenerateService foodGenerateService;

    @PostMapping("/sync")
    public ResponseEntity<Void> syncExternalFoods() {
        syncService.syncAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateFoods() {
        foodGenerateService.generateAll();
        return ResponseEntity.ok("제철음식 데이터 생성 완료");
    }

}
