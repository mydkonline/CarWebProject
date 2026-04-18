package com.motionvolt.carcare.application.port.out;

import com.motionvolt.carcare.domain.model.KakaoUser;

public interface KakaoPort {
    KakaoUser fetchUser(String authorizationCode);
}
