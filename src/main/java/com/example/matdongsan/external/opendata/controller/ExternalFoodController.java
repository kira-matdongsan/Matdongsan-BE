package com.example.matdongsan.external.opendata.controller;

import com.example.matdongsan.external.opendata.service.ExternalFoodSyncService;
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

    @PostMapping("/sync")
    public ResponseEntity<Void> syncExternalFoods() {
        syncService.syncAll();
        return ResponseEntity.ok().build();
    }

}
