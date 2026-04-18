package com.motionvolt.carcare.adapter.in.web;

import com.motionvolt.carcare.application.port.in.AdminUseCase;
import com.motionvolt.carcare.domain.model.DriveSchedule;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("userRole")
public class AdminController {
    private final AdminUseCase adminUseCase;

    public AdminController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    @PostMapping({"/admin/login", "/adminCheck", "/adminCheck.car"})
    public String check(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        if (!adminUseCase.login(username, password)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("userRole", "admin");
        return "redirect:/admin/dashboard";
    }

    @PostMapping({"/admin/accounts", "/adminCreated", "/adminCreated.car"})
    public String createAdmin(@RequestParam String username,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                adminUseCase.createAdmin(username, password) ? "관리자가 생성되었습니다." : "관리자 생성에 실패했습니다.");
        return "redirect:/admin/dashboard";
    }

    @GetMapping({"/admin/reservations", "/adminSelect", "/adminSelect.car"})
    public String schedulesByGet(@RequestParam(required = false) String search, Model model) {
        return schedules(search, model);
    }

    @PostMapping({"/admin/reservations/search", "/adminSelect", "/adminSelect.car"})
    public String schedules(@RequestParam(required = false) String search, Model model) {
        List<DriveSchedule> list = adminUseCase.getDriveSchedules();
        model.addAttribute("list", list);
        if (search != null && !search.trim().isEmpty()) {
            List<DriveSchedule> filteredList = adminUseCase.searchDriveSchedules(search);
            model.addAttribute("filteredList", filteredList);
            model.addAttribute("filteredCount", filteredList.size());
        }
        return "dashboard";
    }

    @GetMapping("/admin/reservations/{id}/edit")
    public String updateFormByPath(@PathVariable int id, Model model) {
        model.addAttribute("ModifyList", adminUseCase.getDriveSchedule(id));
        return "dashboard";
    }

    @PostMapping({"/adminUpdateForm", "/adminUpdateForm.car"})
    public String updateForm(@RequestParam int id, Model model) {
        return updateFormByPath(id, model);
    }

    @PostMapping("/admin/reservations/{id}")
    public String updateByPath(@PathVariable int id,
                         @RequestParam int carId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam String model,
                         @RequestParam String name,
                         @RequestParam DriveSchedule.States state,
                         RedirectAttributes redirectAttributes) {
        boolean updated = adminUseCase.updateDriveSchedule(id, carId, date, model, name, state);
        redirectAttributes.addFlashAttribute("message", updated ? "예약이 수정되었습니다." : "예약 수정에 실패했습니다.");
        return "redirect:/admin/reservations";
    }

    @PostMapping({"/adminUpdate", "/adminUpdate.car"})
    public String update(@RequestParam int id,
                         @RequestParam int carId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam String model,
                         @RequestParam String name,
                         @RequestParam DriveSchedule.States state,
                         RedirectAttributes redirectAttributes) {
        return updateByPath(id, carId, date, model, name, state, redirectAttributes);
    }

    @PostMapping("/admin/reservations/{id}/delete")
    public String deleteReservation(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean deleted = adminUseCase.deleteDriveSchedule(id);
        redirectAttributes.addFlashAttribute("message", deleted ? "예약이 삭제되었습니다." : "삭제할 예약을 찾지 못했습니다.");
        return "redirect:/admin/reservations";
    }

    @GetMapping({"/adminDelete", "/adminDelete.car"})
    public String deleteByGet(@RequestParam int id, RedirectAttributes redirectAttributes) {
        return delete(id, redirectAttributes);
    }

    @PostMapping({"/adminDelete", "/adminDelete.car"})
    public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
        if (adminUseCase.deleteDriveSchedule(id)) {
            redirectAttributes.addFlashAttribute("message", "예약이 삭제되었습니다.");
            return "redirect:/admin/reservations";
        }
        boolean productDeleted = adminUseCase.deleteProductOption(id);
        redirectAttributes.addFlashAttribute("message", productDeleted ? "상품 옵션이 삭제되었습니다." : "삭제할 데이터를 찾지 못했습니다.");
        return productDeleted ? "redirect:/admin/products" : "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        boolean deleted = adminUseCase.deleteProductOption(id);
        redirectAttributes.addFlashAttribute("message", deleted ? "상품 옵션이 삭제되었습니다." : "삭제할 상품 옵션을 찾지 못했습니다.");
        return "redirect:/admin/products";
    }

    @GetMapping({"/admin/products", "/adminProductSelect", "/adminProductSelect.car"})
    public String productsByGet(Model model) {
        return products(model);
    }

    @PostMapping({"/admin/products", "/adminProductSelect", "/adminProductSelect.car"})
    public String products(Model model) {
        model.addAttribute("prdList", adminUseCase.getProducts());
        return "dashboard";
    }

    @GetMapping({"/admin/products/new", "/adminPostProduct", "/adminPostProduct.car"})
    public String productFormByGet(Model model) {
        return productForm(model);
    }

    @PostMapping({"/admin/products/new", "/adminPostProduct", "/adminPostProduct.car"})
    public String productForm(Model model) {
        model.addAttribute("brandList", adminUseCase.getBrands());
        model.addAttribute("modelList", adminUseCase.getModels());
        return "dashboard";
    }

    @PostMapping({"/admin/brands", "/adminBrandWrite", "/adminBrandWrite.car"})
    public String createBrand(@RequestParam String name, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                adminUseCase.createBrand(name) > 0 ? "브랜드가 등록되었습니다." : "브랜드 등록에 실패했습니다.");
        return "redirect:/admin/products/new";
    }

    @PostMapping({"/admin/models", "/adminModelWrite", "/adminModelWrite.car"})
    public String createModel(@RequestParam int carBrandId,
                              @RequestParam String name,
                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                adminUseCase.createModel(carBrandId, name) > 0 ? "모델이 등록되었습니다." : "모델 등록에 실패했습니다.");
        return "redirect:/admin/products/new";
    }

    @PostMapping({"/admin/options", "/adminCarOptionWrite", "/adminCarOptionWrite.car"})
    public String createCarOption(@RequestParam int carId,
                                  @RequestParam String color,
                                  @RequestParam int cc,
                                  @RequestParam int km,
                                  @RequestParam double price,
                                  @RequestParam String grade,
                                  RedirectAttributes redirectAttributes) {
        int created = adminUseCase.createCarOption(carId, color, cc, km, price, grade);
        redirectAttributes.addFlashAttribute("message", created > 0 ? "옵션이 등록되었습니다." : "옵션 등록에 실패했습니다.");
        return "redirect:/admin/products/new";
    }

}
