package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CenterEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.KakaoUserEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.ScheduleDriveEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CenterRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.KakaoUserRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.ScheduleDriveRepository;
import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.KakaoUser;
import com.motionvolt.carcare.domain.model.TestDriveReservation;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class JpaReservationAdapter implements ReservationPort {
    private final KakaoUserRepository kakaoUserRepository;
    private final CenterRepository centerRepository;
    private final CarOptionRepository carOptionRepository;
    private final ScheduleDriveRepository scheduleDriveRepository;

    public JpaReservationAdapter(KakaoUserRepository kakaoUserRepository,
                                 CenterRepository centerRepository,
                                 CarOptionRepository carOptionRepository,
                                 ScheduleDriveRepository scheduleDriveRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
        this.centerRepository = centerRepository;
        this.carOptionRepository = carOptionRepository;
        this.scheduleDriveRepository = scheduleDriveRepository;
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
    public int saveReservation(TestDriveReservation reservation) {
        CenterEntity center = centerRepository.getReferenceById(reservation.getCenterId());
        KakaoUserEntity kakaoUser = kakaoUserRepository.getReferenceById(reservation.getKakaoUserId());
        CarOptionEntity carOption = carOptionRepository.getReferenceById(reservation.getCarOptionId());
        scheduleDriveRepository.save(new ScheduleDriveEntity(
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
        return scheduleDriveRepository.existsByCarOption_IdAndReservationDateAndStateTrue(carOptionId, reservationDate);
    }
}
