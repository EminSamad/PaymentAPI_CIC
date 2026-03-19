package com.devees.paymentapi_cic_1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="refresh_token")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name="column_id")
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private LocalDateTime expiredAt;

    public boolean isExpired() {
        return expiredAt != null;
    }

}
