package com.motionvolt.carcare.application.service;

import com.motionvolt.carcare.application.port.in.KakaoLoginUseCase;
import com.motionvolt.carcare.application.port.out.KakaoPort;
import com.motionvolt.carcare.application.port.out.ReservationPort;
import com.motionvolt.carcare.domain.model.KakaoUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoLoginService implements KakaoLoginUseCase {
    private final KakaoPort kakaoPort;
    private final ReservationPort reservationPort;

    public KakaoLoginService(KakaoPort kakaoPort, ReservationPort reservationPort) {
        this.kakaoPort = kakaoPort;
        this.reservationPort = reservationPort;
    }

    @Override
    @Transactional
    public KakaoUser login(String authorizationCode) {
        KakaoUser user = kakaoPort.fetchUser(authorizationCode);
        if (!reservationPort.existsKakaoUser(user.getId())) {
            reservationPort.saveKakaoUser(user);
        }
        return user;
    }
}
