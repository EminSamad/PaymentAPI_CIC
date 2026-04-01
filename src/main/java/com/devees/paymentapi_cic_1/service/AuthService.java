package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.dto.RequestDTO.LoginRequest;
import com.devees.paymentapi_cic_1.dto.RequestDTO.RegisterRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO login(LoginRequest loginRequest);
    AuthResponseDTO register(RegisterRequestDTO registerRequest);
    AuthResponseDTO refresh(String refreshToken);

    void logout(String authHeader);

}
