package com.mog.authserver.security.thirdparty.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String URL;
    private final RestClient restClient;

    @Override
    public void unlink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>("", headers);
        restClient.post()
                .uri(URL)
                .body(entity)
                .retrieve()
                .toEntity(String.class);
    }
}
