package com.motionvolt.carcare.domain.model;

public class VehicleSummary {
    private int carId;
    private String brandName;
    private String modelName;

    public VehicleSummary() {
    }

    public VehicleSummary(int carId, String brandName, String modelName) {
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
