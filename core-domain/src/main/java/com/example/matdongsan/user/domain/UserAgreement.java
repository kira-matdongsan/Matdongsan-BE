package com.example.matdongsan.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAgreement {

    private Long id;
    private Long userId;
    private Long termsId;
    private LocalDateTime agreedAt;
    private LocalDateTime withdrawnAt;

    public static UserAgreement create(Long userId, Long termsId) {
        return UserAgreement.builder()
                .userId(userId)
                .termsId(termsId)
                .agreedAt(LocalDateTime.now())
                .build();
    }
}
