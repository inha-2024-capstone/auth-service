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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.securityContext((context) -> context
                        .requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                        corsConfigurationSource)).csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JwtGenerationFilter(jwtService), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtValidationFilter(jwtService), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(SecurityApiUri.USER_PERMIT_ALL).permitAll()
                        .requestMatchers(SecurityApiUri.SWAGGER).permitAll()
                        .requestMatchers(SecurityApiUri.COM_PERMIT_ALL).permitAll()
                        .requestMatchers(SecurityApiUri.AUTH_PERMIT_ALL).permitAll()
                        .requestMatchers(SecurityApiUri.USER_AUTHENTICATED).authenticated()
                        .requestMatchers(SecurityApiUri.COM_AUTHENTICATED).authenticated()
                        .requestMatchers(SecurityApiUri.AUTH_AUTHENTICATED).authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .oauth2Login(configure ->
                        configure.authorizationEndpoint(config -> config.authorizationRequestRepository(
                                        httpCookieOAuth2AuthorizationRequestRepository))
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
