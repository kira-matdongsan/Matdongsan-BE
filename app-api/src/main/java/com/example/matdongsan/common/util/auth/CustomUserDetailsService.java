package com.example.matdongsan.common.util.auth;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.auth.repository.AuthQueryRepository;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final AuthQueryRepository authQueryRepository;
    private final UserQueryRepository userQueryRepository;

    public CustomUserDetails loadUserByLoginTypeAndEmail(LoginType loginType, String email) {
        UserLoginCredential credential = authQueryRepository.findCredentialByLoginTypeAndEmail(loginType, email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 사용자를 찾을 수 없습니다."));

        User user = userQueryRepository.findById(credential.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(user, credential);
    }
}
