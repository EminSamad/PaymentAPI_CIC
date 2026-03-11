package com.devees.paymentapi_cic_1.service;

import com.devees.paymentapi_cic_1.dto.RequestDTO.SubscriberRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.SubscriberResponseDTO;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface SubscriberService {

    List<SubscriberResponseDTO> getAllSubscribers();

    SubscriberResponseDTO getById(Long id);

    SubscriberResponseDTO createSubscriber(SubscriberRequestDTO request);

    SubscriberResponseDTO updateSubscriber(Long id, SubscriberRequestDTO request);

    void deleteSubscriber(Long id);

    @Nullable SubscriberResponseDTO getBySubscriberCode(String subscriberCode);


}