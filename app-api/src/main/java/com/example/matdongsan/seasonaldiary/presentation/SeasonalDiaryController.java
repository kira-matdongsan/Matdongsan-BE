package com.example.matdongsan.seasonaldiary.presentation;

import com.example.matdongsan.common.util.auth.CurrentUser;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.seasonaldiary.application.service.SeasonalDiaryService;
import com.example.matdongsan.seasonaldiary.presentation.request.CreateSeasonalDiaryRequest;
import com.example.matdongsan.seasonaldiary.presentation.request.UpdateSeasonalDiaryRequest;
import com.example.matdongsan.seasonaldiary.presentation.response.CalendarDiaryResponse;
import com.example.matdongsan.seasonaldiary.presentation.response.DailyDiaryResponse;
import com.example.matdongsan.seasonaldiary.presentation.response.WeeklyDiaryResponse;
import com.example.matdongsan.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "제철기록장 / 캘린더 API", description = "비공개 제철음식 기록 및 캘린더 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seasonal-diaries")
public class SeasonalDiaryController {

    private final SeasonalDiaryService service;

    @Operation(summary = "제철기록 생성", description = "스티커와 내용으로 제철음식 기록 생성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<RestApiResponse<ResultResponse<Long>>> create(
            @CurrentUser User user,
            @Valid @RequestBody CreateSeasonalDiaryRequest request
    ) {
        Long id = service.create(user.getId(), request.toParam());
        return RestApiResponse.successResult(ResponseCode.CREATED, id);
    }

    @Operation(summary = "제철기록 수정", description = "본인이 작성한 기록의 스티커/내용 수정")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> update(
            @CurrentUser User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeasonalDiaryRequest request
    ) {
        service.update(user.getId(), id, request.toParam());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "제철기록 삭제", description = "본인이 작성한 기록 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> delete(
            @CurrentUser User user,
            @PathVariable Long id
    ) {
        service.delete(user.getId(), id);
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "홈 제철기록장", description = "기준일 ±3일(7일) 스트립과 날짜별 기록 유무")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/weekly")
    public ResponseEntity<RestApiResponse<WeeklyDiaryResponse>> weekly(
            @CurrentUser User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDate base = (date != null) ? date : LocalDate.now();
        return RestApiResponse.success(ResponseCode.OK,
                WeeklyDiaryResponse.from(service.getWeekly(user.getId(), base)));
    }

    @Operation(summary = "캘린더 월별 마커", description = "해당 년월에 기록이 있는 날짜와 최근 스티커")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/calendar")
    public ResponseEntity<RestApiResponse<CalendarDiaryResponse>> calendar(
            @CurrentUser User user,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return RestApiResponse.success(ResponseCode.OK,
                CalendarDiaryResponse.from(service.getCalendar(user.getId(), year, month)));
    }

    @Operation(summary = "선택 날짜 상세", description = "선택 날짜의 기록 + 나의 제철음식 이야기(레시피/플레이스/제철기록)")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/daily")
    public ResponseEntity<RestApiResponse<DailyDiaryResponse>> daily(
            @CurrentUser User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return RestApiResponse.success(ResponseCode.OK,
                DailyDiaryResponse.from(service.getDaily(user.getId(), date)));
    }
}
