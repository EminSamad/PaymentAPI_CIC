package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<PaymentReportDTO> getPaymentReport(String subscriberCode, LocalDate startDate, LocalDate endDate);
}