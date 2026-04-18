package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveReservation;

import java.time.LocalDate;

public interface ReservationPort {
    boolean existsKakaoUser(long kakaoUserId);

    int saveKakaoUser(KakaoUser user);

    int saveReservation(TestDriveReservation reservation);

    boolean existsReservedSchedule(int carOptionId, LocalDate reservationDate);
}
