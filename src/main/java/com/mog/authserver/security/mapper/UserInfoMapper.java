package com.mog.authserver.security.mapper;

import com.mog.authserver.auth.domain.AuthEntity;
import com.mog.authserver.security.thirdparty.user.OAuth2UserInfo;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.LoginSource;
import com.mog.authserver.user.domain.enums.Role;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserInfoMapper {

    public static AuthenticatedUserInfo toAuthenticatedUserInfo(AuthEntity authEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authEntity.getRole().name()));

        return new AuthenticatedUserInfo(
                authEntity.getId(),
                authEntity.getEmail(),
                authEntity.getUsername(),
                authorities);
    }


    public static AuthEntity createAuthEntity(OAuth2UserInfo oAuth2UserInfo) {
        LoginSource loginSource = getLoginSource(oAuth2UserInfo);
        return new AuthEntity(
                null,
                oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getName(),
                "oauthuser1234!",
                Role.USER,
                loginSource);

    }

    public static UserInfoEntity createUserEntity(OAuth2UserInfo oAuth2UserInfo, AuthEntity authEntity) {
        return new UserInfoEntity(
                null,
                null,
                null,
                null,
                oAuth2UserInfo.getNickname(),
                oAuth2UserInfo.getProfileImageUrl(),
                authEntity
        );

    }

    public static LoginSource getLoginSource(OAuth2UserInfo oAuth2UserInfo) {
        switch (oAuth2UserInfo.getProvider()) {
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
