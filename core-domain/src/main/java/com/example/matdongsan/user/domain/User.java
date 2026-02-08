package com.example.matdongsan.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class User {

    private Long id;
    private boolean isBlocked;
    private LocalDateTime lastLoggedInAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User create() {
        return User.builder()
                .isBlocked(false)
                .build();
    }
}
