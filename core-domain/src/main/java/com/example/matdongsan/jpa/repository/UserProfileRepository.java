package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
