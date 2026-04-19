package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.ShowroomEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.KakaoUserEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.TestDriveBookingEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.ShowroomRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.KakaoUserRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.TestDriveBookingRepository;
import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveBooking;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class JpaReservationAdapter implements ReservationPort {
    private final KakaoUserRepository kakaoUserRepository;
    private final ShowroomRepository showroomRepository;
    private final CarOptionRepository carOptionRepository;
    private final TestDriveBookingRepository testDriveBookingRepository;

    public JpaReservationAdapter(KakaoUserRepository kakaoUserRepository,
                                 ShowroomRepository showroomRepository,
                                 CarOptionRepository carOptionRepository,
                                 TestDriveBookingRepository testDriveBookingRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
        this.showroomRepository = showroomRepository;
        this.carOptionRepository = carOptionRepository;
        this.testDriveBookingRepository = testDriveBookingRepository;
    }

    @Override
    public boolean existsKakaoUser(long kakaoUserId) {
        return kakaoUserRepository.existsById(kakaoUserId);
    }

    @Override
    public int saveKakaoUser(KakaoUser user) {
        kakaoUserRepository.save(new KakaoUserEntity(user.getKakaoUserId(), user.getNickname(), user.getConnectedAt()));
        return 1;
    }

    @Override
    public int saveReservation(TestDriveBooking reservation) {
        ShowroomEntity center = showroomRepository.getReferenceById(reservation.getCenterId());
        KakaoUserEntity kakaoUser = kakaoUserRepository.getReferenceById(reservation.getKakaoUserId());
        CarOptionEntity carOption = carOptionRepository.getReferenceById(reservation.getCarOptionId());
        testDriveBookingRepository.save(new TestDriveBookingEntity(
                center,
                kakaoUser,
                carOption,
                reservation.getReservationDate(),
                reservation.isState()
        ));
        return 1;
    }

    @Override
    public boolean existsReservedSchedule(int carOptionId, LocalDate reservationDate) {
        return testDriveBookingRepository.existsByCarOption_IdAndReservationDateAndStateTrue(carOptionId, reservationDate);
    }
}
