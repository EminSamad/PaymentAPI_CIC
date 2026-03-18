package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.entity.RefreshTokenEntity;
import com.devees.paymentapi_cic_1.entity.UserEntity;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.repository.RefreshTokenRepository;
import com.devees.paymentapi_cic_1.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshTokenEntity createRefreshToken(UserEntity user) {
        log.info("Creating refresh token for user : {}", user.getUsername());

        refreshTokenRepository.deleteByUserId(user);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshTokenEntity validateRefreshToken(String token) {
        log.info("Validating refresh token");

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new ResourceNotFoundException("Refresh token not found");
                });

        if (refreshToken.isExpired()) {
            log.warn("Refresh token expired for user: {}", refreshToken.getUser().getUsername());
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token expired. Please login again.");
        }

        return refreshToken;
    }

     @Override
    @Transactional
    public void deleteByUser(UserEntity userEntity) {
        log.info("Deleting refresh token for user : {}", userEntity.getUsername());
        refreshTokenRepository.deleteByUserId(userEntity);

    }
}
