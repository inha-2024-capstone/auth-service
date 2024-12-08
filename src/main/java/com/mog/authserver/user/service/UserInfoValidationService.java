package com.mog.authserver.user.service;

import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoValidationService {
    private final UserInfoPersistService userInfoPersistService;
    private final PasswordEncoder passwordEncoder;

    public Boolean validateDuplicateEmail(String email) {
        return userInfoPersistService.existsByEmailAndLoginSource(email, LoginSource.THIS);
    }

    public Boolean doesEmailExist(String email) {
        return userInfoPersistService.existsByEmailAndLoginSource(email, LoginSource.THIS);
    }

    public Boolean doesNickNameExist(String nickname) {
        return userInfoPersistService.existsByNickname(nickname);
    }

    public Boolean validateSamePassword(Long id, String password) {
        UserInfoEntity findById = userInfoPersistService.findById(id);
        return passwordEncoder.matches(password, findById.getPassword());
    }
}
