package com.mog.authserver.jwt.util;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public String generateAccessToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if(!StringUtils.hasText(authorities)){
            throw new RuntimeException("no authorities are given");
        }

        AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo)authentication.getPrincipal();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(new Date().getTime() + 300000); // 5분
        return Jwts.builder()
                .setSubject(String.valueOf(authenticatedUserInfo.id()))
                .claim("nickName", authenticatedUserInfo.nickName())
                .claim("authorities", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}