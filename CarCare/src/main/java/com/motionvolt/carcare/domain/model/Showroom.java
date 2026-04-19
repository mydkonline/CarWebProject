package com.motionvolt.carcare.domain.model;

public class Showroom {
    private int centerId;
    private String centerName;
    private String phoneNumber;
    private String address;

    public Showroom() {
    }

    public Showroom(int centerId, String centerName, String phoneNumber, String address) {
        this.centerId = centerId;
        this.centerName = centerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getCenterId() {
        return centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
