package com.example.matdongsan.jpa.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "user_agreement")
public class UserAgreementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime agreedAt;
    private LocalDateTime withdrawnAt;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "term_id")
    private Long termId;
}
