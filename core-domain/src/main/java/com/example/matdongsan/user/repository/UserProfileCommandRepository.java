package com.example.matdongsan.user.repository;

import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;

import java.util.List;

public interface UserProfileCommandRepository {

    UserProfile save(UserProfile profile);

    List<UserAgreement> saveAllAgreements(List<UserAgreement> agreements);

    void updateNickname(Long userId, String nickname);
}
