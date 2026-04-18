package com.motionvolt.carcare.domain.model;

import java.time.LocalDate;

public class DriveSchedule {
    private int id;
    private int carId;
    private LocalDate date;
    private String model;
    private String name;
    private int cc;
    private String color;
    private String grade;
    private int km;
    private double price;
    private States state;

    public DriveSchedule() {
    }

    public DriveSchedule(int id, int carId, LocalDate date, String model, String name, int cc, String color,
                         String grade, int km, double price, States state) {
        this.id = id;
        this.carId = carId;
        this.date = date;
        this.model = model;
        this.name = name;
        this.cc = cc;
        this.color = color;
        this.grade = grade;
        this.km = km;
        this.price = price;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public int getCc() {
        return cc;
    }

    public String getColor() {
        return color;
    }

    public String getGrade() {
        return grade;
    }

    public int getKm() {
        return km;
    }

    public double getPrice() {
        return price;
    }

    public States getState() {
        return state;
    }

    public enum States {
        RESERVED(true),
        FAILED(false);

        private final boolean value;

        States(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }
}
