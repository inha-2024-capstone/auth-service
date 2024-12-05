package com.mog.authserver.jwt.service;


import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.exception.TokenInvalidException;
import com.mog.authserver.jwt.util.JwtUtil;
import com.mog.authserver.jwt.util.TokenExpireTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtUtil jwtUtil;
    private final TokenExpireTime tokenExpireTime;

    public JwtToken reGenerateTokenSet(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new TokenInvalidException();
        }
        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        return new JwtToken(
                jwtUtil.generateToken(authentication, tokenExpireTime.getAccessTokenExpireTime()),
                jwtUtil.generateToken(authentication, tokenExpireTime.getRefreshTokenExpireTime())
        );
    }

    public JwtToken generateTokenSet(Authentication authentication) {
        return new JwtToken(
                jwtUtil.generateToken(authentication, tokenExpireTime.getAccessTokenExpireTime()),
                jwtUtil.generateToken(authentication, tokenExpireTime.getRefreshTokenExpireTime())
        );
    }

    public Authentication getAuthentication(String token) {
        if (token == null) {
            throw new RuntimeException("Authorization 헤더가 존재하지 않습니다.");
        } else { // 토큰이 존재할 때.
            if (!jwtUtil.isTokenValid(token)) {
                throw new TokenInvalidException();
            }
            return jwtUtil.getAuthentication(token);
        }
    }

}
