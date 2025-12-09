package com.example.matdongsan.common.util.auth;

import com.example.matdongsan.jpa.entity.LoginType;
import com.example.matdongsan.jpa.entity.UserLoginCredential;
import com.example.matdongsan.jpa.repository.UserLoginCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserLoginCredentialRepository credentialRepository;

    public CustomUserDetails loadUserByLoginTypeAndEmail(LoginType loginType, String email) {
        UserLoginCredential credential = credentialRepository.findByLoginTypeAndEmail(loginType, email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(credential.getUser(), credential);
    }
}
