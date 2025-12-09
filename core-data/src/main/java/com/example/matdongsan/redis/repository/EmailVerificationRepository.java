package com.example.matdongsan.redis.repository;

import com.example.matdongsan.redis.entity.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
}
