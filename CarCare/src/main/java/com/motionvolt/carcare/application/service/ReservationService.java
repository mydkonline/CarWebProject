package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.ReservationUseCase;
import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.ReservationAvailability;
import com.motionvolt.carcare.domain.model.TestDriveReservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService implements ReservationUseCase {
    private static final int MAX_AVAILABILITY_DAYS = 31;

    private final ReservationPort reservationPort;

    public ReservationService(ReservationPort reservationPort) {
        this.reservationPort = reservationPort;
    }

    @Override
    @Transactional
    public boolean reserve(int centerId, long kakaoUserId, int carOptionId, LocalDate reservationDate) {
        if (!isAvailable(carOptionId, reservationDate)) {
            return false;
        }
        TestDriveReservation reservation = new TestDriveReservation(centerId, kakaoUserId, carOptionId, reservationDate);
        return reservationPort.saveReservation(reservation) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationAvailability> getAvailability(int carOptionId, LocalDate startDate, int days) {
        int safeDays = Math.max(1, Math.min(days, MAX_AVAILABILITY_DAYS));
        List<ReservationAvailability> availability = new ArrayList<>();

        for (int index = 0; index < safeDays; index++) {
            LocalDate date = startDate.plusDays(index);
            boolean alreadyReserved = reservationPort.existsReservedSchedule(carOptionId, date);
            boolean closed = isClosed(date);

            String reason = null;
            if (alreadyReserved) {
                reason = "예약 마감";
            } else if (closed) {
                reason = "휴무";
            }
            availability.add(new ReservationAvailability(date, !alreadyReserved && !closed, reason));
        }
        return availability;
    }

    private boolean isAvailable(int carOptionId, LocalDate date) {
        return !isClosed(date) && !reservationPort.existsReservedSchedule(carOptionId, date);
    }

    private boolean isClosed(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
