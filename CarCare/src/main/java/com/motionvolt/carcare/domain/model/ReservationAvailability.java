package com.motionvolt.carcare.domain.model;

import java.time.LocalDate;

public class ReservationAvailability {
    private final LocalDate date;
    private final boolean available;
    private final String reason;

    public ReservationAvailability(LocalDate date, boolean available, String reason) {
        this.date = date;
        this.available = available;
        this.reason = reason;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getReason() {
        return reason;
    }
}
