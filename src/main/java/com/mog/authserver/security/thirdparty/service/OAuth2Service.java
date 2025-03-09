package com.mog.authserver.security.thirdparty.service;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.auth.service.AuthPersistService;
import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.thirdparty.user.OAuth2Provider;
import com.mog.authserver.security.thirdparty.user.OAuth2UserInfo;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.exception.UserNotFoundException;
import com.mog.authserver.user.service.UserInfoPersistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final AuthPersistService authPersistService;
    private final UserInfoPersistService userInfoPersistService;

    public void delete(OAuth2UserInfo oAuth2UserInfo) {
        LoginSource loginSource = getLoginSource(oAuth2UserInfo);
        AuthEntity authEntity = authPersistService.findByEmailAndLoginSource(oAuth2UserInfo.getEmail(), loginSource);
        authPersistService.delete(authEntity);
    }

    public boolean hasSignedIn(OAuth2UserInfo oAuth2UserInfo) {
        try {
            UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(oAuth2UserInfo.getEmail(),
                    getLoginSource(oAuth2UserInfo));
            return userInfoEntity.getPhoneNumber() != null;
        } catch (UserNotFoundException ex) {
            return false;
        }
    }

    public AuthenticatedUserInfo signIn(OAuth2UserInfo oAuth2UserInfo) {
        if (hasSignedIn(oAuth2UserInfo)) { // 로그인한 경험이 있을 경우
            AuthEntity authEntity = authPersistService.findByEmailAndLoginSource(oAuth2UserInfo.getEmail(),
                    getLoginSource(oAuth2UserInfo));
            return UserInfoMapper.toAuthenticatedUserInfo(authEntity);
        }
        // 그렇지 않을 경우
        AuthEntity authEntity = UserInfoMapper.createAuthEntity(oAuth2UserInfo);
        UserInfoEntity userEntity = UserInfoMapper.createUserEntity(oAuth2UserInfo, authEntity);
        userInfoPersistService.save(userEntity);
        return UserInfoMapper.toAuthenticatedUserInfo(authEntity);
    }

    private LoginSource getLoginSource(OAuth2UserInfo oAuth2UserInfo) {
        OAuth2Provider oAuth2Provider = oAuth2UserInfo.getProvider();
        switch (oAuth2Provider) {
            case GOOGLE -> {
                return LoginSource.GOOGLE;
            }
            case NAVER -> {
                return LoginSource.NAVER;
            }
            case KAKAO -> {
                return LoginSource.KAKAO;
            }
            default -> throw new RuntimeException("provider 가 존재하지 않습니다.");
        }
    }
}
