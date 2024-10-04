package com.mog.authserver.jwt.service;


import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.util.JwtUtil;
import com.mog.authserver.jwt.util.TokenExpireTime;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtUtil jwtUtil;
    private final TokenExpireTime tokenExpireTime;
    public JwtToken reGenerateTokenSet(String refreshToken){
        if(!jwtUtil.isTokenValid(refreshToken)){
            throw new RuntimeException("유효하지 않은 토큰");
        }
        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        return new JwtToken(
                jwtUtil.generateToken(authentication, tokenExpireTime.getAccessTokenExpireTime()),
                jwtUtil.generateToken(authentication, tokenExpireTime.getRefreshTokenExpireTime())
        );
    }

    public JwtToken generateTokenSet(Authentication authentication){
        return new JwtToken(
                jwtUtil.generateToken(authentication, tokenExpireTime.getAccessTokenExpireTime()),
                jwtUtil.generateToken(authentication, tokenExpireTime.getRefreshTokenExpireTime())
        );
    }

    public Authentication getAuthentication(String token){
        if(token == null){
            throw new RuntimeException("Authorization is not present in the header");
        }
        else { // 토큰이 존재할 때.
            if (!jwtUtil.isTokenValid(token)) {
                throw new RuntimeException("Access Token is not valid");
            }
            return jwtUtil.getAuthentication(token);
        }
    }

}
