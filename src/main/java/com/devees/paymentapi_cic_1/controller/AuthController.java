package com.devees.paymentapi_cic_1.controller;

import com.devees.paymentapi_cic_1.dto.RequestDTO.LoginRequest;
import com.devees.paymentapi_cic_1.dto.RequestDTO.RegisterRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.AuthResponseDTO;
import com.devees.paymentapi_cic_1.entity.RefreshTokenEntity;
import com.devees.paymentapi_cic_1.entity.RoleEntity;
import com.devees.paymentapi_cic_1.entity.UserEntity;
import com.devees.paymentapi_cic_1.exception.DuplicateResourceException;
import com.devees.paymentapi_cic_1.repository.RoleRepository;
import com.devees.paymentapi_cic_1.repository.UserRepository;
import com.devees.paymentapi_cic_1.security.JwtService;
import com.devees.paymentapi_cic_1.service.EmailService;
import com.devees.paymentapi_cic_1.service.RefreshTokenService;
import com.devees.paymentapi_cic_1.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserEntity user = (UserEntity) auth.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken.getToken())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        String roleName = (request.getRole() != null) ? request.getRole() : "ROLE_USER";
        RoleEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(new java.util.HashSet<>(java.util.List.of(role)))
                .build();
        userRepository.save(user);

        emailService.sendWelcomeEmail(request.getEmail(), request.getUsername());

        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken.getToken())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        RefreshTokenEntity tokenEntity = refreshTokenService.validateRefreshToken(refreshToken);
        String newAccessToken = jwtService.generateToken(tokenEntity.getUser());

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(newAccessToken)
                .username(tokenEntity.getUser().getUsername())
                .refreshToken(refreshToken)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if ((accessToken != null) && accessToken.startsWith("Bearer ")) {
            String refreshToken = accessToken.substring(7);
            long expiration = jwtService.getExpirationTime(refreshToken);
            tokenBlacklistService.blacklistToken(refreshToken, expiration);
        }
        return ResponseEntity.ok("Logged out Successfully");
    }
}
