package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.ResponseDTO.PaymentReportDTO;
import com.devees.paymentapi_cic_1.entity.PaymentEntity;
import com.devees.paymentapi_cic_1.repository.PaymentRepository;
import com.devees.paymentapi_cic_1.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


}