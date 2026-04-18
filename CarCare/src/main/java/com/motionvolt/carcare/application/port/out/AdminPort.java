package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
import com.motionvolt.carcare.domain.model.SelectionOption;

import java.util.List;

public interface AdminPort {
    boolean existsAdmin(String username, String password);

    int createAdmin(String id, String username, String password);

    List<DriveSchedule> findDriveSchedules();

    DriveSchedule findDriveSchedule(int reservationId);

    int updateDriveSchedule(int reservationId, DriveSchedule schedule);

    int deleteDriveSchedule(int reservationId);

    List<ProductOption> findProducts();

    int deleteProductOption(int optionId);

    int createBrand(String name);

    int createModel(int brandId, String name);

    int createCarOption(int carId, String color, int cc, int km, double price, String grade);

    List<SelectionOption> findBrands();

    List<SelectionOption> findModels();
}
