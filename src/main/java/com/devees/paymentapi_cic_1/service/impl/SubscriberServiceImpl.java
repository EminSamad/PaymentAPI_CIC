package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.RequestDTO.SubscriberRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.SubscriberResponseDTO;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.exception.DuplicateResourceException;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.mapper.SubscriberMapper;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import com.devees.paymentapi_cic_1.service.EmailService;
import com.devees.paymentapi_cic_1.service.SubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private static final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

    private final SubscriberRepository repository;

    private final EmailService emailService;

    public SubscriberServiceImpl(SubscriberRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public List<SubscriberResponseDTO> getAllSubscribers() {
        log.info("Getting all subscribers");
        return repository.findAllByIsDeletedFalse()
                .stream()
                .map(SubscriberMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriberResponseDTO getById(Long id) {
        log.info("Getting subscriber by id: {}", id);
        SubscriberEntity entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with id: {}", id);
                    return new ResourceNotFoundException("Subscriber not found");
                });
        return SubscriberMapper.toDTO(entity);
    }

    @Override
    public SubscriberResponseDTO getBySubscriberCode(String subscriberCode) {
        log.info("Getting subscriber by code: {}", subscriberCode);
        SubscriberEntity entity = repository.findBySubscriberCodeAndIsDeletedFalse(subscriberCode)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with code: {}", subscriberCode);
                    return new ResourceNotFoundException("Subscriber not found");
                });
        return SubscriberMapper.toDTO(entity);
    }

    @Override
    public SubscriberResponseDTO createSubscriber(SubscriberRequestDTO request) {
        log.info("Creating subscriber with code: {}", request.getSubscriberCode());
        if (repository.findBySubscriberCodeAndIsDeletedFalse(request.getSubscriberCode()).isPresent()) {
            log.warn("Subscriber code already exists: {}", request.getSubscriberCode());
            throw new DuplicateResourceException("Subscriber code already exists: " + request.getSubscriberCode());
        }
        SubscriberEntity entity = SubscriberMapper.toEntity(request);
        repository.save(entity);

        if (entity.getEmail() != null) {
            emailService.sendSubscriberCreatedEmail(entity.getEmail(), entity.getSubscriberCode());
        }
        log.info("Subscriber created successfully: {}", request.getSubscriberCode());
        return SubscriberMapper.toDTO(entity);
    }

    @Override
    public SubscriberResponseDTO updateSubscriber(Long id, SubscriberRequestDTO request) {
        log.info("Updating subscriber with id: {}", id);
        SubscriberEntity entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with id: {}", id);
                    return new ResourceNotFoundException("Subscriber not found");
                });
        SubscriberMapper.updateEntity(entity, request);
        repository.save(entity);
        log.info("Subscriber updated successfully with id: {}", id);
        return SubscriberMapper.toDTO(entity);
    }

    @Override
    public void deleteSubscriber(Long id) {
        log.warn("Deleting subscriber with id: {}", id);
        SubscriberEntity entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> {
                    log.error("Subscriber not found with id: {}", id);
                    return new ResourceNotFoundException("Subscriber not found");
                });
        entity.setDeleted(true);
        repository.save(entity);
        log.info("Subscriber deleted successfully with id: {}", id);
    }
}