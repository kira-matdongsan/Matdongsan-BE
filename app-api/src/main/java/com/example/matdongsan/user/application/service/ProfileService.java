package com.example.matdongsan.user.application.service;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.repository.CredentialQueryRepository;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.user.application.dto.ProfileServiceDto;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserProfileCommandRepository;
import com.example.matdongsan.user.repository.UserProfileQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;
    private final CredentialQueryRepository credentialQueryRepository;

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
}
