package com.motionvolt.carcare.domain.model;

import java.time.Instant;

public class KakaoUser {
    private long kakaoUserId;
    private String nickname;
    private Instant connectedAt;

    public KakaoUser(long kakaoUserId, String nickname, Instant connectedAt) {
        this.kakaoUserId = kakaoUserId;
        this.nickname = nickname;
        this.connectedAt = connectedAt;
    }

    public long getKakaoUserId() {
        return kakaoUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }
}
