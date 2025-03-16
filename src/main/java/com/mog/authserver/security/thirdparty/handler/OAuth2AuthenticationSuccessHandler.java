package com.mog.authserver.security.thirdparty.handler;

import com.mog.authserver.common.constant.HttpConstant;
import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.thirdparty.requestrepository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mog.authserver.security.thirdparty.service.OAuth2Service;
import com.mog.authserver.security.thirdparty.unlink.OAuth2UserUnlinkManager;
import com.mog.authserver.security.thirdparty.user.OAuth2Provider;
import com.mog.authserver.security.thirdparty.user.OAuth2UserInfo;
import com.mog.authserver.security.thirdparty.user.OAuth2UserPrincipal;
import com.mog.authserver.security.thirdparty.util.CookieUtils;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        String targetUrl = resolveTargetURI(request);
        String mode = resolveMode(request);
        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal != null) {
            OAuth2UserInfo oAuth2UserInfo = principal.getUserInfo();
            if (mode.equalsIgnoreCase("login")) {

                boolean isSignedUp = oAuth2Service.hasSignedIn(oAuth2UserInfo);
                JwtToken jwtToken = signIn(oAuth2UserInfo);

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam(HttpConstant.HEADER_ACCESS_TOKEN, jwtToken.getAccessToken()) // Test
                        .queryParam(HttpConstant.HEADER_REFRESH_TOKEN, jwtToken.getRefreshToken())
                        .queryParam("sign-up", String.valueOf(isSignedUp)).build().toUriString();
            } else if (mode.equalsIgnoreCase("unlink")) {
                String accessToken = principal.getUserInfo().getAccessToken();
                OAuth2Provider provider = principal.getUserInfo().getProvider();

                oAuth2UserUnlinkManager.unlink(provider, accessToken, oAuth2UserInfo);

                return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
            }
        }
        // 실패
        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("error", "Login failed").build().toUriString();
    }

    private JwtToken signIn(OAuth2UserInfo oAuth2UserInfo) {
        log.info("oauth login succeeded with email={}, provider={}", oAuth2UserInfo.getEmail(),
                oAuth2UserInfo.getProvider());

        AuthenticatedUserInfo authenticatedUserInfo = oAuth2Service.signIn(oAuth2UserInfo);

        Authentication usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(
                authenticatedUserInfo);

        return jwtService.generateTokenSet(usernamePasswordAuthenticationToken);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
            AuthenticatedUserInfo authenticatedUserInfo) {
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authenticatedUserInfo.authorities());
    }

    private String resolveMode(HttpServletRequest request) {
        return CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue).orElse("");
    }


    private String resolveTargetURI(HttpServletRequest request) {
        Optional<String> redirectUri = CookieUtils.getCookie(request,
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        return redirectUri.orElse(getDefaultTargetUrl());
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}