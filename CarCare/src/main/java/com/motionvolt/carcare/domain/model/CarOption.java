package com.motionvolt.carcare.domain.model;

public class CarOption {
    private int optionId;
    private int carId;
    private String color;
    private int cc;
    private int km;
    private String price;
    private String grade;

    public CarOption() {
    }

    public CarOption(int optionId, int carId, String color, int cc, int km, String price, String grade) {
        this.optionId = optionId;
        this.carId = carId;
        this.color = color;
        this.cc = cc;
        this.km = km;
        this.price = price;
        this.grade = grade;
    }

    public int getOptionId() {
        return optionId;
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
