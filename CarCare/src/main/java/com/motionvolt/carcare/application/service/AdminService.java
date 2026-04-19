package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.AdminUseCase;
import com.motionvolt.carcare.application.port.out.AdminPort;
import com.motionvolt.carcare.domain.model.TestDriveSchedule;
import com.motionvolt.carcare.domain.model.AdminVehicleOption;
import com.motionvolt.carcare.domain.model.SelectionOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService implements AdminUseCase {
    private final AdminPort adminPort;

    public AdminService(AdminPort adminPort) {
        this.adminPort = adminPort;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return false;
        }
        return adminPort.existsAdmin(username, password);
    }

    @Override
    @Transactional
    public boolean createAdmin(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return false;
        }
        return adminPort.createAdmin(UUID.randomUUID().toString(), username, password) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestDriveSchedule> getTestDriveSchedules() {
        return adminPort.findTestDriveSchedules();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestDriveSchedule> searchTestDriveSchedules(String keyword) {
        String normalized = keyword == null ? "" : keyword.toLowerCase();
        return getTestDriveSchedules().stream()
                .filter(schedule -> schedule.getCustomerName() != null && schedule.getCustomerName().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestDriveSchedule getTestDriveSchedule(int reservationId) {
        return adminPort.findTestDriveSchedule(reservationId);
    }

    @Override
    @Transactional
    public boolean updateTestDriveSchedule(int reservationId, int optionId, LocalDate reservationDate, String modelName, String customerName,
                                       TestDriveSchedule.States state) {
        TestDriveSchedule schedule = new TestDriveSchedule(reservationId, optionId, reservationDate, modelName, customerName, 0, null, null, 0, 0, state);
        return adminPort.updateTestDriveSchedule(reservationId, schedule) > 0;
    }

    @Override
    @Transactional
    public boolean deleteTestDriveSchedule(int reservationId) {
        return adminPort.deleteTestDriveSchedule(reservationId) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminVehicleOption> getAdminVehicleOptions() {
        return adminPort.findAdminVehicleOptions();
    }

    @Override
    @Transactional
    public boolean deleteAdminVehicleOption(int optionId) {
        return adminPort.deleteAdminVehicleOption(optionId) > 0;
    }

    @Override
    @Transactional
    public int createBrand(String name) {
        return adminPort.createBrand(name);
    }

    @Override
    @Transactional
    public int createModel(int brandId, String name) {
        return adminPort.createModel(brandId, name);
    }

    @Override
    @Transactional
    public int createVehicleOption(int carId, String color, int cc, int km, double price, String grade) {
        return adminPort.createVehicleOption(carId, color, cc, km, price, grade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SelectionOption> getBrands() {
        return adminPort.findBrands();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SelectionOption> getModels() {
        return adminPort.findModels();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
