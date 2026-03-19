package com.devees.paymentapi_cic_1.repository;


import com.devees.paymentapi_cic_1.entity.RefreshTokenEntity;
import com.devees.paymentapi_cic_1.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String refreshToken);

    @Transactional
    void deleteByUser(UserEntity user);

}
