package com.devees.paymentapi_cic_1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="refresh_token")
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
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
