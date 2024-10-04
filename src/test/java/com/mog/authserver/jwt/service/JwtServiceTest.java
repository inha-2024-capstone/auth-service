package com.mog.authserver.jwt.service;

import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {
    private static final Logger log = LoggerFactory.getLogger(JwtServiceTest.class);
    @Autowired
    private JwtService jwtService;

    @Test
    void 인증객체_반환(){
        // When
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);

        // Given
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);
        Authentication jwtServiceAuthentication = jwtService.getAuthentication(jwtToken.getAccessToken());
        AuthenticatedUserInfo principal = (AuthenticatedUserInfo)jwtServiceAuthentication.getPrincipal();

        // Then
        Assertions.assertThat(principal.id()).isEqualTo(authenticatedUserInfo.id());
        Assertions.assertThat(principal.nickName()).isEqualTo(authenticatedUserInfo.nickName());
        assertIterableEquals(authorities, principal.getAuthorities());
    }

    @Test
    void 토큰_재발급(){
        // When
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);

        // Given
        JwtToken jwtToken1 = jwtService.generateTokenSet(authentication);
        JwtToken jwtToken2 = jwtService.reGenerateTokenSet(jwtToken1.getRefreshToken());

        Authentication jwtServiceAuthentication1 = jwtService.getAuthentication(jwtToken1.getAccessToken());
        AuthenticatedUserInfo principal1 = (AuthenticatedUserInfo)jwtServiceAuthentication1.getPrincipal();

        Authentication jwtServiceAuthentication2 = jwtService.getAuthentication(jwtToken2.getAccessToken());
        AuthenticatedUserInfo principal2 = (AuthenticatedUserInfo)jwtServiceAuthentication2.getPrincipal();

        // Then
        Assertions.assertThat(principal1.id()).isEqualTo(principal2.id());
        Assertions.assertThat(principal1.nickName()).isEqualTo(principal2.nickName());
        assertIterableEquals(principal1.getAuthorities(), principal2.getAuthorities());


    }

}