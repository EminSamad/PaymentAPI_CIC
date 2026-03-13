package com.devees.paymentapi_cic_1.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentReportDTO {
    private String subscriberCode;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
}