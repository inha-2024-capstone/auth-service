package com.mog.authserver.jwt.util;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void JWT_생성_및_확인(){
        // Given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);

        // When
        String jwtToken = jwtUtil.generateToken(authentication, 5);
        boolean tokenValid = jwtUtil.isTokenValid(jwtToken);

        // Then
        Assertions.assertThat(tokenValid).isEqualTo(true);
    }

    @Test
    void JWT_생성_후_인증객체_반환(){
        // When
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);

        // Given
        String jwtToken = jwtUtil.generateToken(authentication, 5);
        boolean tokenValid = jwtUtil.isTokenValid(jwtToken);
        Authentication jwtUtillAuthentication = jwtUtil.getAuthentication(jwtToken);

        AuthenticatedUserInfo principal = (AuthenticatedUserInfo)jwtUtillAuthentication.getPrincipal();
        // Then
        Assertions.assertThat(tokenValid).isEqualTo(true);
        Assertions.assertThat(principal.id()).isEqualTo(1L);
        Assertions.assertThat(principal.nickName()).isEqualTo("kim");
        Assertions.assertThat(principal.authorities().isEmpty()).isEqualTo(false);
    }


}