package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.VehicleSummary;
import com.motionvolt.carcare.domain.model.Showroom;

import java.util.List;

public interface CatalogPort {
    List<VehicleSummary> findCars();
    List<CarOption> findOptions(int carId);
    List<Showroom> findShowrooms();
}
