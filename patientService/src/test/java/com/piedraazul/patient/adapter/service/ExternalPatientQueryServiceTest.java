package com.piedraazul.patient.adapter.service;

import com.piedraazul.patient.adapter.PatientDataProvider;
import com.piedraazul.patient.entity.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalPatientQueryServiceTest {

    @Mock
    private PatientDataProvider patientDataProvider;

    @InjectMocks
    private ExternalPatientQueryService service;

    @Test
    void getExternalPatient_shouldDelegateToProvider() {
        Patient patient = Patient.builder()
                .id(UUID.randomUUID())
                .firstName("Jose")
                .firstLastName("Lopez")
                .documentType("EXTERNAL")
                .documentNumber("EXT-001")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(patientDataProvider.getPatient()).thenReturn(patient);

        Patient result = service.getExternalPatient();

        assertEquals("Jose", result.getFirstName());
        assertEquals("Lopez", result.getFirstLastName());
        verify(patientDataProvider, times(1)).getPatient();
    }
}