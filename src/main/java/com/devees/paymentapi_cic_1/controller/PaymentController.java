package com.devees.paymentapi_cic_1.controller;

import com.devees.paymentapi_cic_1.dto.RequestDTO.PaymentRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentResponseDTO;
import com.devees.paymentapi_cic_1.dto.CheckPaymentDTO;
import com.devees.paymentapi_cic_1.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class    PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentResponseDTO> pay(@Valid @RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.makePayment(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/subscriber/{subscriberCode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsBySubscriberCode(
            @PathVariable String subscriberCode) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsBySubscriberCode(subscriberCode);
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponseDTO updatePayment(@PathVariable Long id,
                                            @Valid @RequestBody PaymentRequestDTO request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/check/{transactionCode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CheckPaymentDTO>> check(@PathVariable String transactionCode) {
        List<CheckPaymentDTO> payments = paymentService.checkPayments(transactionCode);
        return ResponseEntity.ok(payments);
    }

}


//TODO validation , exception handling, logging, test case
