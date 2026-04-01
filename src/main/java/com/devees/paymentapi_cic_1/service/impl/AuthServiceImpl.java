package com.devees.paymentapi_cic_1.service.impl;

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
import com.devees.paymentapi_cic_1.service.AuthService;
import com.devees.paymentapi_cic_1.service.EmailService;
import com.devees.paymentapi_cic_1.service.RefreshTokenService;
import com.devees.paymentapi_cic_1.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public AuthResponseDTO login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserEntity user = (UserEntity) auth.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);
        log.info("Login successful for user: {}", user.getUsername());

        return AuthResponseDTO.builder()
                .token(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.info("Register attempt for user: {}", request.getUsername());
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
                .roles(new HashSet<>(List.of(role)))
                .build();
        userRepository.save(user);

        emailService.sendWelcomeEmail(request.getEmail(), request.getUsername());

        String accessToken = jwtService.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);
        log.info("Register successful for user: {}", user.getUsername());

        return AuthResponseDTO.builder()
                .token(accessToken)
                .username(user.getUsername())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponseDTO refresh(String refreshToken) {
        log.info("Refresh token attempt");
        RefreshTokenEntity tokenEntity = refreshTokenService.validateRefreshToken(refreshToken);
        String newAccessToken = jwtService.generateToken(tokenEntity.getUser());

        return AuthResponseDTO.builder()
                .token(newAccessToken)
                .username(tokenEntity.getUser().getUsername())
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expiration = jwtService.getExpirationTime(token);
            tokenBlacklistService.blacklistToken(token, expiration);
            log.info("User logged out successfully");
        }
    }
}