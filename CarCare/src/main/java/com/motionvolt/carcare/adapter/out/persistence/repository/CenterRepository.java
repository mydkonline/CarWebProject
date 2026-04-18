package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.CenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepository extends JpaRepository<CenterEntity, Integer> {
    List<CenterEntity> findAllByOrderByIdAsc();
}
