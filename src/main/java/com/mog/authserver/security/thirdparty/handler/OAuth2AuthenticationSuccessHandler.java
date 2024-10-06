package com.mog.authserver.security.thirdparty.handler;

import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.mapper.UserInfoMapper;
import com.mog.authserver.security.thirdparty.requestrepository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mog.authserver.security.thirdparty.unlink.OAuth2UserUnlinkManager;
import com.mog.authserver.security.thirdparty.user.OAuth2Provider;
import com.mog.authserver.security.thirdparty.user.OAuth2UserInfo;
import com.mog.authserver.security.thirdparty.user.OAuth2UserPrincipal;
import com.mog.authserver.security.thirdparty.util.CookieUtils;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import com.mog.authserver.user.domain.UserInfoEntity;
import com.mog.authserver.user.domain.enums.Role;
import com.mog.authserver.user.service.UserInfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        String targetUrl = resolveTargetURI(request);
        String mode = resolveMode(request);
        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        // 실패
        if (principal != null) {
            // TODO: DB 저장
            // TODO: 액세스 토큰, 리프레시 토큰 발급
            // TODO: 리프레시 토큰 DB 저장
            if ("login".equalsIgnoreCase(mode)) {
                OAuth2UserInfo oAuth2UserInfo = principal.getUserInfo();

                log.info("oauth login succeeded with email={}, provider={}",
                        oAuth2UserInfo.getEmail(),
                        oAuth2UserInfo.getProvider()
                );

                UserInfoEntity userInfoEntity;
                userInfoEntity = userInfoService.findUserInfoByEmailAndLoginSource
                        (oAuth2UserInfo.getEmail(), UserInfoMapper.getLoginSource(oAuth2UserInfo));

                boolean isSignedUp = userInfoEntity != null;
                response.setHeader("sign-up", Boolean.toString(isSignedUp));

                if(!isSignedUp){ // 회원가입이 안 되어 있다면.
                    UserInfoEntity convertedUserInfoEntity = UserInfoMapper.toUserInfoEntity(oAuth2UserInfo);
                    userInfoEntity = userInfoService.saveUserInfo(convertedUserInfoEntity);
                }
                // Authority 객체 생성 후 넘겨줘야 함.
                Authentication usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(userInfoEntity);

                JwtToken jwtToken = jwtService.generateTokenSet(usernamePasswordAuthenticationToken);

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("Authorization", jwtToken.getAccessToken()) // Test
                        .queryParam("refresh", jwtToken.getRefreshToken())
                        .build().toUriString();
            } else if ("unlink".equalsIgnoreCase(mode)) {

                String accessToken = principal.getUserInfo().getAccessToken();
                OAuth2Provider provider = principal.getUserInfo().getProvider();

                // TODO: DB 삭제
                // TODO: 리프레시 토큰 삭제
                oAuth2UserUnlinkManager.unlink(provider, accessToken);

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .build().toUriString();
            }
        }
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UserInfoEntity userInfoEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(new AuthenticatedUserInfo(
                        userInfoEntity.getId(),
                        userInfoEntity.getEmail(),
                        authorities), "", authorities);
        return usernamePasswordAuthenticationToken;
    }

    private String resolveMode(HttpServletRequest request){
        return CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");
    }


    private String resolveTargetURI(HttpServletRequest request){
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        return redirectUri.orElse(getDefaultTargetUrl());
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

}