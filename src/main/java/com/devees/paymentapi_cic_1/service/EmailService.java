package com.devees.paymentapi_cic_1.service;

import java.math.BigDecimal;

public interface EmailService {

    void sendWelcomeEmail(String to, String username);
    void sendPaymentSuccessEmail(String to, String subscriberCode, java.math.BigDecimal amount);
    void sendPaymentFailedEmail(String to, String subscriberCode);
    void sendSubscriberCreatedEmail(String to, String subscriberCode);
    void sendBalanceToppedUpEmail(String to, String subscriberCode, BigDecimal amount);
    void sendDebtAddedEmail(String to, String subscriberCode, BigDecimal amount);

}