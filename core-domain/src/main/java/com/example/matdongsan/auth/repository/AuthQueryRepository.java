package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.Terms;
import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthQueryRepository {

    List<Terms> findAllActiveTerms();

    Set<Long> findRequiredTermsIds();

    List<Terms> findAllTermsByIds(List<Long> ids);

    Optional<UserLoginCredential> findCredentialByLoginTypeAndEmail(LoginType loginType, String email);

    boolean existsCredentialByEmail(String email);

    boolean existsCredentialByLoginTypeAndEmail(LoginType loginType, String email);
}
