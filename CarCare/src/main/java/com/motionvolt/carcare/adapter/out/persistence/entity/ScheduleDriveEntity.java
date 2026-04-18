package com.motionvolt.carcare.adapter.out.persistence.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_drive")
public class ScheduleDriveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "center_id", nullable = false)
    private CenterEntity center;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kakaouser_id", nullable = false)
    private KakaoUserEntity kakaoUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_option_id", nullable = false)
    private CarOptionEntity carOption;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(nullable = false)
    private boolean state;

    protected ScheduleDriveEntity() {
    }

    public ScheduleDriveEntity(CenterEntity center, KakaoUserEntity kakaoUser, CarOptionEntity carOption,
                               LocalDate reservationDate, boolean state) {
        this.center = center;
        this.kakaoUser = kakaoUser;
        this.carOption = carOption;
        this.reservationDate = reservationDate;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public KakaoUserEntity getKakaoUser() {
        return kakaoUser;
    }

    public CarOptionEntity getCarOption() {
        return carOption;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public boolean isState() {
        return state;
    }

    public void update(CarOptionEntity carOption, LocalDate reservationDate, boolean state) {
        this.carOption = carOption;
        this.reservationDate = reservationDate;
        this.state = state;
    }
}
