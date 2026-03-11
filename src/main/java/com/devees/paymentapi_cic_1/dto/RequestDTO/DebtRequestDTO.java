package com.devees.paymentapi_cic_1.dto.RequestDTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DebtRequestDTO {
    private String subscriberCode;
    private BigDecimal amount;
    private String note;
    private LocalDateTime dateTime;
}
