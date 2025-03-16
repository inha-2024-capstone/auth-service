package com.mog.authserver.jwt.util;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil("2e158dca3324bbf4491a7ca7435f16c5df6bac981cc9865683ae47a2f18254e8");

    @Test
    void JWT_생성_및_확인() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "example@example.com", "kim",
                authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        String jwtToken = jwtUtil.generateToken(authentication, 5);
        boolean tokenValid = jwtUtil.isTokenValid(jwtToken);
        //then
        Assertions.assertThat(tokenValid).isEqualTo(true);
    }

    @Test
    void JWT_생성_후_인증객체_반환() {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "example@example.com", "kim",
                authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
        //when
        String jwtToken = jwtUtil.generateToken(authentication, 5);
        boolean tokenValid = jwtUtil.isTokenValid(jwtToken);
        Authentication jwtUtillAuthentication = jwtUtil.getAuthentication(jwtToken);
        AuthenticatedUserInfo principal = (AuthenticatedUserInfo) jwtUtillAuthentication.getPrincipal();
        //then
        Assertions.assertThat(tokenValid).isEqualTo(true);
        Assertions.assertThat(principal.id()).isEqualTo(1L);
        Assertions.assertThat(principal.name()).isEqualTo("kim");
        Assertions.assertThat(principal.authorities().isEmpty()).isEqualTo(false);
    }
}