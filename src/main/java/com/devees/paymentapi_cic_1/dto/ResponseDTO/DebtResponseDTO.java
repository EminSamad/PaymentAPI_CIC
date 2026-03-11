package com.devees.paymentapi_cic_1.dto.ResponseDTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DebtResponseDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String note;
}
