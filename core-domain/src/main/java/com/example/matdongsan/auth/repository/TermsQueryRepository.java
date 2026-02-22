package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.Terms;

import java.util.List;
import java.util.Set;

public interface TermsQueryRepository {

    List<Terms> findAllActive();

    Set<Long> findRequiredIds();

    List<Terms> findAllByIds(List<Long> ids);
}
