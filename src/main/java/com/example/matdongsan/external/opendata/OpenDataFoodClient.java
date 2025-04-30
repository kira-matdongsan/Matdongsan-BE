package com.example.matdongsan.external.opendata;

import com.example.matdongsan.external.opendata.dto.OpenDataFoodResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "openDataFoodClient", url = "http://211.237.50.150:7080")
public interface OpenDataFoodClient {

    @GetMapping("/openapi/{apikey}/json/Grid_20171128000000000572_1/{startIdx}/{endIdx}")
    OpenDataFoodResponse fetchFoodData(
            @PathVariable("apikey") String apiKey,
            @PathVariable("startIdx") int startIdx,
            @PathVariable("endIdx") int endIdx
    );
}
