package com.mog.authserver.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoValidationService {
    private final UserInfoPersistService userInfoPersistService;

    public Boolean doesNickNameExist(String nickname) {
        return userInfoPersistService.existsByNickname(nickname);
    }
}
