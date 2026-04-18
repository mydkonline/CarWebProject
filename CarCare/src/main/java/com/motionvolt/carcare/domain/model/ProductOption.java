package com.motionvolt.carcare.domain.model;

public class ProductOption {
    private int optionId;
    private int carId;
    private String brandName;
    private String modelName;
    private String color;
    private int cc;
    private int km;
    private double price;
    private String grade;

    public ProductOption() {
    }

    public ProductOption(int optionId, int carId, String brandName, String modelName, String color, int cc, int km, double price,
                         String grade) {
        this.optionId = optionId;
        this.carId = carId;
        this.brandName = brandName;
        this.modelName = modelName;
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

    public String getBrandName() {
        return brandName;
    }

    public String getModelName() {
        return modelName;
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
