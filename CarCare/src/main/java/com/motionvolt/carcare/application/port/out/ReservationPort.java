package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveBooking;

import java.time.LocalDate;

public interface ReservationPort {
    boolean existsKakaoUser(long kakaoUserId);

    int saveKakaoUser(KakaoUser user);

    int saveReservation(TestDriveBooking reservation);

    boolean existsReservedSchedule(int carOptionId, LocalDate reservationDate);
}
