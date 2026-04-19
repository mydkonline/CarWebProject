package com.motionvolt.carcare.adapter.out.persistence;

import com.motionvolt.carcare.adapter.out.persistence.entity.AdminAccountEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarBrandEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.CarOptionEntity;
import com.motionvolt.carcare.adapter.out.persistence.entity.TestDriveBookingEntity;
import com.motionvolt.carcare.adapter.out.persistence.repository.AdminAccountRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarBrandRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarOptionRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.CarRepository;
import com.motionvolt.carcare.adapter.out.persistence.repository.TestDriveBookingRepository;
import com.motionvolt.carcare.application.port.out.AdminPort;
import com.motionvolt.carcare.domain.model.TestDriveSchedule;
import com.motionvolt.carcare.domain.model.AdminVehicleOption;
import com.motionvolt.carcare.domain.model.SelectionOption;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaAdminAdapter implements AdminPort {
    private final AdminAccountRepository adminAccountRepository;
    private final TestDriveBookingRepository testDriveBookingRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarRepository carRepository;
    private final CarOptionRepository carOptionRepository;

    public JpaAdminAdapter(AdminAccountRepository adminAccountRepository,
                           TestDriveBookingRepository testDriveBookingRepository,
                           CarBrandRepository carBrandRepository,
                           CarRepository carRepository,
                           CarOptionRepository carOptionRepository) {
        this.adminAccountRepository = adminAccountRepository;
        this.testDriveBookingRepository = testDriveBookingRepository;
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
    public List<TestDriveSchedule> findTestDriveSchedules() {
        return testDriveBookingRepository.findAllByOrderByIdAsc().stream()
                .map(this::toTestDriveSchedule)
                .collect(Collectors.toList());
    }

    @Override
    public TestDriveSchedule findTestDriveSchedule(int reservationId) {
        return testDriveBookingRepository.findById(reservationId)
                .map(this::toTestDriveSchedule)
                .orElse(null);
    }

    @Override
    public int updateTestDriveSchedule(int reservationId, TestDriveSchedule schedule) {
        Optional<TestDriveBookingEntity> current = testDriveBookingRepository.findById(reservationId);
        if (!current.isPresent()) {
            return 0;
        }
        CarOptionEntity option = carOptionRepository.getReferenceById(schedule.getOptionId());
        current.get().update(option, schedule.getReservationDate(), schedule.getState().getValue());
        return 1;
    }

    @Override
    public int deleteTestDriveSchedule(int reservationId) {
        if (!testDriveBookingRepository.existsById(reservationId)) {
            return 0;
        }
        testDriveBookingRepository.deleteById(reservationId);
        return 1;
    }

    @Override
    public List<AdminVehicleOption> findAdminVehicleOptions() {
        return carOptionRepository.findAllByOrderByIdAsc().stream()
                .map(this::toAdminVehicleOption)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteAdminVehicleOption(int optionId) {
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
    public int createVehicleOption(int carId, String color, int cc, int km, double price, String grade) {
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

    private TestDriveSchedule toTestDriveSchedule(TestDriveBookingEntity schedule) {
        CarOptionEntity option = schedule.getCarOption();
        return new TestDriveSchedule(
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
                schedule.isState() ? TestDriveSchedule.States.RESERVED : TestDriveSchedule.States.FAILED
        );
    }

    private AdminVehicleOption toAdminVehicleOption(CarOptionEntity option) {
        CarEntity car = option.getCar();
        return new AdminVehicleOption(
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
