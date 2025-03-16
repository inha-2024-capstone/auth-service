package com.mog.authserver.security.config;

public class SecurityApiUri {
    public static final String[] SWAGGER = {"/v3/*", "/v3/api-docs/*", "/swagger-ui/*", "/favicon.ico"};

    public static final String[] AUTH_PERMIT_ALL = {"/api/auth/refresh", "/api/auth/email"};

    public static final String[] AUTH_AUTHENTICATED = {
            "/api/auth/sign-out", "/api/auth/sign-in",
            "/api/auth/pass-id", "/api/auth/pwd"};

    public static final String[] USER_PERMIT_ALL = {
            "/api/user/sign-up", "/api/auth/refresh",
            "/api/user/pass/**", "/api/user/pass", "/health"};

    public static final String[] USER_AUTHENTICATED = {
            "/api/auth/sign-in", "/api/user/test", "/api/user/auth-info",
            "/api/user/pass-info/{id}", "/api/oauth/sign-up", "/api/user/nickname", "/api/user/image"};

    public static final String[] COM_PERMIT_ALL = {
            "/api/company/sign-up", "/api/company/infos", "/api/company/info/**",
            "/api/company/info", "/api/company/pass", "/api/company/pass/**"};

    public static final String[] COM_AUTHENTICATED = {
            "/api/company/auth-info", "/api/company/description",
            "/api/company/short-description", "/api/company/image"};
}
