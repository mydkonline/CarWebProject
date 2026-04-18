package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.ScheduleDriveEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDriveRepository extends JpaRepository<ScheduleDriveEntity, Integer> {
    @EntityGraph(attributePaths = {"kakaoUser", "carOption", "carOption.car"})
    List<ScheduleDriveEntity> findAllByOrderByIdAsc();

    boolean existsByCarOption_IdAndReservationDateAndStateTrue(Integer optionId, LocalDate reservationDate);
}
