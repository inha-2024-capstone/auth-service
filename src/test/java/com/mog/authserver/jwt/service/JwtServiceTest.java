package com.mog.authserver.jwt.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void 인증객체_반환() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);
        Authentication jwtServiceAuthentication = jwtService.getAuthentication(jwtToken.getAccessToken());
        AuthenticatedUserInfo principal = (AuthenticatedUserInfo) jwtServiceAuthentication.getPrincipal();
        //then
        Assertions.assertThat(principal.id()).isEqualTo(authenticatedUserInfo.id());
        Assertions.assertThat(principal.nickName()).isEqualTo(authenticatedUserInfo.nickName());
        assertIterableEquals(authorities, principal.getAuthorities());
    }

    @Test
    void 토큰_재발급() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        JwtToken jwtToken1 = jwtService.generateTokenSet(authentication);
        JwtToken jwtToken2 = jwtService.reGenerateTokenSet(jwtToken1.getRefreshToken());

        Authentication jwtServiceAuthentication1 = jwtService.getAuthentication(jwtToken1.getAccessToken());
        AuthenticatedUserInfo principal1 = (AuthenticatedUserInfo) jwtServiceAuthentication1.getPrincipal();

        Authentication jwtServiceAuthentication2 = jwtService.getAuthentication(jwtToken2.getAccessToken());
        AuthenticatedUserInfo principal2 = (AuthenticatedUserInfo) jwtServiceAuthentication2.getPrincipal();
        //then
        Assertions.assertThat(principal1.id()).isEqualTo(principal2.id());
        Assertions.assertThat(principal1.nickName()).isEqualTo(principal2.nickName());
        assertIterableEquals(principal1.getAuthorities(), principal2.getAuthorities());
    }

    @Test
    void 토큰_레디스_저장() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        JwtToken jwtToken = jwtService.generateTokenSet(authentication);
        String refreshToken = jwtToken.getRefreshToken();
        jwtService.storeRefreshToken(refreshToken);
        //then
        assertThatThrownBy(() -> jwtService.validateRefreshTokenExistence(refreshToken)).isInstanceOf(
                RuntimeException.class);
    }
}