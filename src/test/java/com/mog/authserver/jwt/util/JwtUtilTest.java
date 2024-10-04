package com.mog.authserver.jwt.util;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtill;

    @Test
    void JWT_생성_및_확인(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(1L, "kim", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);

        String jwtToken = jwtUtill.generateAccessToken(authentication);
        boolean tokenValid = jwtUtill.isTokenValid(jwtToken);

        Assertions.assertThat(tokenValid).isEqualTo(true);
    }


}