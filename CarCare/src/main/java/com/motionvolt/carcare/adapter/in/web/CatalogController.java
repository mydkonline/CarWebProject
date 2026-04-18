package com.motionvolt.carcare.adapter.in.web;

import com.motionvolt.carcare.application.port.in.CatalogUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogController {
    private final CatalogUseCase catalogUseCase;

    public CatalogController(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }

    @GetMapping({"/cars", "/carList", "/carList.car"})
    public String cars(Model model) {
        model.addAttribute("carList", catalogUseCase.getCars());
        return "carList";
    }

    @GetMapping("/cars/{carId}/options")
    public String options(@PathVariable int carId, Model viewModel) {
        viewModel.addAttribute("carListOption", catalogUseCase.getOptions(carId));
        return "carListOption";
    }

    @PostMapping({"/carListOption", "/carListOption.car"})
    public String legacyOptions(@RequestParam("id") int carId, Model viewModel) {
        return options(carId, viewModel);
    }

    @GetMapping("/options/{optionId}/centers")
    public String centers(@PathVariable int optionId, Model model) {
        model.addAttribute("center", catalogUseCase.getCenters());
        model.addAttribute("optionId", optionId);
        return "center";
    }

    @PostMapping({"/center", "/center.car"})
    public String legacyCenters(@RequestParam("id") int optionId, Model model) {
        return centers(optionId, model);
    }
}
