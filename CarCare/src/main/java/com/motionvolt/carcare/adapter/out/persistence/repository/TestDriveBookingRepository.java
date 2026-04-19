package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.TestDriveBookingEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDriveBookingRepository extends JpaRepository<TestDriveBookingEntity, Integer> {
    @EntityGraph(attributePaths = {"kakaoUser", "carOption", "carOption.car"})
    List<TestDriveBookingEntity> findAllByOrderByIdAsc();

    boolean existsByCarOption_IdAndReservationDateAndStateTrue(Integer optionId, LocalDate reservationDate);
}
