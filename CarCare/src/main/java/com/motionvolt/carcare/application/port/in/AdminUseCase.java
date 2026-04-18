package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
import com.motionvolt.carcare.domain.model.SelectionOption;

import java.time.LocalDate;
import java.util.List;

public interface AdminUseCase {
    boolean login(String username, String password);

    boolean createAdmin(String username, String password);

    List<DriveSchedule> getDriveSchedules();

    List<DriveSchedule> searchDriveSchedules(String keyword);

    DriveSchedule getDriveSchedule(int id);

    boolean updateDriveSchedule(int id, int carId, LocalDate date, String model, String name, DriveSchedule.States state);

    boolean deleteDriveSchedule(int id);

    List<ProductOption> getProducts();

    boolean deleteProductOption(int id);

    int createBrand(String name);

    int createModel(int brandId, String name);

    int createCarOption(int carId, String color, int cc, int km, double price, String grade);

    List<SelectionOption> getBrands();

    List<SelectionOption> getModels();
}
