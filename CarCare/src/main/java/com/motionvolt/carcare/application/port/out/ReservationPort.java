package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveReservation;

public interface ReservationPort {
    boolean existsKakaoUser(long id);

    int saveKakaoUser(KakaoUser user);

    int saveReservation(TestDriveReservation reservation);
}
