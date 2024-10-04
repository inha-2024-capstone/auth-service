package com.mog.authserver.jwt.service;


import com.mog.authserver.jwt.JwtToken;
import com.mog.authserver.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtUtil jwtUtil;
    private final Integer accessTokenTime = 5;
    private final Integer refreshTokenTime = 30;
    public JwtToken reGenerateTokenSet(String refreshToken){
        if(!jwtUtil.isTokenValid(refreshToken)){
            throw new RuntimeException("유효하지 않은 토큰");
        }
        Authentication authentication = jwtUtil.getAuthentication(refreshToken);
        return new JwtToken(
                jwtUtil.generateToken(authentication, accessTokenTime),
                jwtUtil.generateToken(authentication, refreshTokenTime)
        );
    }

    public JwtToken generateTokenSet(Authentication authentication){
        return new JwtToken(
                jwtUtil.generateToken(authentication, accessTokenTime),
                jwtUtil.generateToken(authentication, refreshTokenTime)
        );
    }

    public Authentication getAuthentication(HttpServletRequest request){
        String token = resolveAccessToken(request);
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

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
