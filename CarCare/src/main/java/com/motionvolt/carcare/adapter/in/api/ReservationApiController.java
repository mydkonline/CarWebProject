package com.motionvolt.carcare.adapter.in.api;

import com.motionvolt.carcare.application.port.in.ReservationUseCase;
import com.motionvolt.carcare.domain.model.ReservationAvailability;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReservationApiController {
    private static final long DEMO_KAKAO_USER_ID = 9000000001L;

    private final ReservationUseCase reservationUseCase;

    public ReservationApiController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @GetMapping("/reservations/availability")
    public List<ReservationAvailability> availability(@RequestParam int optionId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                      @RequestParam(defaultValue = "14") int days) {
        return reservationUseCase.getAvailability(optionId, startDate, days);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse> reserve(@RequestBody ReservationRequest request) {
        String validationMessage = request.validate();
        if (validationMessage != null) {
            return ResponseEntity.badRequest().body(ApiResponse.of(false, validationMessage));
        }

        // SPIKE: mobile demo uses a seeded Kakao user until the native Kakao SDK flow is added.
        long kakaoUserId = request.getKakaoUserId() == null ? DEMO_KAKAO_USER_ID : request.getKakaoUserId();
        boolean reserved = reservationUseCase.reserve(
                request.getCenterId(),
                kakaoUserId,
                request.getOptionId(),
                request.getReservationDate()
        );

        HttpStatus status = reserved ? HttpStatus.CREATED : HttpStatus.CONFLICT;
        String message = reserved ? "시승 신청이 완료되었습니다." : "선택한 날짜는 예약할 수 없습니다.";
        return ResponseEntity.status(status).body(ApiResponse.of(reserved, message));
    }

    public static class ReservationRequest {
        private Integer centerId;
        private Integer optionId;
        private Long kakaoUserId;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate reservationDate;

        public Integer getCenterId() {
            return centerId;
        }

        public void setCenterId(Integer centerId) {
            this.centerId = centerId;
        }

        public Integer getOptionId() {
            return optionId;
        }

        public void setOptionId(Integer optionId) {
            this.optionId = optionId;
        }

        public Long getKakaoUserId() {
            return kakaoUserId;
        }

        public void setKakaoUserId(Long kakaoUserId) {
            this.kakaoUserId = kakaoUserId;
        }

        public LocalDate getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(LocalDate reservationDate) {
            this.reservationDate = reservationDate;
        }

        private String validate() {
            if (centerId == null) {
                return "전시장을 선택해 주세요.";
            }
            if (optionId == null) {
                return "차량 옵션을 선택해 주세요.";
            }
            if (reservationDate == null) {
                return "시승일을 선택해 주세요.";
            }
            return null;
        }
    }
}
