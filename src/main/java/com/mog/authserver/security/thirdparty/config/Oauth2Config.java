package com.mog.authserver.security.thirdparty.config;

import com.mog.authserver.security.thirdparty.handler.OAuth2AuthenticationFailureHandler;
import com.mog.authserver.security.thirdparty.handler.OAuth2AuthenticationSuccessHandler;
import com.mog.authserver.security.thirdparty.requestrepository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mog.authserver.security.thirdparty.userservice.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class Oauth2Config extends AbstractHttpConfigurer<Oauth2Config, HttpSecurity> {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.oauth2Login(configure ->
                        configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                );
    }
}
