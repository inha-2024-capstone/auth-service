package com.mog.authserver.security.thirdparty.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://kapi.kakao.com/v1/user/unlink";
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
