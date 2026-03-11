package com.devees.paymentapi_cic_1.service.impl;

import com.devees.paymentapi_cic_1.dto.RequestDTO.SubscriberRequestDTO;
import com.devees.paymentapi_cic_1.dto.ResponseDTO.SubscriberResponseDTO;
import com.devees.paymentapi_cic_1.entity.SubscriberEntity;
import com.devees.paymentapi_cic_1.entity.SubscriberType;
import com.devees.paymentapi_cic_1.exception.DuplicateResourceException;
import com.devees.paymentapi_cic_1.exception.ResourceNotFoundException;
import com.devees.paymentapi_cic_1.repository.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository repository;

    @InjectMocks
    private SubscriberServiceImpl service;

    private SubscriberEntity subscriber;
    private SubscriberRequestDTO request;

    @BeforeEach
    void setUp() {
        subscriber = new SubscriberEntity();
        subscriber.setId(1L);
        subscriber.setSubscriberCode("SUB001");
        subscriber.setFullName("Emin Test");
        subscriber.setIdNumber("AZE123");
        subscriber.setSubscriberType(SubscriberType.INDIVIDUAL);
        subscriber.setDebt(BigDecimal.ZERO);
        subscriber.setBalance(BigDecimal.ZERO);
        subscriber.setDeleted(false);

        request = new SubscriberRequestDTO();
        request.setSubscriberCode("SUB001");
        request.setFullName("Emin Test");
        request.setIdNumber("AZE123");
        request.setSubscriberType(SubscriberType.INDIVIDUAL);
        request.setDebt(BigDecimal.ZERO);
        request.setBalance(BigDecimal.ZERO);
    }

    @Test
    void getById_ShouldReturnSubscriber_WhenExists() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(subscriber));

        SubscriberResponseDTO result = service.getById(1L);

        assertNotNull(result);
        assertEquals("SUB001", result.getSubscriberCode());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void getAllSubscribers_ShouldReturnList() {
        when(repository.findAllByIsDeletedFalse()).thenReturn(List.of(subscriber));

        List<SubscriberResponseDTO> result = service.getAllSubscribers();

        assertEquals(1, result.size());
    }

    @Test
    void createSubscriber_ShouldCreate_WhenCodeNotExists() {
        when(repository.findBySubscriberCodeAndIsDeletedFalse("SUB001")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(subscriber);

        SubscriberResponseDTO result = service.createSubscriber(request);

        assertNotNull(result);
        verify(repository, times(1)).save(any());
    }

    @Test
    void createSubscriber_ShouldThrowException_WhenCodeExists() {
        when(repository.findBySubscriberCodeAndIsDeletedFalse("SUB001")).thenReturn(Optional.of(subscriber));

        assertThrows(DuplicateResourceException.class, () -> service.createSubscriber(request));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteSubscriber_ShouldMarkDeleted_WhenExists() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(subscriber));

        service.deleteSubscriber(1L);

        assertTrue(subscriber.isDeleted());
        verify(repository, times(1)).save(subscriber);
    }

    @Test
    void deleteSubscriber_ShouldThrowException_WhenNotFound() {
        when(repository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteSubscriber(1L));
    }
}