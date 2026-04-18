package com.motionvolt.carcare.adapter.out.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "car_options")
public class CarOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @Column(nullable = false, length = 120)
    private String color;

    @Column(nullable = false)
    private int cc;

    @Column(nullable = false)
    private int km;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 120)
    private String grade;

    protected CarOptionEntity() {
    }

    public CarOptionEntity(CarEntity car, String color, int cc, int km, BigDecimal price, String grade) {
        this.car = car;
        this.color = color;
        this.cc = cc;
        this.km = km;
        this.price = price;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public CarEntity getCar() {
        return car;
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

    public BigDecimal getPrice() {
        return price;
    }

    public String getGrade() {
        return grade;
    }
}
