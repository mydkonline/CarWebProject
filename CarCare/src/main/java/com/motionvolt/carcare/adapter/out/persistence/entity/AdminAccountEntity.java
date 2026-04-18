package com.motionvolt.carcare.adapter.out.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class AdminAccountEntity {
    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, unique = true, length = 80)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;

    protected AdminAccountEntity() {
    }

    public AdminAccountEntity(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
