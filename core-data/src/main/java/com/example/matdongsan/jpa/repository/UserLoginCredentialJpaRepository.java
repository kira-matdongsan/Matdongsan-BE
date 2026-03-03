package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.jpa.entity.auth.UserLoginCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserLoginCredentialJpaRepository extends JpaRepository<UserLoginCredentialEntity, Long> {

    boolean existsByEmailAndDeletedAtIsNull(String email);
    boolean existsByLoginTypeAndEmailAndDeletedAtIsNull(LoginType loginType, String email);
    boolean existsByLoginTypeAndOauthIdAndDeletedAtIsNull(LoginType loginType, String oauthId);

    Optional<UserLoginCredentialEntity> findByLoginTypeAndEmailAndDeletedAtIsNull(LoginType loginType, String email);
    Optional<UserLoginCredentialEntity> findByLoginTypeAndOauthIdAndDeletedAtIsNull(LoginType loginType, String oauthId);

    Optional<UserLoginCredentialEntity> findFirstByUserIdAndDeletedAtIsNullOrderByIdDesc(Long userId);

    @Modifying
    @Query("UPDATE UserLoginCredentialEntity c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.user.id = :userId")
    void softDeleteByUserId(@Param("userId") Long userId);
}
