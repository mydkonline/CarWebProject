package com.motionvolt.carcare.adapter.out.persistence.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kakaouserinfos")
public class KakaoUserEntity {
    @Id
    private Long id;

    @Column(nullable = false, length = 120)
    private String nickname;

    @Column(name = "connected_at", nullable = false)
    private Instant connectedAt;

    protected KakaoUserEntity() {
    }

    public KakaoUserEntity(Long id, String nickname, Instant connectedAt) {
        this.id = id;
        this.nickname = nickname;
        this.connectedAt = connectedAt;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
