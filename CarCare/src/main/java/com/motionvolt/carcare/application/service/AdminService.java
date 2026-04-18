package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.AdminUseCase;
import com.motionvolt.carcare.application.port.out.AdminPort;
import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
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
    public List<DriveSchedule> getDriveSchedules() {
        return adminPort.findDriveSchedules();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriveSchedule> searchDriveSchedules(String keyword) {
        String normalized = keyword == null ? "" : keyword.toLowerCase();
        return getDriveSchedules().stream()
                .filter(schedule -> schedule.getCustomerName() != null && schedule.getCustomerName().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DriveSchedule getDriveSchedule(int reservationId) {
        return adminPort.findDriveSchedule(reservationId);
    }

    @Override
    @Transactional
    public boolean updateDriveSchedule(int reservationId, int optionId, LocalDate reservationDate, String modelName, String customerName,
                                       DriveSchedule.States state) {
        DriveSchedule schedule = new DriveSchedule(reservationId, optionId, reservationDate, modelName, customerName, 0, null, null, 0, 0, state);
        return adminPort.updateDriveSchedule(reservationId, schedule) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDriveSchedule(int reservationId) {
        return adminPort.deleteDriveSchedule(reservationId) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOption> getProducts() {
        return adminPort.findProducts();
    }

    @Override
    @Transactional
    public boolean deleteProductOption(int optionId) {
        return adminPort.deleteProductOption(optionId) > 0;
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
    public int createCarOption(int carId, String color, int cc, int km, double price, String grade) {
        return adminPort.createCarOption(carId, color, cc, km, price, grade);
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
