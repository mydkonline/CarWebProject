package com.motionvolt.carcare.domain.model;

public class ProductOption {
    private int id;
    private int carId;
    private String brand;
    private String model;
    private String color;
    private int cc;
    private int km;
    private double price;
    private String grade;

    public ProductOption() {
    }

    public ProductOption(int id, int carId, String brand, String model, String color, int cc, int km, double price,
                         String grade) {
        this.id = id;
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.cc = cc;
        this.km = km;
        this.price = price;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
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

    public double getPrice() {
        return price;
    }

    public String getGrade() {
        return grade;
    }
}
