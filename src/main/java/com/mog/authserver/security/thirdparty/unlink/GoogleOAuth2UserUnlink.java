package com.mog.authserver.security.thirdparty.unlink;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {

    @Value("${unlink.google}")
    private String URL;
    private final RestClient restClient;

    @Override
    public void unlink(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        restClient.post()
                .uri(URL)
                .body(params)
                .retrieve()
                .toEntity(String.class);
    }
}
