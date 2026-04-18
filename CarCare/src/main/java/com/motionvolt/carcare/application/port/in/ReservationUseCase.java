package com.motionvolt.carcare.application.port.in;

import java.time.LocalDate;

public interface ReservationUseCase {
    boolean reserve(int centerId, long kakaoUserId, int carOptionId, LocalDate reservationDate);
}
