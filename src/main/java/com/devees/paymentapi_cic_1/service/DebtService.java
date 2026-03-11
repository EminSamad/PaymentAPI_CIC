package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.dto.RequestDTO.DebtRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.DebtResponseDTO;

import java.util.List;

public interface DebtService {
    DebtResponseDTO addDebt(DebtRequestDTO request);
    List<DebtResponseDTO> getDebtHistory(String subscriberCode);
}
