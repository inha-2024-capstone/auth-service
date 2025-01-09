package com.mog.authserver.auth.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.repository.AuthRepository;
import com.mog.authserver.user.domain.enums.LoginSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthPersistService {
    private final AuthRepository authRepository;

    public AuthEntity findById(Long id) {
        return authRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 id의 인증정보가 존재하지 않습니다."));
    }

    public AuthEntity findByEmailAndLoginSource(String email, LoginSource loginSource) {
        return authRepository.findByEmailAndLoginSource(email, loginSource)
                .orElseThrow(() -> new RuntimeException("해당 id와 LoginSource의 인증정보가 존재하지 않습니다."));
    }

    public boolean existsByEmailAndLoginSource(String email, LoginSource loginSource) {
        return authRepository.existsByEmailAndLoginSource(email, loginSource);
    }

    public AuthEntity save(AuthEntity authEntity) {
        return authRepository.save(authEntity);
    }

    public void delete(AuthEntity authEntity) {
        authRepository.delete(authEntity);
    }
}
