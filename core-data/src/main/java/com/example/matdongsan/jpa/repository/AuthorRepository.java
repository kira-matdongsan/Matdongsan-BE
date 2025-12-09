package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.dummy.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
