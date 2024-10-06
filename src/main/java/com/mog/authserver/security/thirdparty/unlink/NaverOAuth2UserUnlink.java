package com.mog.authserver.security.thirdparty.unlink;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String URL;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final RestClient restClient;

    @Override
    public void unlink(String accessToken) {

        String url = URL +
                "?service_provider=NAVER" +
                "&grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken;

        ResponseEntity<UnlinkResponse> entity = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(UnlinkResponse.class);
        UnlinkResponse response = entity.getBody();

        if (response != null && !response.getResult().equalsIgnoreCase("success")) {
            throw new RuntimeException("Failed to Naver Unlink");
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse {
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}
