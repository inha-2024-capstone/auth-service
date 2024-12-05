package com.mog.authserver.security.firstparty.provider;

import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.service.UserInfoPersistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CustomProvider implements AuthenticationProvider {
    private UserInfoPersistService userInfoPersistService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(email, LoginSource.THIS);

        if(passwordEncoder.matches(pwd, userInfoEntity.getPassword())){
            AuthenticatedUserInfo authenticatedUserInfo = UserInfoMapper.toAuthenticatedUserInfo(userInfoEntity);
            log.info("로그인 완료, email={}, loginSource={}", userInfoEntity.getEmail(), userInfoEntity.getLoginSource());
            return new UsernamePasswordAuthenticationToken(
                    authenticatedUserInfo, "", authenticatedUserInfo.authorities());
        }
        else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
