package com.example.matdongsan.external.opendata.service;

import com.example.matdongsan.domain.ExternalFoodSource;
import com.example.matdongsan.external.opendata.OpenDataFoodClient;
import com.example.matdongsan.external.opendata.OpenDataFoodMapper;
import com.example.matdongsan.external.opendata.dto.OpenDataFoodResponse;
import com.example.matdongsan.repository.ExternalFoodSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExternalFoodSyncService {

    private final OpenDataFoodClient openDataFoodClient;
    private final OpenDataFoodMapper openDataFoodMapper;
    private final ExternalFoodSourceRepository externalFoodSourceRepository;

    @Value("${external.opendata.api-key}")
    private String apiKey;

    public void syncAll() {
        int pageSize = 100;
        int total = openDataFoodClient.fetchFoodData(apiKey, 1, 1).getData().getTotalCnt();

        for (int i = 1; i <= total; i += pageSize) {
            int end = Math.min(i + pageSize - 1, total);
            OpenDataFoodResponse response = openDataFoodClient.fetchFoodData(apiKey, i, end);

            List<ExternalFoodSource> entities = response.getData().getRow().stream()
                    .map(openDataFoodMapper::toEntity)
                    .toList();

            externalFoodSourceRepository.saveAll(entities);
        }
    }
}
