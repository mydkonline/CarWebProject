package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.CarSummary;
import com.motionvolt.carcare.domain.model.Center;

import java.util.List;

public interface CatalogUseCase {
    List<CarSummary> getCars();

    List<CarOption> getOptions(int carId);

    List<Center> getCenters();
}
