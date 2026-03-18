package com.devees.paymentapi_cic_1.service.task;

import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.repository.PaymentRepository;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import com.devees.paymentapi_cic_1.service.EmailService;
import com.devees.paymentapi_cic_1.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledJobService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledJobService.class);

    private final SubscriberRepository subscriberRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final ReportService reportService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDebtReminderEmails() {
        log.info("Running debt reminder job...");
        List<SubscriberEntity> debtors = subscriberRepository.findAllByIsDeletedFalse()
                .stream()
                .filter(s -> s.getDebt().compareTo(BigDecimal.ZERO) > 0)
                .filter(s -> s.getEmail() != null)
                .toList();

        for (SubscriberEntity subscriber : debtors) {
            emailService.sendDebtAddedEmail(
                    subscriber.getEmail(),
                    subscriber.getSubscriberCode(),
                    subscriber.getDebt()
            );
            log.info("Debt reminder sent to: {}", subscriber.getSubscriberCode());
        }
        log.info("Debt reminder job completed. Total: {}", debtors.size());
    }

    @Scheduled(cron = "0 0 8 1 * *")
    public void reportSubscribersWithNoPayments() {
        log.info("Running no-payment subscribers report job...");
        List<SubscriberEntity> noPaymentSubs = subscriberRepository.findAllByIsDeletedFalse()
                .stream()
                .filter(s -> paymentRepository.findBySubscriber(s).isEmpty())
                .toList();

        log.warn("Subscribers with no payments: {}", noPaymentSubs.size());
        noPaymentSubs.forEach(s -> log.warn("No payment subscriber: {}", s.getSubscriberCode()));
        log.info("No-payment report job completed.");
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanDeletedRecords() {
        log.info("Running clean deleted records job...");
        List<SubscriberEntity> deleted = subscriberRepository.findAll()
                .stream()
                .filter(SubscriberEntity::isDeleted)
                .toList();

        subscriberRepository.deleteAll(deleted);
        log.info("Cleaned {} deleted subscriber records.", deleted.size());
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanExpiredRefreshTokens() {
        log.info("Running clean expired refresh tokens job...");
        log.info("Clean expired refresh tokens job completed.");
    }

    @Scheduled(cron = "0 0 7 * * MON")
    public void generateWeeklyExcelReport() {
        log.info("Running weekly Excel report job...");
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        byte[] report = reportService.exportPaymentsToExcel(null, startDate, endDate);
        log.info("Weekly Excel report generated. Size: {} bytes", report.length);
    }
}