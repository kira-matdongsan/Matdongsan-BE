package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.auth.TermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TermsJpaRepository extends JpaRepository<TermsEntity, Long> {

    List<TermsEntity> findAllByActiveTrueOrderByOrderNumAsc();

    @Query("SELECT t.id FROM TermsEntity t WHERE t.active = true AND t.required = true")
    Set<Long> findRequiredTermsIds();
}
