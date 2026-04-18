package com.motionvolt.carcare.domain.model;

public class Center {
    private int id;
    private String name;
    private String number;
    private String address;

    public Center() {
    }

    public Center(int id, String name, String number, String address) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }
}
