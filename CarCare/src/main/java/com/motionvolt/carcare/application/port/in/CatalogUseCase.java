package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.VehicleSummary;
import com.motionvolt.carcare.domain.model.Showroom;

import java.util.List;

public interface CatalogUseCase {
    List<VehicleSummary> getCars();

    List<CarOption> getOptions(int carId);

    List<Showroom> getShowrooms();
}
