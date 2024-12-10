package com.mog.authserver.security.firstparty.provider;

import com.mog.authserver.company.domain.CompanyEntity;
import com.mog.authserver.company.service.CompanyPersistService;
import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.service.UserInfoPersistService;
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
    private final UserInfoPersistService userInfoPersistService;
    private final CompanyPersistService companyPersistService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER = "1";
    private static final String COMP = "2";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        String[] split = email.split("#");
        if (split[1].equals(USER)) {
            return authenticateUser(split[0], pwd);
        } else if (split[1].equals(COMP)) {
            return authenticateComp(split[0], pwd);
        }

        throw new RuntimeException("로그인 형식에 맞지 않습니다.");
    }

    public Authentication authenticateUser(String email, String pwd) {
        UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(email, LoginSource.THIS);

        validatePassword(pwd, userInfoEntity.getPassword());

        AuthenticatedUserInfo authenticatedUserInfo = UserInfoMapper.toAuthenticatedUserInfo(userInfoEntity);
        log.info("로그인 완료, email={}, loginSource={}", userInfoEntity.getEmail(), userInfoEntity.getLoginSource());
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authenticatedUserInfo.authorities());
    }

    public Authentication authenticateComp(String email, String pwd) {
        CompanyEntity companyEntity = companyPersistService.findByEmail(email);

        validatePassword(pwd, companyEntity.getPassword());

        AuthenticatedUserInfo authenticatedUserInfo = UserInfoMapper.toAuthenticatedUserInfo(companyEntity);
        log.info("로그인 완료, email={}, company", companyEntity.getEmail());
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authenticatedUserInfo.authorities());
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
