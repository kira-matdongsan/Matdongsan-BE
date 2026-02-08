package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.Terms;
import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.auth.mapper.AuthMapper;
import com.example.matdongsan.jpa.repository.TermsJpaRepository;
import com.example.matdongsan.jpa.repository.UserLoginCredentialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class AuthQueryRepositoryImpl implements AuthQueryRepository {

    private final TermsJpaRepository termsJpaRepository;
    private final UserLoginCredentialJpaRepository credentialJpaRepository;
    private final AuthMapper authMapper;

    @Override
    public List<Terms> findAllActiveTerms() {
        return termsJpaRepository.findAllByActiveTrueOrderByOrderNumAsc()
                .stream()
                .map(authMapper::toTermsDomain)
                .toList();
    }

    @Override
    public Set<Long> findRequiredTermsIds() {
        return termsJpaRepository.findRequiredTermsIds();
    }

    @Override
    public List<Terms> findAllTermsByIds(List<Long> ids) {
        return termsJpaRepository.findAllById(ids)
                .stream()
                .map(authMapper::toTermsDomain)
                .toList();
    }

    @Override
    public Optional<UserLoginCredential> findCredentialByLoginTypeAndEmail(LoginType loginType, String email) {
        return credentialJpaRepository.findByLoginTypeAndEmail(loginType, email)
                .map(authMapper::toCredentialDomain);
    }

    @Override
    public boolean existsCredentialByEmail(String email) {
        return credentialJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsCredentialByLoginTypeAndEmail(LoginType loginType, String email) {
        return credentialJpaRepository.existsByLoginTypeAndEmail(loginType, email);
    }
}
