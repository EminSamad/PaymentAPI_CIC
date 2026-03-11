package com.devees.paymentapi_cic_1.repository;

import com.devees.paymentapi_cic_1.entity.DebtEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<DebtEntity, Long> {
    List<DebtEntity> findBySubscriberIdOrderByDateTimeDesc(Long subscriberId);
}
