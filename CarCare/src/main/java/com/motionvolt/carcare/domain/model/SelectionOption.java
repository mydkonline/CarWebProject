package com.motionvolt.carcare.domain.model;

public class SelectionOption {
    private int selectionId;
    private String selectionName;

    public SelectionOption(int selectionId, String selectionName) {
        this.selectionId = selectionId;
        this.selectionName = selectionName;
    }

    public int getSelectionId() {
        return selectionId;
    }

    public String getSelectionName() {
        return selectionName;
    }
}
