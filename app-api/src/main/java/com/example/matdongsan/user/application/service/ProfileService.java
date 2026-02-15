package com.example.matdongsan.user.application.service;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.user.application.dto.ProfileServiceDto;
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserCommandRepository;
import com.example.matdongsan.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserQueryRepository userQueryRepository;
    private final UserCommandRepository userCommandRepository;

    public ProfileServiceDto getProfile(Long userId) {
        UserProfile profile = userQueryRepository.findProfileByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserLoginCredential credential = userQueryRepository.findLoginCredentialByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return ProfileServiceDto.from(profile, credential);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        userQueryRepository.findProfileByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userCommandRepository.updateNickname(userId, nickname);
    }
}
