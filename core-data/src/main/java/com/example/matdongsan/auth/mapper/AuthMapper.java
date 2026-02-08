package com.example.matdongsan.auth.mapper;

import com.example.matdongsan.auth.domain.Terms;
import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.jpa.entity.auth.TermsEntity;
import com.example.matdongsan.jpa.entity.auth.UserLoginCredentialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    Terms toTermsDomain(TermsEntity entity);

    @Mapping(source = "user.id", target = "userId")
    UserLoginCredential toCredentialDomain(UserLoginCredentialEntity entity);
}
