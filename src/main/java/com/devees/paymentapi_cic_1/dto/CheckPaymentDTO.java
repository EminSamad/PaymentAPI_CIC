package com.devees.paymentapi_cic_1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckPaymentDTO {
        private String status;
        private LocalDateTime timestamp;

    public CheckPaymentDTO(BigDecimal balance, String name, LocalDateTime dateTime) {

    }
}
