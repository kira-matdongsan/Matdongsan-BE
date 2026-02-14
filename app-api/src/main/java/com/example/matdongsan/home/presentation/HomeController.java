package com.example.matdongsan.home.presentation;

import com.example.matdongsan.home.application.service.HomeService;
import com.example.matdongsan.home.presentation.response.HomeResponse;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "홈 API", description = "홈 화면 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "홈 화면 조회", description = "홈 화면에 필요한 배너 및 제철 음식 정보 조회")
    @GetMapping
    public ResponseEntity<RestApiResponse<HomeResponse>> getHome() {
        return RestApiResponse.success(ResponseCode.OK, HomeResponse.from(homeService.getHome()));
    }
}
