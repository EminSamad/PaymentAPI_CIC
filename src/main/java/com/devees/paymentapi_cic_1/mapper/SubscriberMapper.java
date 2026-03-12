package com.devees.paymentapi_cic_1.mapper;

import com.devees.paymentapi_cic_1.dto.RequestDTO.SubscriberRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.SubscriberResponseDTO;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;

import java.math.BigDecimal;

public class SubscriberMapper {

    public static SubscriberResponseDTO toDTO(SubscriberEntity entity) {
        return new SubscriberResponseDTO(
                entity.getId(),
                entity.getSubscriberCode(),
                entity.getFullName(),
                entity.getIdNumber(),
                entity.getSubscriberType(),
                entity.getDebt(),
                entity.getBalance()
        );
    }

    public static SubscriberEntity toEntity(SubscriberRequestDTO request) {
        SubscriberEntity entity = new SubscriberEntity();
        entity.setFullName(request.getFullName());
        entity.setIdNumber(request.getIdNumber());
        entity.setSubscriberCode(request.getSubscriberCode());
        entity.setSubscriberType(request.getSubscriberType());
        entity.setDebt(request.getDebt() != null ? request.getDebt() : BigDecimal.ZERO);
        entity.setBalance(request.getBalance() != null ? request.getBalance() : BigDecimal.ZERO);
        entity.setDeleted(false);
        entity.setEmail(request.getEmail());
        return entity;
    }

    public static void updateEntity(SubscriberEntity entity, SubscriberRequestDTO request) {
        entity.setFullName(request.getFullName());
        entity.setIdNumber(request.getIdNumber());
        entity.setSubscriberType(request.getSubscriberType());
        entity.setDebt(request.getDebt() != null ? request.getDebt() : entity.getDebt());
        entity.setBalance(request.getBalance() != null ? request.getBalance() : entity.getBalance());
    }
}