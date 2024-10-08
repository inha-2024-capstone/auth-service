package com.mog.authserver.security.thirdparty.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {

    @Value("${unlink.kakao}")
    private String URL;
    private final RestClient restClient;

    @Override
    public void unlink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        restClient.post()
                .uri(URL)
                .headers(header -> header.addAll(headers))
                .retrieve()
                .toEntity(String.class);
    }
}
