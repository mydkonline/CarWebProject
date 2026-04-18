package com.motionvolt.carcare.application.port.in;

import com.motionvolt.carcare.domain.model.KakaoUser;

public interface KakaoLoginUseCase {
    KakaoUser login(String authorizationCode);
}
