package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandRepository extends JpaRepository<CarBrandEntity, Integer> {
    List<CarBrandEntity> findAllByOrderByIdAsc();
}
