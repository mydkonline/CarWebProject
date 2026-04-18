package com.motionvolt.carcare.domain.model;

public class CarSummary {
    private int carId;
    private String brandName;
    private String modelName;

    public CarSummary() {
    }

    public CarSummary(int carId, String brandName, String modelName) {
        this.carId = carId;
        this.brandName = brandName;
        this.modelName = modelName;
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
}
