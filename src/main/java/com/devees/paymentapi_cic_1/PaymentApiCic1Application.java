package com.devees.paymentapi_cic_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentApiCic1Application {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApiCic1Application.class, args);
    }

}
