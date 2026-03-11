package com.devees.paymentapi_cic_1.controller;

import com.devees.paymentapi_cic_1.dto.RequestDTO.SubscriberRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.SubscriberResponseDTO;
import com.devees.paymentapi_cic_1.service.SubscriberService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO role management

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubscriberResponseDTO>> getAllSubscribers() {
        return ResponseEntity.ok(subscriberService.getAllSubscribers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriberResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriberService.getById(id));
    }

    @GetMapping("/code/{subscriberCode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriberResponseDTO> getBySubscriberCode(@PathVariable String subscriberCode) {
        return ResponseEntity.ok(subscriberService.getBySubscriberCode(subscriberCode));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriberResponseDTO> createSubscriber(@Valid @RequestBody SubscriberRequestDTO request) {
        return ResponseEntity.ok(subscriberService.createSubscriber(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriberResponseDTO> updateSubscriber(@PathVariable Long id,
                                                                  @Valid @RequestBody SubscriberRequestDTO request) {
        return ResponseEntity.ok(subscriberService.updateSubscriber(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSubscriber(@PathVariable Long id) {
        subscriberService.deleteSubscriber(id);
        return ResponseEntity.ok("Subscriber deleted successfully");
    }
}
//
//Spring security
//        Authentication
//                JWT token
//Authorization
//Role handling

//
//User entity
//        Role entity  many-many
//
//                shole  admin , user
//                        emin user