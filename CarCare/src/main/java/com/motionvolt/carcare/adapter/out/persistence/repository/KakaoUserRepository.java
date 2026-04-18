package com.motionvolt.carcare.adapter.out.persistence.repository;

import com.motionvolt.carcare.adapter.out.persistence.entity.KakaoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoUserRepository extends JpaRepository<KakaoUserEntity, Long> {
}
