package com.motionvolt.carcare.adapter.out.persistence.entity;

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
@Table(name = "cars")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_brand_id", nullable = false)
    private CarBrandEntity brand;

    @Column(nullable = false, length = 120)
    private String name;

    protected CarEntity() {
    }

    public CarEntity(CarBrandEntity brand, String name) {
        this.brand = brand;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public CarBrandEntity getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }
}
