package com.example.matdongsan.auth.repository;

import com.example.matdongsan.auth.domain.UserLoginCredential;

public interface AuthCommandRepository {

    UserLoginCredential saveCredential(UserLoginCredential credential);
}
