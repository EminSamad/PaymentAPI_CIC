package com.devees.paymentapi_cic_1.repository;

import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentResponseDTO;
import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByDeletedFalse();

    List<PaymentEntity> findBySubscriberAndDeletedFalse(SubscriberEntity subscriber);

    List<PaymentEntity> findByTransactionCodeAndDeletedFalse(String transactionCode);

    List<PaymentEntity> findBySubscriber(SubscriberEntity subscriber);
}

