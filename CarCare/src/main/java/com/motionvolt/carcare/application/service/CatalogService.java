package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.CatalogUseCase;
import com.motionvolt.carcare.application.port.out.CatalogPort;
import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.VehicleSummary;
import com.motionvolt.carcare.domain.model.Showroom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogService implements CatalogUseCase {
    private final CatalogPort catalogPort;

    public CatalogService(CatalogPort catalogPort) {
        this.catalogPort = catalogPort;
    }

    @Override
    public List<VehicleSummary> getCars() {
        return catalogPort.findCars();
    }

    @Override
    public List<CarOption> getOptions(int carId) {
        return catalogPort.findOptions(carId);
    }

    @Override
    public List<Showroom> getShowrooms() {
        return catalogPort.findShowrooms();
    }
}
