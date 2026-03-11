package com.devees.paymentapi_cic_1.dto.ResponseDTO;

import com.devees.paymentapi_cic_1.entity.SubscriberType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberResponseDTO {

    private Long id;
    private String subscriberCode;
    private String fullName;
    private String idNumber;
    private SubscriberType subscriberType;
    private BigDecimal debt;
    private BigDecimal balance;
}