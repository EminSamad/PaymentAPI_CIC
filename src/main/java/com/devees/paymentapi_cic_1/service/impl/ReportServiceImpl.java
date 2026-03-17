package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentReportDTO;
import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.exception.ExcelExportException;
import com.devees.paymentapi_cic_1.repository.PaymentRepository;
import com.devees.paymentapi_cic_1.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
 import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final PaymentRepository paymentRepository;

    @Override
    public List<PaymentReportDTO> getPaymentReport(String subscriberCode, LocalDate startDate, LocalDate endDate) {
        log.info("Getting payment report. SubscriberCode: {}, StartDate: {}, EndDate: {}", subscriberCode, startDate, endDate);

        Specification<PaymentEntity> spec = Specification.where(
                (root, query, cb) -> cb.isFalse(root.get("deleted"))
        );

        if (subscriberCode != null && !subscriberCode.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("subscriber").get("subscriberCode"), subscriberCode));
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("dateTime"), startDate.atStartOfDay()));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("dateTime"), endDate.atTime(23, 59, 59)));
        }

        return paymentRepository.findAll(spec)
                .stream()
                .map(p -> new PaymentReportDTO(
                        p.getSubscriber().getSubscriberCode(),
                        p.getDateTime(),
                        p.getBalance()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportPaymentsToExcel(String subscriberCode, LocalDate startDate, LocalDate endDate) {
        log.info("Exporting payments to Excel");

        List<PaymentReportDTO> payments = getPaymentReport(subscriberCode, startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payments");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Subscriber Code");
            header.createCell(1).setCellValue("Payment Date");
            header.createCell(2).setCellValue("Amount (AZN)");

            // Data
            int rowNum = 1;
            for (PaymentReportDTO p : payments) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getSubscriberCode());
                row.createCell(1).setCellValue(p.getPaymentDate().toString());
                row.createCell(2).setCellValue(p.getAmount().doubleValue());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            log.error("Error exporting to Excel: {}", e.getMessage());
            throw new ExcelExportException("Error exporting to Excel");
        }
    }


}