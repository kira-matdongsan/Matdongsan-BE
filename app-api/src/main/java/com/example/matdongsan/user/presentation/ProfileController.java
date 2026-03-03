package com.example.matdongsan.user.presentation;

import com.example.matdongsan.common.util.auth.CurrentUser;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.user.application.service.ProfileService;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.presentation.request.NicknameRequest;
import com.example.matdongsan.user.presentation.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로필 API", description = "프로필 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "나의 프로필 조회", description = "로그인한 사용자의 프로필 정보 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<RestApiResponse<ProfileResponse>> getProfile(
            @CurrentUser User user
    ) {
        return RestApiResponse.success(ResponseCode.OK, ProfileResponse.from(profileService.getProfile(user.getId())));
    }

    @Operation(summary = "닉네임 변경", description = "로그인한 사용자의 닉네임 변경")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/nickname")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> updateNickname(
            @CurrentUser User user,
            @RequestBody @Valid NicknameRequest request
    ) {
        profileService.updateNickname(user.getId(), request.getNickname());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 계정을 탈퇴 처리")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> withdraw(
            @CurrentUser User user
    ) {
        profileService.withdraw(user.getId());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }
}
