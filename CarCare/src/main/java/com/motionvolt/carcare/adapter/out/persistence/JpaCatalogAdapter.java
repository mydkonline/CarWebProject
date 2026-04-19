package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.ShowroomEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.ShowroomRepository;
import com.motionvolt.carcare.application.port.out.CatalogPort;
import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.VehicleSummary;
import com.motionvolt.carcare.domain.model.Showroom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaCatalogAdapter implements CatalogPort {
    private final CarRepository carRepository;
    private final CarOptionRepository carOptionRepository;
    private final ShowroomRepository showroomRepository;

    public JpaCatalogAdapter(CarRepository carRepository,
                             CarOptionRepository carOptionRepository,
                             ShowroomRepository showroomRepository) {
        this.carRepository = carRepository;
        this.carOptionRepository = carOptionRepository;
        this.showroomRepository = showroomRepository;
    }

    @Override
    public List<VehicleSummary> findCars() {
        return carRepository.findAllByOrderByIdAsc().stream()
                .map(this::toCarSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarOption> findOptions(int carId) {
        return carOptionRepository.findByCarIdOrderByIdAsc(carId).stream()
                .map(this::toCarOption)
                .collect(Collectors.toList());
    }

    @Override
    public List<Showroom> findShowrooms() {
        return showroomRepository.findAllByOrderByIdAsc().stream()
                .map(this::toShowroom)
                .collect(Collectors.toList());
    }

    private VehicleSummary toCarSummary(CarEntity car) {
        return new VehicleSummary(car.getId(), car.getBrand().getName(), car.getName());
    }

    private CarOption toCarOption(CarOptionEntity option) {
        return new CarOption(
                option.getId(),
                option.getCar().getId(),
                option.getColor(),
                option.getCc(),
                option.getKm(),
                option.getPrice().toPlainString(),
                option.getGrade()
        );
    }

    private Showroom toShowroom(ShowroomEntity center) {
        return new Showroom(center.getId(), center.getName(), center.getNumber(), center.getAddress());
    }
}
