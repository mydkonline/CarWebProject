package com.motionvolt.carcare.adapter.in.api;

import com.motionvolt.carcare.application.port.in.AdminUseCase;
import com.motionvolt.carcare.domain.model.DriveSchedule;
import com.motionvolt.carcare.domain.model.ProductOption;
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
        List<DriveSchedule> reservations = adminUseCase.getDriveSchedules();
        long reservedCount = reservations.stream()
                .filter(reservation -> reservation.getState() == DriveSchedule.States.RESERVED)
                .count();
        long failedCount = reservations.size() - reservedCount;

        return new AdminSummaryResponse(reservations.size(), reservedCount, failedCount, adminUseCase.getProducts().size());
    }

    @GetMapping("/reservations")
    public List<DriveSchedule> reservations() {
        return adminUseCase.getDriveSchedules();
    }

    @GetMapping("/products")
    public List<ProductOption> products() {
        return adminUseCase.getProducts();
    }

    @PatchMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse> updateReservation(@PathVariable int reservationId,
                                                         @RequestBody ReservationUpdateRequest request) {
        DriveSchedule current = adminUseCase.getDriveSchedule(reservationId);
        if (current == null) {
            return ResponseEntity.status(404).body(ApiResponse.of(false, "예약을 찾지 못했습니다."));
        }

        DriveSchedule.States state = request.getState() == null ? current.getState() : request.getState();
        boolean updated = adminUseCase.updateDriveSchedule(
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

    public static class ReservationUpdateRequest {
        private java.time.LocalDate reservationDate;
        private DriveSchedule.States state;

        public java.time.LocalDate getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(java.time.LocalDate reservationDate) {
            this.reservationDate = reservationDate;
        }

        public DriveSchedule.States getState() {
            return state;
        }

        public void setState(DriveSchedule.States state) {
            this.state = state;
        }
    }
}
