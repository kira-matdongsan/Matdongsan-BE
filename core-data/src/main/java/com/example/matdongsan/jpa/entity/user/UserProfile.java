package com.example.matdongsan.jpa.entity.user;

import com.example.matdongsan.jpa.entity.common.BaseTimeEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserProfile extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String profileImageUrl;

    @OneToOne
    private User user;

    public static UserProfile createDefault(User user) {
        return UserProfile.builder()
                .user(user)
                .nickname("행복한사자")
                .build();
    }

    public static UserProfile createOauth(User user, String nickname, String profileImageUrl) {
        return UserProfile.builder()
                .user(user)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
