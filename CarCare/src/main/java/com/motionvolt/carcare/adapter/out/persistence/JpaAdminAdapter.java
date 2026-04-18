package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.AdminAccountEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarBrandEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.ScheduleDriveEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.AdminAccountRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarBrandRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.ScheduleDriveRepository;
import com.motionvolt.carcare.application.port.out.AdminPort;
import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
import com.motionvolt.carcare.domain.model.SelectionOption;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaAdminAdapter implements AdminPort {
    private final AdminAccountRepository adminAccountRepository;
    private final ScheduleDriveRepository scheduleDriveRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarRepository carRepository;
    private final CarOptionRepository carOptionRepository;

    public JpaAdminAdapter(AdminAccountRepository adminAccountRepository,
                           ScheduleDriveRepository scheduleDriveRepository,
                           CarBrandRepository carBrandRepository,
                           CarRepository carRepository,
                           CarOptionRepository carOptionRepository) {
        this.adminAccountRepository = adminAccountRepository;
        this.scheduleDriveRepository = scheduleDriveRepository;
        this.carBrandRepository = carBrandRepository;
        this.carRepository = carRepository;
        this.carOptionRepository = carOptionRepository;
    }

    @Override
    public boolean existsAdmin(String username, String password) {
        return adminAccountRepository.existsByUsernameAndPassword(username, password);
    }

    @Override
    public int createAdmin(String id, String username, String password) {
        adminAccountRepository.save(new AdminAccountEntity(id, username, password));
        return 1;
    }

    @Override
    public List<DriveSchedule> findDriveSchedules() {
        return scheduleDriveRepository.findAllByOrderByIdAsc().stream()
                .map(this::toDriveSchedule)
                .collect(Collectors.toList());
    }

    @Override
    public DriveSchedule findDriveSchedule(int reservationId) {
        return scheduleDriveRepository.findById(reservationId)
                .map(this::toDriveSchedule)
                .orElse(null);
    }

    @Override
    public int updateDriveSchedule(int reservationId, DriveSchedule schedule) {
        Optional<ScheduleDriveEntity> current = scheduleDriveRepository.findById(reservationId);
        if (!current.isPresent()) {
            return 0;
        }
        CarOptionEntity option = carOptionRepository.getReferenceById(schedule.getOptionId());
        current.get().update(option, schedule.getReservationDate(), schedule.getState().getValue());
        return 1;
    }

    @Override
    public int deleteDriveSchedule(int reservationId) {
        if (!scheduleDriveRepository.existsById(reservationId)) {
            return 0;
        }
        scheduleDriveRepository.deleteById(reservationId);
        return 1;
    }

    @Override
    public List<ProductOption> findProducts() {
        return carOptionRepository.findAllByOrderByIdAsc().stream()
                .map(this::toProductOption)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteProductOption(int optionId) {
        if (!carOptionRepository.existsById(optionId)) {
            return 0;
        }
        carOptionRepository.deleteById(optionId);
        return 1;
    }

    @Override
    public int createBrand(String name) {
        carBrandRepository.save(new CarBrandEntity(name));
        return 1;
    }

    @Override
    public int createModel(int brandId, String name) {
        CarBrandEntity brand = carBrandRepository.getReferenceById(brandId);
        carRepository.save(new CarEntity(brand, name));
        return 1;
    }

    @Override
    public int createCarOption(int carId, String color, int cc, int km, double price, String grade) {
        CarEntity car = carRepository.getReferenceById(carId);
        carOptionRepository.save(new CarOptionEntity(car, color, cc, km, BigDecimal.valueOf(price), grade));
        return 1;
    }

    @Override
    public List<SelectionOption> findBrands() {
        return carBrandRepository.findAllByOrderByIdAsc().stream()
                .map(brand -> new SelectionOption(brand.getId(), brand.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SelectionOption> findModels() {
        return carRepository.findAllByOrderByIdAsc().stream()
                .map(car -> new SelectionOption(car.getId(), car.getName()))
                .collect(Collectors.toList());
    }

    private DriveSchedule toDriveSchedule(ScheduleDriveEntity schedule) {
        CarOptionEntity option = schedule.getCarOption();
        return new DriveSchedule(
                schedule.getId(),
                option.getId(),
                schedule.getReservationDate(),
                option.getCar().getName(),
                schedule.getKakaoUser().getNickname(),
                option.getCc(),
                option.getColor(),
                option.getGrade(),
                option.getKm(),
                option.getPrice().doubleValue(),
                schedule.isState() ? DriveSchedule.States.RESERVED : DriveSchedule.States.FAILED
        );
    }

    private ProductOption toProductOption(CarOptionEntity option) {
        CarEntity car = option.getCar();
        return new ProductOption(
                option.getId(),
                car.getId(),
                car.getBrand().getName(),
                car.getName(),
                option.getColor(),
                option.getCc(),
                option.getKm(),
                option.getPrice().doubleValue(),
                option.getGrade()
        );
    }
}
