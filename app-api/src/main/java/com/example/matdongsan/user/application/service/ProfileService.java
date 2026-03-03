package com.example.matdongsan.user.application.service;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.repository.CredentialCommandRepository;
import com.example.matdongsan.auth.repository.CredentialQueryRepository;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.user.application.dto.ProfileServiceDto;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserCommandRepository;
import com.example.matdongsan.user.repository.UserProfileCommandRepository;
import com.example.matdongsan.user.repository.UserProfileQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;
    private final UserCommandRepository userCommandRepository;
    private final CredentialQueryRepository credentialQueryRepository;
    private final CredentialCommandRepository credentialCommandRepository;
    private final StringRedisTemplate redisTemplate;

    public ProfileServiceDto getProfile(Long userId) {
        UserProfile profile = userProfileQueryRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserLoginCredential credential = credentialQueryRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return ProfileServiceDto.from(profile, credential);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        userProfileQueryRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userProfileCommandRepository.updateNickname(userId, nickname);
    }

    @Transactional
    public void withdraw(Long userId) {
        userCommandRepository.softDeleteById(userId);
        userProfileCommandRepository.softDeleteByUserId(userId);
        credentialCommandRepository.softDeleteByUserId(userId);

        Set<String> keys = redisTemplate.keys("refresh:" + userId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
