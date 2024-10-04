package com.mog.authserver.jwt.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.time")
@Getter @Setter
public class TokenExpireTime {
    private Integer accessTokenExpireTime;
    private Integer refreshTokenExpireTime;
}
