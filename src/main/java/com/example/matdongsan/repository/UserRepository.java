package com.example.matdongsan.repository;

import com.example.matdongsan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
