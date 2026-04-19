package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.TestDriveSchedule;
import com.motionvolt.carcare.domain.model.AdminVehicleOption;
import com.motionvolt.carcare.domain.model.SelectionOption;

import java.time.LocalDate;
import java.util.List;

public interface AdminUseCase {
    boolean login(String username, String password);

    boolean createAdmin(String username, String password);

    List<TestDriveSchedule> getTestDriveSchedules();

    List<TestDriveSchedule> searchTestDriveSchedules(String keyword);

    TestDriveSchedule getTestDriveSchedule(int reservationId);

    boolean updateTestDriveSchedule(int reservationId, int optionId, LocalDate reservationDate, String modelName,
                                String customerName, TestDriveSchedule.States state);

    boolean deleteTestDriveSchedule(int reservationId);

    List<AdminVehicleOption> getAdminVehicleOptions();

    boolean deleteAdminVehicleOption(int optionId);

    int createBrand(String name);

    int createModel(int brandId, String name);

    int createVehicleOption(int carId, String color, int cc, int km, double price, String grade);

    List<SelectionOption> getBrands();

    List<SelectionOption> getModels();
}
