package com.mog.authserver.security.config;

public class SecurityApiUri {
    public static final String[] SWAGGER = {"/v3/*", "/v3/api-docs/*", "/swagger-ui/*", "/favicon.ico"};

    public static final String[] USER_PERMIT_ALL = {"/api/user/sign-up", "/api/user/refresh", "/health"};

    public static final String[] USER_AUTHENTICATED = {"/api/user/sign-in", "/api/user/test", "/api/user/info",
            "/api/user/pass-id", "/api/user/pass-info/{id}", "/api/oauth/sign-up", "/api/user/sign-out"};
}
