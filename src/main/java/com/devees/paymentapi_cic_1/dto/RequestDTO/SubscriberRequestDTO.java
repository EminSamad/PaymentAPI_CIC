package com.devees.paymentapi_cic_1.dto.RequestDTO;

import com.devees.paymentapi_cic_1.entity.SubscriberType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriberRequestDTO {

    @NotBlank(message = "Subscriber code cannot be empty")
    private String subscriberCode;

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "ID number cannot be empty")
    private String idNumber;

    @NotNull(message = "Subscriber type cannot be null")
    private SubscriberType subscriberType;

    @PositiveOrZero(message = "Debt cannot be negative")
    private BigDecimal debt;

    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;
}