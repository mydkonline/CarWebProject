package com.motionvolt.carcare.adapter.out.oauth;

import com.motionvolt.carcare.application.port.out.KakaoPort;
import com.motionvolt.carcare.domain.model.KakaoUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class KakaoOAuthAdapter implements KakaoPort {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String clientId;
    private final String redirectUri;

    public KakaoOAuthAdapter(@Value("${kakao.client-id}") String clientId,
                             @Value("${kakao.redirect-uri}") String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    @Override
    public KakaoUser fetchUser(String authorizationCode) {
        String accessToken = requestAccessToken(authorizationCode);
        return requestUser(accessToken);
    }

    private String requestAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        Map response = restTemplate.postForObject(
                "https://kauth.kakao.com/oauth/token",
                new HttpEntity<>(body, headers),
                Map.class
        );
        if (response == null || response.get("access_token") == null) {
            throw new IllegalStateException("Kakao access token response is empty.");
        }
        return response.get("access_token").toString();
    }

    private KakaoUser requestUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> entity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );
        Map response = entity.getBody();
        if (response == null) {
            throw new IllegalStateException("Kakao user response is empty.");
        }

        Map properties = (Map) response.get("properties");
        String nickname = properties == null ? "" : String.valueOf(properties.get("nickname"));
        long kakaoUserId = Long.parseLong(String.valueOf(response.get("id")));
        Instant connectedAt = Instant.parse(String.valueOf(response.get("connected_at")));
        return new KakaoUser(kakaoUserId, nickname, connectedAt);
    }
}
