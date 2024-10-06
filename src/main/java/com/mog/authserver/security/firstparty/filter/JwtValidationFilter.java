package com.mog.authserver.security.firstparty.filter;

import com.mog.authserver.common.constant.Constant;
import com.mog.authserver.jwt.exception.TokenNotPresentException;
import com.mog.authserver.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = jwtService.getAuthentication(resolveJwtToken(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }

    private String resolveJwtToken(HttpServletRequest request){
        String bearerToken = request.getHeader(Constant.HEADER_AUTHORIZATION);
        if (bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        throw new TokenNotPresentException();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/user/sign-up") || path.equals("/user/sign-in") || path.equals("/user/refresh");
    }
}
