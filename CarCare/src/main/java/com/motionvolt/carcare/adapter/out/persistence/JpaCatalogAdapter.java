package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.CarEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CenterEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CenterRepository;
import com.motionvolt.carcare.application.port.out.CatalogPort;
import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.CarSummary;
import com.motionvolt.carcare.domain.model.Center;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaCatalogAdapter implements CatalogPort {
    private final CarRepository carRepository;
    private final CarOptionRepository carOptionRepository;
    private final CenterRepository centerRepository;

    public JpaCatalogAdapter(CarRepository carRepository,
                             CarOptionRepository carOptionRepository,
                             CenterRepository centerRepository) {
        this.carRepository = carRepository;
        this.carOptionRepository = carOptionRepository;
        this.centerRepository = centerRepository;
    }

    @Override
    public List<CarSummary> findCars() {
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
    public List<Center> findCenters() {
        return centerRepository.findAllByOrderByIdAsc().stream()
                .map(this::toCenter)
                .collect(Collectors.toList());
    }

    private CarSummary toCarSummary(CarEntity car) {
        return new CarSummary(car.getId(), car.getBrand().getName(), car.getName());
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

    private Center toCenter(CenterEntity center) {
        return new Center(center.getId(), center.getName(), center.getNumber(), center.getAddress());
    }
}
