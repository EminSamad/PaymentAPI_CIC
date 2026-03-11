package com.devees.paymentapi_cic_1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    @JsonBackReference
    private SubscriberEntity subscriber;


    @Column(nullable = false)
    private BigDecimal balance;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false, unique = true)
    private String transactionCode;

    @Column(nullable = false)
    private Boolean deleted = false ;

    public PaymentEntity(SubscriberEntity subscriber, BigDecimal balance, PaymentStatus status, String transactionCode) {
        this.subscriber = subscriber;
        this.balance = balance;
        this.status = status;
        this.transactionCode = transactionCode;
        this.dateTime = LocalDateTime.now();
    }
}
