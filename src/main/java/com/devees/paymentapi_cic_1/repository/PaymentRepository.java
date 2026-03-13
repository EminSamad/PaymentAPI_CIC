package com.devees.paymentapi_cic_1.repository;

import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> , JpaSpecificationExecutor<PaymentEntity> {

    List<PaymentEntity> findByDeletedFalse();

    List<PaymentEntity> findBySubscriberAndDeletedFalse(SubscriberEntity subscriber);

    List<PaymentEntity> findByTransactionCodeAndDeletedFalse(String transactionCode);

    List<PaymentEntity> findBySubscriber(SubscriberEntity subscriber);


}

