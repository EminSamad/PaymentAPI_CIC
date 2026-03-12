package com.devees.paymentapi_cic_1.service;

public interface EmailService {

    void sendWelcomeEmail(String to, String username);
    void sendPaymentSuccessEmail(String to, String subscriberCode, java.math.BigDecimal amount);
    void sendPaymentFailedEmail(String to, String subscriberCode);

}