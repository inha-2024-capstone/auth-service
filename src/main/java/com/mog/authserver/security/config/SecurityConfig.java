package com.mog.authserver.security.config;

import com.mog.authserver.jwt.service.JwtService;
import com.mog.authserver.security.firstparty.filter.JwtGenerationFilter;
import com.mog.authserver.security.firstparty.filter.JwtValidationFilter;
import com.mog.authserver.security.thirdparty.handler.OAuth2AuthenticationFailureHandler;
import com.mog.authserver.security.thirdparty.handler.OAuth2AuthenticationSuccessHandler;
import com.mog.authserver.security.thirdparty.requestrepository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mog.authserver.security.thirdparty.userservice.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.securityContext((context) -> context
                        .requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                })).csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JwtGenerationFilter(jwtService), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtValidationFilter(jwtService), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/user/sign-up", "/user/refresh").permitAll()
                        .requestMatchers("/user/sign-in", "/user/test", "/user/info", "/user/pass-id"
                        , "/user/pass-info/{id}", "/oauth/sign-up").authenticated())
                .oauth2Login(configure ->
                        configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
