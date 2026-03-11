package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.RequestDTO.PaymentRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentResponseDTO;
import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.entity.PaymentStatus;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.entity.SubscriberType;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.repository.PaymentRepository;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private PaymentServiceImpl service;

    private SubscriberEntity subscriber;
    private PaymentEntity payment;
    private PaymentRequestDTO request;

     void setUp() {
        subscriber = new SubscriberEntity();
        subscriber.setId(1L);
        subscriber.setSubscriberCode("SUB001");
        subscriber.setFullName("Emin Test");
        subscriber.setSubscriberType(SubscriberType.INDIVIDUAL);
        subscriber.setDebt(BigDecimal.ZERO);
        subscriber.setBalance(BigDecimal.valueOf(100));
        subscriber.setDeleted(false);

        payment = PaymentEntity.builder()
                .id(1L)
                .subscriber(subscriber)
                .balance(BigDecimal.valueOf(50))
                .status(PaymentStatus.OKAY)
                .transactionCode(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .deleted(false)
                .build();

        request = new PaymentRequestDTO();
        request.setSubscriberCode("SUB001");
        request.setAmount(BigDecimal.valueOf(50));
    }

    @Test
    void makePayment_ShouldSucceed_WhenSubscriberExists() {
        when(subscriberRepository.findBySubscriberCodeAndIsDeletedFalse("SUB001"))
                .thenReturn(Optional.of(subscriber));
        when(paymentRepository.save(any())).thenReturn(payment);

        PaymentResponseDTO result = service.makePayment(request);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any());
        verify(subscriberRepository, times(1)).save(subscriber);
    }

    @Test
    void makePayment_ShouldThrowException_WhenSubscriberNotFound() {
        when(subscriberRepository.findBySubscriberCodeAndIsDeletedFalse("SUB005"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.makePayment(request));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void makePayment_ShouldReduceDebt_WhenSubscriberHasDebt() {
        subscriber.setDebt(BigDecimal.valueOf(30));

        when(subscriberRepository.findBySubscriberCodeAndIsDeletedFalse("SUB001"))
                .thenReturn(Optional.of(subscriber));
        when(paymentRepository.save(any())).thenReturn(payment);

        service.makePayment(request);

        assertEquals(BigDecimal.ZERO, subscriber.getDebt());
        assertEquals(BigDecimal.valueOf(120), subscriber.getBalance());
    }

    @Test
    void getPaymentById_ShouldReturnPayment_WhenExists() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO result = service.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(payment.getTransactionCode(), result.getTransactionCode());
    }

    @Test
    void getPaymentById_ShouldThrowException_WhenNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getPaymentById(1L));
    }

    @Test
    void deletePayment_ShouldMarkDeleted_WhenExists() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        service.deletePayment(1L);

        assertTrue(payment.getDeleted());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void deletePayment_ShouldThrowException_WhenNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deletePayment(1L));
    }

    @Test
    void getPaymentsBySubscriberCode_ShouldReturnList_WhenSubscriberExists() {
        when(subscriberRepository.findBySubscriberCodeAndIsDeletedFalse("SUB001"))
                .thenReturn(Optional.of(subscriber));
        when(paymentRepository.findBySubscriber(subscriber)).thenReturn(List.of(payment));

        List<PaymentResponseDTO> result = service.getPaymentsBySubscriberCode("SUB001");

        assertEquals(1, result.size());
    }
}