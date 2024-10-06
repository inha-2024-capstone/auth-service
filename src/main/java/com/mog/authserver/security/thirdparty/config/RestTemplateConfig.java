package com.mog.authserver.security.thirdparty.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestClient restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return RestClient.create();
    }
}
