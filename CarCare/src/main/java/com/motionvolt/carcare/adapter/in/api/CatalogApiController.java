package com.motionvolt.carcare.adapter.in.api;

import com.motionvolt.carcare.application.port.in.CatalogUseCase;
import com.motionvolt.carcare.domain.model.CarOption;
import com.motionvolt.carcare.domain.model.CarSummary;
import com.motionvolt.carcare.domain.model.Center;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CatalogApiController {
    private final CatalogUseCase catalogUseCase;

    public CatalogApiController(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }

    // React Native is a separate client, so it reads catalog data as JSON instead of Thymeleaf pages.
    @GetMapping("/cars")
    public List<CarSummary> cars() {
        return catalogUseCase.getCars();
    }

    @GetMapping("/cars/{carId}/options")
    public List<CarOption> options(@PathVariable int carId) {
        return catalogUseCase.getOptions(carId);
    }

    @GetMapping("/options/{optionId}/centers")
    public List<Center> centers(@PathVariable int optionId) {
        return catalogUseCase.getCenters();
    }
}
