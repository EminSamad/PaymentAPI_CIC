package com.devees.paymentapi_cic_1.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subscribers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SubscriberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "idnumber", nullable = false)
    private String idNumber;

    @Column(name = "subscribercode", nullable = false, unique = true)
    private String subscriberCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "subcriber_type", nullable = false)
    private SubscriberType subscriberType;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private BigDecimal debt = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "subscriber")
    @JsonManagedReference
    private List<PaymentEntity> payments;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
    }
}