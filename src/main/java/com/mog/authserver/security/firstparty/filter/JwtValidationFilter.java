package com.mog.authserver.security.firstparty.filter;

import com.mog.authserver.common.constant.HttpConstant;
import com.mog.authserver.jwt.exception.TokenNotPresentException;
import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.config.SecurityApiUri;
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
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private String[] swagger = {"/v3/api-docs", "/swagger-ui", "/favicon.ico"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = jwtService.getAuthentication(resolveJwtToken(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }

    private String resolveJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpConstant.HEADER_AUTHORIZATION);
        if (bearerToken != null) {
            if (bearerToken.startsWith("Bearer")) {
                return bearerToken.substring(7);
            }
        }
        throw new TokenNotPresentException();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/sign-in")
                || Arrays.stream(SecurityApiUri.USER_PERMIT_ALL).anyMatch(path::startsWith)
                || Arrays.stream(SecurityApiUri.COM_PERMIT_ALL).anyMatch(path::startsWith)
                || Arrays.stream(SecurityApiUri.AUTH_PERMIT_ALL).anyMatch(path::startsWith)
                || Arrays.stream(swagger)
                .anyMatch(path::startsWith);
    }
}
