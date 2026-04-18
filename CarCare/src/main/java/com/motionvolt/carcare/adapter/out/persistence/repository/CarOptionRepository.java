package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarOptionRepository extends JpaRepository<CarOptionEntity, Integer> {
    @EntityGraph(attributePaths = {"car", "car.brand"})
    List<CarOptionEntity> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "car")
    List<CarOptionEntity> findByCarIdOrderByIdAsc(Integer carId);
}
