package com.motionvolt.carcare.adapter.out.persistence.repository;

import java.util.List;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarEntity, Integer> {
    @EntityGraph(attributePaths = "brand")
    List<CarEntity> findAllByOrderByIdAsc();
}
