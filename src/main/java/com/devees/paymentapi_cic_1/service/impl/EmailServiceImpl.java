package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;


    @Override
    public void sendWelcomeEmail(String to, String username) {
        log.info("Sending welcome email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Payment API!");
        message.setText("Hello " + username + ",\n\nYou have successfully registered to our system.\n\nBest regards,\nPayment API Team");
        mailSender.send(message);
        log.info("Welcome email sent to: {}", to);
    }

    @Override
    public void sendPaymentSuccessEmail(String to, String subscriberCode, BigDecimal amount) {
        log.info("Sending payment success email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Payment Successful");
        message.setText("Hello,\n\nYour payment has been successfully processed.\n\nSubscriber Code: " + subscriberCode + "\nAmount: " + amount + " AZN\n\nBest regards,\nPayment API Team");
        mailSender.send(message);
        log.info("Payment success email sent to: {}", to);
    }

    @Override
    public void sendPaymentFailedEmail(String to, String subscriberCode) {
        log.warn("Sending payment failed email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Payment Failed");
        message.setText("Hello,\n\nYour payment has failed. Please try again.\n\nSubscriber Code: " + subscriberCode + "\n\nBest regards,\nPayment API Team");
        mailSender.send(message);
        log.warn("Payment failed email sent to: {}", to);
    }
}