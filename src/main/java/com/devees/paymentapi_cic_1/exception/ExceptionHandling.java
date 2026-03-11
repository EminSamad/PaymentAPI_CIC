package com.devees.paymentapi_cic_1.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionHandling {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

//TODO mailtrap
