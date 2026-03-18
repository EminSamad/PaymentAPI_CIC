package com.devees.paymentapi_cic_1.service;

public interface TokenBlacklistService {
    void blacklistToken(String token, long expirationMillis);
    boolean isBlacklisted(String token);
}