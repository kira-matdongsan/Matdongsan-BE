package com.example.matdongsan.user.mapper;

import com.example.matdongsan.jpa.entity.user.UserAgreementEntity;
import com.example.matdongsan.jpa.entity.user.UserEntity;
import com.example.matdongsan.jpa.entity.user.UserProfileEntity;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserDomain(UserEntity entity);

    @Mapping(source = "user.id", target = "userId")
    UserProfile toProfileDomain(UserProfileEntity entity);

    @Mapping(source = "termId", target = "termsId")
    UserAgreement toAgreementDomain(UserAgreementEntity entity);
}
