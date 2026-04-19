package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.ShowroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowroomRepository extends JpaRepository<ShowroomEntity, Integer> {
    List<ShowroomEntity> findAllByOrderByIdAsc();
}
