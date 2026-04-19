package com.motionvolt.carcare.domain.model;

import java.time.LocalDate;

public class TestDriveBooking {
    
    private int centerId;
    private long kakaoUserId;
    private int carOptionId;
    private LocalDate reservationDate;
    private boolean state;

    public TestDriveBooking(int centerId, long kakaoUserId, int carOptionId, LocalDate reservationDate) {
        this.centerId = centerId;
        this.kakaoUserId = kakaoUserId;
        this.carOptionId = carOptionId;
        this.reservationDate = reservationDate;
        this.state = true;
    }

    public int getCenterId() {
        return centerId;
    }

    public long getKakaoUserId() {
        return kakaoUserId;
    }

    public int getCarOptionId() {
        return carOptionId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public boolean isState() {
        return state;
    }
}
