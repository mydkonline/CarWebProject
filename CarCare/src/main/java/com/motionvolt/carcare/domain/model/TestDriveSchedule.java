package com.motionvolt.carcare.domain.model;

import java.time.LocalDate;

public class TestDriveSchedule {
    private int reservationId;
    private int optionId;
    private LocalDate reservationDate;
    private String modelName;
    private String customerName;
    private int cc;
    private String color;
    private String grade;
    private int km;
    private double price;
    private States state;

    public TestDriveSchedule() {
    }

    public TestDriveSchedule(int reservationId, int optionId, LocalDate reservationDate, String modelName, String customerName, int cc, String color,
                         String grade, int km, double price, States state) {
        this.reservationId = reservationId;
        this.optionId = optionId;
        this.reservationDate = reservationDate;
        this.modelName = modelName;
        this.customerName = customerName;
        this.cc = cc;
        this.color = color;
        this.grade = grade;
        this.km = km;
        this.price = price;
        this.state = state;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getOptionId() {
        return optionId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public String getModelName() {
        return modelName;
    }

    public String getCustomerName() {
        return customerName;
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
