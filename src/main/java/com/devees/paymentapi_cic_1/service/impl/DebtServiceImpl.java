package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.RequestDTO.DebtRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.DebtResponseDTO;
import com.devees.paymentapi_cic_1.entity.DebtEntity;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.repository.DebtRepository;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import com.devees.paymentapi_cic_1.service.DebtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtServiceImpl implements DebtService {

    private final DebtRepository debtRepository;
    private final SubscriberRepository subscriberRepository;

    public DebtServiceImpl(DebtRepository debtRepository, SubscriberRepository subscriberRepository) {
        this.debtRepository = debtRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public DebtResponseDTO addDebt(DebtRequestDTO request) {
        SubscriberEntity subscriber = subscriberRepository.findBySubscriberCodeAndIsDeletedFalse(request.getSubscriberCode())
                .orElseThrow(() ->  new ResourceNotFoundException("Subscriber not found"));

        DebtEntity debt = DebtEntity.builder()
                .subscriber(subscriber)
                .amount(request.getAmount())
                .note(request.getNote())
                .dateTime(request.getDateTime() != null ? request.getDateTime() : LocalDateTime.now())
                .build();

        debtRepository.save(debt);

        subscriber.setDebt(subscriber.getDebt().add(request.getAmount()));
        subscriberRepository.save(subscriber);

        return new DebtResponseDTO(debt.getId(), debt.getAmount(), debt.getDateTime(), debt.getNote());
    }

    @Override
    public List<DebtResponseDTO> getDebtHistory(String subscriberCode) {
        SubscriberEntity subscriber = subscriberRepository
                .findBySubscriberCodeAndIsDeletedFalse(subscriberCode)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found"));

        return debtRepository.findBySubscriberIdOrderByDateTimeDesc(subscriber.getId())
                .stream()
                .map(d -> new DebtResponseDTO(d.getId(), d.getAmount(), d.getDateTime(), d.getNote()))
                .collect(Collectors.toList());
    }
}
