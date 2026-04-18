package com.motionvolt.carcare.adapter.in.api;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.motionvolt.carcare.adapter.in.api")
public class ApiExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> invalidJson() {
        return ResponseEntity.badRequest().body(ApiResponse.of(false, "요청 JSON 형식을 확인해 주세요. reservationDate는 YYYY-MM-DD 형식입니다."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> dataConflict() {
        return ResponseEntity.badRequest().body(ApiResponse.of(false, "요청한 예약 정보를 다시 확인해 주세요."));
    }
}
