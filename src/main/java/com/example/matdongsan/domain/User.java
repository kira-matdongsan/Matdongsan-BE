package com.example.matdongsan.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class User extends BaseTimeEntityWithSoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private boolean isBlocked = false;

    private LocalDateTime lastLoggedInAt;

    @Setter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLoginCredential> loginCredentials = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAgreement> agreements = new ArrayList<>();

    public static User create() {
        return User.builder()
                .isBlocked(false)
                .build();
    }
}
