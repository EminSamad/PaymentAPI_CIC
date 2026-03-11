package com.devees.paymentapi_cic_1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SubscriberEntity subscriber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(length = 255)
    private String note;
}
