package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.entity.RefreshTokenEntity;
import com.devees.paymentapi_cic_1.entity.UserEntity;

public interface RefreshTokenService {

    RefreshTokenEntity createRefreshToken(UserEntity userEntity);
    RefreshTokenEntity validateRefreshToken(String refreshToken);
    void deleteByUser(UserEntity userEntity);

}
