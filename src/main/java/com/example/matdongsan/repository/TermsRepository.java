package com.example.matdongsan.repository;

import com.example.matdongsan.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    List<Terms> findAllByActiveTrueOrderByOrderNumAsc();

    @Query("SELECT t.id FROM Terms t WHERE t.active = true AND t.required = true")
    Set<Long> findRequiredTermsIds();
}
