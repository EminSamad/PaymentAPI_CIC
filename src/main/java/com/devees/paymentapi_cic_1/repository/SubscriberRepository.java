package com.devees.paymentapi_cic_1.repository;

import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {

    Optional<SubscriberEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<SubscriberEntity> findBySubscriberCodeAndIsDeletedFalse(String subscriberCode);

    List<SubscriberEntity> findAllByIsDeletedFalse();
}