package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.CheckPaymentDTO;
import com.devees.paymentapi_cic_1.dto.RequestDTO.PaymentRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentResponseDTO;
import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.entity.PaymentStatus;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.repository.PaymentRepository;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import com.devees.paymentapi_cic_1.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final SubscriberRepository subscriberRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              SubscriberRepository subscriberRepository) {
        this.paymentRepository = paymentRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public PaymentResponseDTO makePayment(PaymentRequestDTO request) {
        log.info("Making payment for subscriber: {}", request.getSubscriberCode());

        SubscriberEntity subscriber = subscriberRepository
                .findBySubscriberCodeAndIsDeletedFalse(request.getSubscriberCode())
                .orElseThrow(() -> {
                    log.error("Subscriber not found: {}", request.getSubscriberCode());
                    return new ResourceNotFoundException("Subscriber not found");
                });

        BigDecimal amount = request.getAmount();

        if (subscriber.getDebt().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal debtBefore = subscriber.getDebt();
            if (debtBefore.compareTo(amount) >= 0) {
                subscriber.setDebt(debtBefore.subtract(amount));
                amount = BigDecimal.ZERO;
            } else {
                amount = amount.subtract(debtBefore);
                subscriber.setDebt(BigDecimal.ZERO);
            }
        }

        subscriber.setBalance(subscriber.getBalance().add(amount));
        subscriberRepository.save(subscriber);

        PaymentEntity payment = PaymentEntity.builder()
                .subscriber(subscriber)
                .balance(request.getAmount())
                .status(PaymentStatus.OKAY)
                .transactionCode(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        log.info("Payment successful. TransactionCode: {}, Amount: {}", payment.getTransactionCode(), request.getAmount());
        return mapToDTO(payment);
    }

    @Override
    public PaymentResponseDTO addBalance(String subscriberCode, BigDecimal amount) {
        log.info("Adding balance for subscriber: {}, amount: {}", subscriberCode, amount);

        SubscriberEntity subscriber = subscriberRepository
                .findBySubscriberCodeAndIsDeletedFalse(subscriberCode)
                .orElseThrow(() -> {
                    log.error("Subscriber not found: {}", subscriberCode);
                    return new ResourceNotFoundException("Subscriber not found");
                });

        subscriber.setBalance(subscriber.getBalance().add(amount));
        subscriberRepository.save(subscriber);

        PaymentEntity payment = PaymentEntity.builder()
                .subscriber(subscriber)
                .balance(amount)
                .status(PaymentStatus.OKAY)
                .transactionCode(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .deleted(false)
                .build();

        paymentRepository.save(payment);

        log.info("Balance added successfully for subscriber: {}", subscriberCode);
        return mapToDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsBySubscriberCode(String subscriberCode) {
        log.info("Getting payments for subscriber: {}", subscriberCode);

        SubscriberEntity subscriber = subscriberRepository
                .findBySubscriberCodeAndIsDeletedFalse(subscriberCode)
                .orElseThrow(() -> {
                    log.error("Subscriber not found: {}", subscriberCode);
                    return new ResourceNotFoundException("Subscriber not found");
                });

        return paymentRepository.findBySubscriber(subscriber)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        log.info("Getting payment by id: {}", id);

        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found with id: {}", id);
                    return new ResourceNotFoundException("Payment not found with id: " + id);
                });

        return mapToDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        log.info("Getting all payments");
        return List.of();
    }

    @Override
    public void deletePayment(Long id) {
        log.warn("Deleting payment with id: {}", id);

        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found with id: {}", id);
                    return new ResourceNotFoundException("Payment not found");
                });

        payment.setDeleted(true);
        paymentRepository.save(payment);
        log.info("Payment deleted successfully with id: {}", id);
    }

    @Override
    public List<CheckPaymentDTO> checkPayments(String transactionCode) {
        log.info("Checking payments for transactionCode: {}", transactionCode);

        return paymentRepository.findByTransactionCodeAndDeletedFalse(transactionCode)
                .stream()
                .map(p -> new CheckPaymentDTO(
                        p.getSubscriber().getSubscriberCode(),
                        p.getDateTime()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO request) {
        log.info("Updating payment with id: {}", id);

        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Payment not found with id: {}", id);
                    return new ResourceNotFoundException("Payment not found with id: " + id);
                });

        SubscriberEntity subscriber = subscriberRepository
                .findBySubscriberCodeAndIsDeletedFalse(request.getSubscriberCode())
                .orElseThrow(() -> {
                    log.error("Subscriber not found: {}", request.getSubscriberCode());
                    return new ResourceNotFoundException("Subscriber not found");
                });

        payment.setSubscriber(subscriber);
        payment.setBalance(request.getAmount());
        payment.setDateTime(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("Payment updated successfully with id: {}", id);
        return mapToDTO(payment);
    }

    private PaymentResponseDTO mapToDTO(PaymentEntity payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getTransactionCode(),
                payment.getBalance(),
                payment.getStatus(),
                payment.getDateTime()
        );
    }
}