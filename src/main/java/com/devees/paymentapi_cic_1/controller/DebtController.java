package com.devees.paymentapi_cic_1.controller;

import com.devees.paymentapi_cic_1.dto.RequestDTO.DebtRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.DebtResponseDTO;
import com.devees.paymentapi_cic_1.service.DebtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }



    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DebtResponseDTO> addDebt(@RequestBody DebtRequestDTO request) {
        DebtResponseDTO response = debtService.addDebt(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subscriberCode}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<DebtResponseDTO>> getDebtHistory(@PathVariable String subscriberCode) {
        List<DebtResponseDTO> history = debtService.getDebtHistory(subscriberCode);
        return ResponseEntity.ok(history);
    }
}
