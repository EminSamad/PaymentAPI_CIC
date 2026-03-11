package com.devees.paymentapi_cic_1.dto.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import com.devees.paymentapi_cic_1.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotBlank(message = "Subscriber code cannot be empty")
    private String subscriberCode;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    private PaymentStatus status;
    private String transactionCode;
    private LocalDateTime timestamp;
}