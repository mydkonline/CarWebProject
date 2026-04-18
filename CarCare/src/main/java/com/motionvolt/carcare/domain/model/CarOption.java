package com.motionvolt.carcare.domain.model;

public class CarOption {
    private int id;
    private int carId;
    private String color;
    private int cc;
    private int km;
    private String price;
    private String grade;

    public CarOption() {
    }

    public CarOption(int id, int carId, String color, int cc, int km, String price, String grade) {
        this.id = id;
        this.carId = carId;
        this.color = color;
        this.cc = cc;
        this.km = km;
        this.price = price;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getCar_id() {
        return carId;
    }

    public int getCarId() {
        return carId;
    }

    public String getColor() {
        return color;
    }

    public int getCc() {
        return cc;
    }

    public int getKm() {
        return km;
    }

    public String getPrice() {
        return price;
    }

    public String getGrade() {
        return grade;
    }
}
