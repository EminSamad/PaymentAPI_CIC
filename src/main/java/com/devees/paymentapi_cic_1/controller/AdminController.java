package com.devees.paymentapi_cic_1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Welcome Admin!");
    }

    //TODO hesabatlar
    //odenishler - subscriber code, odenis tarixi, odenish meblegi
    //filterler - subcode, tarix araligi


    //endpoint export to excel  File handling
}