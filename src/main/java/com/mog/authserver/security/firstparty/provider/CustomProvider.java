package com.mog.authserver.security.firstparty.provider;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.service.AuthPersistService;
import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.enums.LoginSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomProvider implements AuthenticationProvider {
    private final AuthPersistService authPersistService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        AuthEntity authEntity = authPersistService.findByEmailAndLoginSource(email, LoginSource.THIS);
        validatePassword(pwd, authEntity.getPassword());
        AuthenticatedUserInfo authenticatedUserInfo = UserInfoMapper.toAuthenticatedUserInfo(authEntity);

        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, null, authenticatedUserInfo.authorities());
    }


    private void validatePassword(String rawPwd, String encodedPwd) {
        if (!passwordEncoder.matches(rawPwd, encodedPwd)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
