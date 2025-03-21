package com.mog.authserver.jwt.util;

import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
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
    public String generateToken(Authentication authentication, Integer minute) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if (!StringUtils.hasText(authorities)) {
            throw new RuntimeException("no authorities are given");
        }

        AuthenticatedUserInfo authenticatedUserInfo = (AuthenticatedUserInfo) authentication.getPrincipal();

        long millis = minute.longValue() * 60 * 1000;
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(new Date().getTime() + millis); // 5분
        return Jwts.builder().setSubject(String.valueOf(authenticatedUserInfo.id()))
                .claim("email", authenticatedUserInfo.email())
                .claim("name", authenticatedUserInfo.name())
                .claim("authorities", authorities)
                .setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("authorities") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                        claims.get("authorities").toString().split(",")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo(Long.valueOf(claims.getSubject()),
                claims.get("email", String.class),  claims.get("name", String.class), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUserInfo, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰 정보를 검증하는 메서드
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty.", e);
        }
        return false;
    }

}