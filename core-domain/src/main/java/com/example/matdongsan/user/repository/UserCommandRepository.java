package com.example.matdongsan.user.repository;

import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.domain.UserAgreement;
import com.example.matdongsan.user.domain.UserProfile;

import java.util.List;

public interface UserCommandRepository {

    User save(User user);

    UserProfile saveProfile(UserProfile profile);

    List<UserAgreement> saveAllAgreements(List<UserAgreement> agreements);
}
