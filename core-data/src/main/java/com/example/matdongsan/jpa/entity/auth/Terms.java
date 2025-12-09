package com.example.matdongsan.jpa.entity.auth;

import com.example.matdongsan.auth.enums.TermsType;
import com.example.matdongsan.jpa.entity.common.BaseTimeEntity;
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
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TermsType type;

    private Integer version;

    private String title;

    private String content;

    private Boolean required;

    private Boolean active;

    private Integer orderNum = 1;

}
