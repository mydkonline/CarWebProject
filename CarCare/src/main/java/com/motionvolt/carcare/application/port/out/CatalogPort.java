package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.CarSummary;
import com.motionvolt.carcare.domain.model.Center;

import java.util.List;

public interface CatalogPort {
    List<CarSummary> findCars();
    List<CarOption> findOptions(int carId);
    List<Center> findCenters();
}
