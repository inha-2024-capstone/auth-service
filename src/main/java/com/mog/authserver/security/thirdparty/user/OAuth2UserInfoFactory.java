package com.mog.authserver.security.thirdparty.user;

import com.mog.authserver.security.thirdparty.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken,
                                                   Map<String, Object> attributes) {
        if (registrationId.equals(OAuth2Provider.GOOGLE.getRegistrationId())) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else if (registrationId.equals(OAuth2Provider.NAVER.getRegistrationId())) {
            return new NaverOAuth2UserInfo(accessToken, attributes);
        } else if (registrationId.equals(OAuth2Provider.KAKAO.getRegistrationId())) {
            return new KakaoOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
