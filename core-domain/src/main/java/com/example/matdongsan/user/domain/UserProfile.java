package com.example.matdongsan.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfile {

    private Long id;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserProfile createDefault(Long userId) {
        return UserProfile.builder()
                .userId(userId)
                .nickname("행복한사자")
                .build();
    }

    public static UserProfile createOauth(Long userId, String nickname, String profileImageUrl) {
        return UserProfile.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
