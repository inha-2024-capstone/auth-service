package com.mog.authserver.auth.mapper;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;

public class AuthEntityMapper {

    public static AuthEntity createAuthEntity(AuthSignUpRequestDTO authSignUpRequestDTO, String encodedPwd) {
        return new AuthEntity(
                null,
                authSignUpRequestDTO.email(),
                authSignUpRequestDTO.username(),
                encodedPwd,
                authSignUpRequestDTO.role(),
                authSignUpRequestDTO.loginSource());
    }


    public static AuthEntity modifyPwd(AuthEntity authEntity, String encodedPwd) {
        return new AuthEntity(
                authEntity.getId(),
                authEntity.getEmail(),
                authEntity.getUsername(),
                encodedPwd,
                authEntity.getRole(),
                authEntity.getLoginSource());
    }
}
