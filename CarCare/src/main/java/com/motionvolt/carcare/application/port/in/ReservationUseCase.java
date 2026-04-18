package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.ReservationAvailability;

import java.time.LocalDate;
import java.util.List;

public interface ReservationUseCase {
    boolean reserve(int centerId, long kakaoUserId, int carOptionId, LocalDate reservationDate);

    List<ReservationAvailability> getAvailability(int carOptionId, LocalDate startDate, int days);
}
