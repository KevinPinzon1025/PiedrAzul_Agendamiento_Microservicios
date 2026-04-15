package com.piedraazul.patient.service;

import com.piedraazul.patient.dto.CreatePatientRequest;
import com.piedraazul.patient.dto.UpdatePatientRequest;
import com.piedraazul.patient.entity.Patient;
import com.piedraazul.patient.exception.PatientAlreadyExistsException;
import com.piedraazul.patient.exception.PatientNotFoundException;
import com.piedraazul.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository repository;

    @InjectMocks
    private PatientService patientService;

    private CreatePatientRequest createRequest;
    private UpdatePatientRequest updateRequest;
    private Patient patient;
    private UUID patientId;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();

        createRequest = new CreatePatientRequest();
        createRequest.setFirstName("Juan");
        createRequest.setSecondName("Carlos");
        createRequest.setFirstLastName("Perez");
        createRequest.setSecondLastName("Gomez");
        createRequest.setDocumentType("CC");
        createRequest.setDocumentNumber("123456");
        createRequest.setEmail("juan@test.com");
        createRequest.setCellNumber("3001234567");
        createRequest.setGender("M");
        createRequest.setBirthDate(LocalDate.of(2000, 1, 1));

        updateRequest = new UpdatePatientRequest();
        updateRequest.setFirstName("Juan Updated");
        updateRequest.setSecondName("Carlos");
        updateRequest.setFirstLastName("Perez");
        updateRequest.setSecondLastName("Gomez");
        updateRequest.setDocumentType("CC");
        updateRequest.setDocumentNumber("123456");
        updateRequest.setEmail("juan.updated@test.com");
        updateRequest.setCellNumber("3000000000");
        updateRequest.setGender("M");
        updateRequest.setBirthDate(LocalDate.of(2000, 1, 1));
        updateRequest.setActive(false);

        patient = Patient.builder()
                .id(patientId)
                .authUserId(null)
                .firstName("Juan")
                .secondName("Carlos")
                .firstLastName("Perez")
                .secondLastName("Gomez")
                .documentType("CC")
                .documentNumber("123456")
                .email("juan@test.com")
                .cellNumber("3001234567")
                .gender("M")
                .birthDate(LocalDate.of(2000, 1, 1))
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }

    @Test
    void create_shouldSavePatient_whenDocumentDoesNotExist() {
        when(repository.existsByDocumentNumber(createRequest.getDocumentNumber())).thenReturn(false);
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.create(createRequest);

        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        assertEquals("123456", result.getDocumentNumber());
        assertTrue(result.isActive());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());

        verify(repository).existsByDocumentNumber("123456");
        verify(repository).save(any(Patient.class));
    }

    @Test
    void create_shouldThrowException_whenDocumentAlreadyExists() {
        when(repository.existsByDocumentNumber(createRequest.getDocumentNumber())).thenReturn(true);

        assertThrows(PatientAlreadyExistsException.class, () -> patientService.create(createRequest));

        verify(repository).existsByDocumentNumber("123456");
        verify(repository, never()).save(any(Patient.class));
    }

    @Test
    void findAll_shouldReturnPatients() {
        when(repository.findAll()).thenReturn(List.of(patient));

        List<Patient> result = patientService.findAll();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnPatient_whenExists() {
        when(repository.findById(patientId)).thenReturn(Optional.of(patient));

        Patient result = patientService.findById(patientId);

        assertEquals(patientId, result.getId());
        verify(repository).findById(patientId);
    }

    @Test
    void findById_shouldThrowException_whenNotExists() {
        when(repository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.findById(patientId));

        verify(repository).findById(patientId);
    }

    @Test
    void update_shouldUpdatePatient_whenExistsAndDocumentIsSame() {
        when(repository.findById(patientId)).thenReturn(Optional.of(patient));
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.update(patientId, updateRequest);

        assertEquals("Juan Updated", result.getFirstName());
        assertEquals("juan.updated@test.com", result.getEmail());
        assertFalse(result.isActive());
        assertNotNull(result.getUpdatedAt());

        verify(repository).findById(patientId);
        verify(repository, never()).existsByDocumentNumber(any());
        verify(repository).save(patient);
    }

    @Test
    void update_shouldThrowException_whenPatientDoesNotExist() {
        when(repository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.update(patientId, updateRequest));

        verify(repository).findById(patientId);
        verify(repository, never()).save(any(Patient.class));
    }

    @Test
    void update_shouldThrowException_whenNewDocumentAlreadyExists() {
        updateRequest.setDocumentNumber("999999");

        when(repository.findById(patientId)).thenReturn(Optional.of(patient));
        when(repository.existsByDocumentNumber("999999")).thenReturn(true);

        assertThrows(PatientAlreadyExistsException.class, () -> patientService.update(patientId, updateRequest));

        verify(repository).findById(patientId);
        verify(repository).existsByDocumentNumber("999999");
        verify(repository, never()).save(any(Patient.class));
    }

    @Test
    void linkAuthUser_shouldLinkUser_whenPatientExists() {
        UUID authUserId = UUID.randomUUID();

        when(repository.findById(patientId)).thenReturn(Optional.of(patient));
        when(repository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.linkAuthUser(patientId, authUserId);

        assertEquals(authUserId, result.getAuthUserId());
        assertNotNull(result.getUpdatedAt());

        verify(repository).findById(patientId);
        verify(repository).save(patient);
    }

    @Test
    void linkAuthUser_shouldThrowException_whenPatientNotExists() {
        UUID authUserId = UUID.randomUUID();
        when(repository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.linkAuthUser(patientId, authUserId));

        verify(repository).findById(patientId);
        verify(repository, never()).save(any(Patient.class));
    }
}