package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.Terms;
import com.example.matdongsan.auth.mapper.AuthMapper;
import com.example.matdongsan.jpa.repository.TermsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class TermsQueryRepositoryImpl implements TermsQueryRepository {

    private final TermsJpaRepository termsJpaRepository;
    private final AuthMapper authMapper;

    @Override
    public List<Terms> findAllActive() {
        return termsJpaRepository.findAllByActiveTrueOrderByOrderNumAsc()
                .stream()
                .map(authMapper::toTermsDomain)
                .toList();
    }

    @Override
    public Set<Long> findRequiredIds() {
        return termsJpaRepository.findRequiredTermsIds();
    }

    @Override
    public List<Terms> findAllByIds(List<Long> ids) {
        return termsJpaRepository.findAllById(ids)
                .stream()
                .map(authMapper::toTermsDomain)
                .toList();
    }
}
