package com.piedraazul.patient.adapter;

import com.piedraazul.patient.entity.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPatientAdapterTest {

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private ExternalPatientAdapter adapter;

    @Test
    void getPatient_shouldAdaptJsonCorrectly() {
        when(externalService.getPatientData())
                .thenReturn("{\"name\":\"Jose Lopez\",\"email\":\"jose.lopez@example.com\",\"documentNumber\":\"EXT-001\"}");

        Patient result = adapter.getPatient();

        assertNotNull(result);
        assertEquals("Jose", result.getFirstName());
        assertEquals("Lopez", result.getFirstLastName());
        assertEquals("jose.lopez@example.com", result.getEmail());
        assertEquals("EXT-001", result.getDocumentNumber());
        assertEquals("EXTERNAL", result.getDocumentType());
        assertTrue(result.isActive());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void getPatient_shouldUseDefaultValues_whenJsonIsIncomplete() {
        when(externalService.getPatientData())
                .thenReturn("{\"name\":\"\"}");

        Patient result = adapter.getPatient();

        assertNotNull(result);
        assertEquals("Paciente", result.getFirstName());
        assertEquals("Externo", result.getFirstLastName());
        assertEquals("EXTERNAL", result.getDocumentType());
        assertNotNull(result.getDocumentNumber());
        assertTrue(result.getDocumentNumber().startsWith("EXT-"));
        assertTrue(result.isActive());
    }

    @Test
    void getPatient_shouldSplitFullNameCorrectly_whenHasMoreThanTwoNames() {
        when(externalService.getPatientData())
                .thenReturn("{\"name\":\"Juan Carlos Perez Gomez\",\"email\":\"jc@test.com\"}");

        Patient result = adapter.getPatient();

        assertEquals("Juan", result.getFirstName());
        assertEquals("Carlos", result.getSecondName());
        assertEquals("Gomez", result.getFirstLastName());
        assertEquals("Perez", result.getSecondLastName());
        assertEquals("jc@test.com", result.getEmail());
    }
}