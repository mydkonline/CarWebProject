package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.TestDriveSchedule;
import com.motionvolt.carcare.domain.model.AdminVehicleOption;
import com.motionvolt.carcare.domain.model.SelectionOption;

import java.util.List;

public interface AdminPort {
    boolean existsAdmin(String username, String password);

    int createAdmin(String id, String username, String password);

    List<TestDriveSchedule> findTestDriveSchedules();

    TestDriveSchedule findTestDriveSchedule(int reservationId);

    int updateTestDriveSchedule(int reservationId, TestDriveSchedule schedule);

    int deleteTestDriveSchedule(int reservationId);

    List<AdminVehicleOption> findAdminVehicleOptions();

    int deleteAdminVehicleOption(int optionId);

    int createBrand(String name);

    int createModel(int brandId, String name);

    int createVehicleOption(int carId, String color, int cc, int km, double price, String grade);

    List<SelectionOption> findBrands();

    List<SelectionOption> findModels();
}
