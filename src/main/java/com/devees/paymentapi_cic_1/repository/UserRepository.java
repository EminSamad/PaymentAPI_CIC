package com.devees.paymentapi_cic_1.repository;

import com.devees.paymentapi_cic_1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);
}
