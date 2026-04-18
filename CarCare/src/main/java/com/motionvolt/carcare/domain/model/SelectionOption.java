package com.motionvolt.carcare.domain.model;

public class SelectionOption {
    private int id;
    private String name;

    public SelectionOption(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
