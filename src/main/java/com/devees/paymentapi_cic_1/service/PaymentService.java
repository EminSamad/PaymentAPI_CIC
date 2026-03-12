package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.dto.CheckPaymentDTO;
import com.devees.paymentapi_cic_1.dto.RequestDTO.PaymentRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    PaymentResponseDTO makePayment(PaymentRequestDTO request);

    PaymentResponseDTO getPaymentById(Long id);

    List<PaymentResponseDTO> getAllPayments();

    void deletePayment(Long id);

    PaymentResponseDTO updatePayment(Long id, PaymentRequestDTO request);

    List<CheckPaymentDTO> checkPayments(String transactionCode);

    List<PaymentResponseDTO> getPaymentsBySubscriberCode(String subscriberCode);

}