package com.motionvolt.carcare.adapter.out.persistence.repository;

import com.motionvolt.carcare.adapter.out.persistence.entity.AdminAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccountEntity, String> {
    boolean existsByUsernameAndPassword(String username, String password);
}
