package com.motionvolt.carcare.adapter.in.api;

import com.motionvolt.carcare.application.port.in.AdminUseCase;
import com.motionvolt.carcare.domain.model.TestDriveSchedule;
import com.motionvolt.carcare.domain.model.AdminVehicleOption;
import com.motionvolt.carcare.domain.model.SelectionOption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminApiController {
    private final AdminUseCase adminUseCase;

    public AdminApiController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AdminLoginRequest request) {
        boolean loggedIn = adminUseCase.login(request.getUsername(), request.getPassword());
        if (loggedIn) {
            return ResponseEntity.ok(ApiResponse.withSession("관리자 로그인이 완료되었습니다.", "demo-admin-session"));
        }
        return ResponseEntity.status(401).body(ApiResponse.of(false, "관리자 계정을 확인해 주세요."));
    }

    @GetMapping("/summary")
    public AdminSummaryResponse summary() {
        List<TestDriveSchedule> reservations = adminUseCase.getTestDriveSchedules();
        long reservedCount = reservations.stream()
                .filter(reservation -> reservation.getState() == TestDriveSchedule.States.RESERVED)
                .count();
        long failedCount = reservations.size() - reservedCount;

        return new AdminSummaryResponse(reservations.size(), reservedCount, failedCount, adminUseCase.getAdminVehicleOptions().size());
    }

    @GetMapping("/reservations")
    public List<TestDriveSchedule> reservations() {
        return adminUseCase.getTestDriveSchedules();
    }

    @GetMapping("/products")
    public List<AdminVehicleOption> products() {
        return adminUseCase.getAdminVehicleOptions();
    }

    @GetMapping("/brands")
    public List<SelectionOption> brands() {
        return adminUseCase.getBrands();
    }

    @GetMapping("/models")
    public List<SelectionOption> models() {
        return adminUseCase.getModels();
    }

    @PostMapping("/brands")
    public ResponseEntity<ApiResponse> createBrand(@RequestBody NameCreateRequest request) {
        if (request.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.of(false, "브랜드명을 입력해 주세요."));
        }
        adminUseCase.createBrand(request.getName().trim());
        return ResponseEntity.status(201).body(ApiResponse.of(true, "브랜드가 생성되었습니다."));
    }

    @PostMapping("/models")
    public ResponseEntity<ApiResponse> createModel(@RequestBody ModelCreateRequest request) {
        if (request.getBrandId() == null || request.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.of(false, "브랜드와 모델명을 확인해 주세요."));
        }
        adminUseCase.createModel(request.getBrandId(), request.getName().trim());
        return ResponseEntity.status(201).body(ApiResponse.of(true, "모델이 생성되었습니다."));
    }

    @PostMapping("/vehicle-options")
    public ResponseEntity<ApiResponse> createVehicleOption(@RequestBody VehicleOptionCreateRequest request) {
        String validationMessage = request.validate();
        if (validationMessage != null) {
            return ResponseEntity.badRequest().body(ApiResponse.of(false, validationMessage));
        }
        adminUseCase.createVehicleOption(
                request.getCarId(),
                request.getColor().trim(),
                request.getCc(),
                request.getKm(),
                request.getPrice(),
                request.getGrade().trim()
        );
        return ResponseEntity.status(201).body(ApiResponse.of(true, "트림 옵션이 생성되었습니다."));
    }

    @PatchMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable int reservationId,
                                                         @RequestBody ReservationUpdateRequest request) {
        TestDriveSchedule current = adminUseCase.getTestDriveSchedule(reservationId);
        if (current == null) {
            return ResponseEntity.status(404).body(ApiResponse.of(false, "예약을 찾지 못했습니다."));
        }

        TestDriveSchedule.States state = request.getState() == null ? current.getState() : request.getState();
        boolean updated = adminUseCase.updateTestDriveSchedule(
                reservationId,
                current.getOptionId(),
                request.getReservationDate() == null ? current.getReservationDate() : request.getReservationDate(),
                current.getModelName(),
                current.getCustomerName(),
                state
        );
        return updated ? ResponseEntity.ok(ApiResponse.of(true, "예약 상태가 업데이트되었습니다."))
                : ResponseEntity.status(500).body(ApiResponse.of(false, "예약 상태 업데이트에 실패했습니다."));
    }

    public static class AdminSummaryResponse {
        private final int reservationCount;
        private final long reservedCount;
        private final long failedCount;
        private final int productCount;

        public AdminSummaryResponse(int reservationCount, long reservedCount, long failedCount, int productCount) {
            this.reservationCount = reservationCount;
            this.reservedCount = reservedCount;
            this.failedCount = failedCount;
            this.productCount = productCount;
        }

        public int getReservationCount() {
            return reservationCount;
        }

        public long getReservedCount() {
            return reservedCount;
        }

        public long getFailedCount() {
            return failedCount;
        }

        public int getProductCount() {
            return productCount;
        }
    }

    public static class AdminLoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class NameCreateRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        protected boolean isBlank() {
            return name == null || name.trim().isEmpty();
        }
    }

    public static class ModelCreateRequest extends NameCreateRequest {
        private Integer brandId;

        public Integer getBrandId() {
            return brandId;
        }

        public void setBrandId(Integer brandId) {
            this.brandId = brandId;
        }
    }

    public static class VehicleOptionCreateRequest {
        private Integer carId;
        private String color;
        private Integer cc;
        private Integer km;
        private Double price;
        private String grade;

        public Integer getCarId() {
            return carId;
        }

        public void setCarId(Integer carId) {
            this.carId = carId;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getCc() {
            return cc;
        }

        public void setCc(Integer cc) {
            this.cc = cc;
        }

        public Integer getKm() {
            return km;
        }

        public void setKm(Integer km) {
            this.km = km;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        private String validate() {
            if (carId == null) {
                return "모델을 선택해 주세요.";
            }
            if (isBlank(color)) {
                return "색상을 입력해 주세요.";
            }
            if (cc == null || cc < 0) {
                return "배기량을 확인해 주세요.";
            }
            if (km == null || km < 0) {
                return "연비를 확인해 주세요.";
            }
            if (price == null || price < 0) {
                return "가격을 확인해 주세요.";
            }
            if (isBlank(grade)) {
                return "트림명을 입력해 주세요.";
            }
            return null;
        }

        private boolean isBlank(String value) {
            return value == null || value.trim().isEmpty();
        }
    }

    public static class ReservationUpdateRequest {
        private java.time.LocalDate reservationDate;
        private TestDriveSchedule.States state;

        public java.time.LocalDate getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(java.time.LocalDate reservationDate) {
            this.reservationDate = reservationDate;
        }

        public TestDriveSchedule.States getState() {
            return state;
        }

        public void setState(TestDriveSchedule.States state) {
            this.state = state;
        }
    }
}
