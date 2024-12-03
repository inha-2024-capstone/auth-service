package com.mog.authserver.security.thirdparty.service;

import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.thirdparty.user.OAuth2Provider;
import com.mog.authserver.security.thirdparty.user.OAuth2UserInfo;
import com.mog.authserver.security.thirdparty.vo.UserJwtInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.service.UserInfoPersistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2Service {
    private final UserInfoPersistService userInfoPersistService;

    public void delete(OAuth2UserInfo oAuth2UserInfo) {
        LoginSource loginSource = getLoginSource(oAuth2UserInfo);
        userInfoPersistService.findByEmailAndLoginSource(oAuth2UserInfo.getEmail(), loginSource);
    }

    public boolean hasSignedIn(OAuth2UserInfo oAuth2UserInfo) {
        return userInfoPersistService.existsByEmailAndLoginSource(oAuth2UserInfo.getEmail(),
                getLoginSource(oAuth2UserInfo));
    }

    public UserJwtInfo signIn(OAuth2UserInfo oAuth2UserInfo) {
        if (hasSignedIn(oAuth2UserInfo)) {
            UserInfoEntity userInfoEntity = userInfoPersistService.findByEmailAndLoginSource(
                    oAuth2UserInfo.getEmail(),
                    getLoginSource(oAuth2UserInfo));
            return UserJwtInfo.from(userInfoEntity);
        }
        UserInfoEntity userInfoEntity = UserInfoMapper.toUserInfoEntity(oAuth2UserInfo);
        UserInfoEntity savedUserInfoEntity = userInfoPersistService.save(userInfoEntity);
        return UserJwtInfo.from(savedUserInfoEntity);
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
