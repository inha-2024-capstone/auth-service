package com.mog.authserver.auth.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthValidationService {
    private final AuthPersistService authPersistService;
    private final PasswordEncoder passwordEncoder;

    public Boolean doesEmailExist(String email) {
        return authPersistService.existsByEmailAndLoginSource(email, LoginSource.THIS);
    }

    public Boolean isPasswordSame(Long id, String pwd) {
        AuthEntity byId = authPersistService.findById(id);
        return passwordEncoder.matches(pwd, byId.getPassword());
    }
}
