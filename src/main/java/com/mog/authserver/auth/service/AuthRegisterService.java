package com.mog.authserver.auth.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.dto.request.AuthSignUpRequestDTO;
import com.mog.authserver.auth.mapper.AuthEntityMapper;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthRegisterService {
    private final AuthValidationService authValidationService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthEntity signUp(AuthSignUpRequestDTO authSignUpRequestDTO) {
        if (authValidationService.doesEmailExist(authSignUpRequestDTO.email())) {
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }

        String encodedPwd = passwordEncoder.encode(authSignUpRequestDTO.rawPwd());
        return AuthEntityMapper.createAuthEntity(authSignUpRequestDTO, encodedPwd);
    }

    public void signOut(String refreshToken) {
        jwtService.storeRefreshToken(refreshToken);
    }

    public JwtToken refreshAuth(String refreshToken) {
        jwtService.validateRefreshTokenExistence(refreshToken);
        return jwtService.reGenerateTokenSet(refreshToken);
    }
}
