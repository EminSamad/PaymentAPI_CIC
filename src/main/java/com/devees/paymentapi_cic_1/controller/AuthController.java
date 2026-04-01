package com.devees.paymentapi_cic_1.controller;

import com.devees.paymentapi_cic_1.dto.RequestDTO.LoginRequest;
import com.devees.paymentapi_cic_1.dto.RequestDTO.RegisterRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.AuthResponseDTO;
import com.devees.paymentapi_cic_1.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.refresh(request.get("refreshToken")));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request.getHeader("Authorization"));
        return ResponseEntity.ok("Logged out successfully");
    }
}
