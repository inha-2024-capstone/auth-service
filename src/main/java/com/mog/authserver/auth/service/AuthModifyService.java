package com.mog.authserver.auth.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.mapper.AuthEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthModifyService {
    private final AuthPersistService authPersistService;
    private final AuthValidationService authValidationService;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(Long id, String pwd) {
        AuthEntity byId = authPersistService.findById(id);
        Boolean passwordSame = authValidationService.isPasswordSame(id, pwd);
        if (passwordSame) {
            throw new RuntimeException("비밀번호가 이전과 동일합니다.");
        }
        AuthEntity authEntity = AuthEntityMapper.modifyPwd(byId, passwordEncoder.encode(pwd));
        authPersistService.save(authEntity);
    }
}
