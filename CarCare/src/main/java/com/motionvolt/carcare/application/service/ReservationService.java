package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.ReservationUseCase;
import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.TestDriveReservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ReservationService implements ReservationUseCase {
    private final ReservationPort reservationPort;

    public ReservationService(ReservationPort reservationPort) {
        this.reservationPort = reservationPort;
    }

    @Override
    @Transactional
    public boolean reserve(int centerId, long kakaoUserId, int carOptionId, LocalDate reservationDate) {
        TestDriveReservation reservation = new TestDriveReservation(centerId, kakaoUserId, carOptionId, reservationDate);
        return reservationPort.saveReservation(reservation) > 0;
    }
}
