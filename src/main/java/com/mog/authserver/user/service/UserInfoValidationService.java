package com.mog.authserver.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager", readOnly = true)
public class UserInfoValidationService {
    private final UserInfoPersistService userInfoPersistService;

    public Boolean doesNickNameExist(String nickname) {
        return userInfoPersistService.existsByNickname(nickname);
    }
}
