package com.devees.paymentapi_cic_1.dto.ResponseDTO;

import com.devees.paymentapi_cic_1.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentResponseDTO {

    private Long id;
    private String transactionCode;
    private BigDecimal balance;
    private PaymentStatus status;
    private LocalDateTime dateTime;
}
