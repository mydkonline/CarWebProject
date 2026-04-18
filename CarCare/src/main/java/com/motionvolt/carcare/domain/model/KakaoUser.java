package com.motionvolt.carcare.domain.model;

import java.time.Instant;

public class KakaoUser {
    private long id;
    private String nickname;
    private Instant connectedAt;

    public KakaoUser(long id, String nickname, Instant connectedAt) {
        this.id = id;
        this.nickname = nickname;
        this.connectedAt = connectedAt;
    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }
}
