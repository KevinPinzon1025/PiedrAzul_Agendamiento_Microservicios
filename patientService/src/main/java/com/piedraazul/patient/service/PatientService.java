package com.piedraazul.patient.service;

import com.piedraazul.patient.dto.CreatePatientRequest;
import com.piedraazul.patient.dto.UpdatePatientRequest;
import com.piedraazul.patient.entity.Patient;
import com.piedraazul.patient.exception.PatientAlreadyExistsException;
import com.piedraazul.patient.exception.PatientNotFoundException;
import com.piedraazul.patient.infra.event.AuthUserCreatedEvent;
import com.piedraazul.patient.infra.event.PatientCreatedEvent;
import com.piedraazul.patient.infra.messaging.PatientEventPublisher;
import com.piedraazul.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository repository;
    private final PatientEventPublisher eventPublisher;

    public Patient create(CreatePatientRequest request) {
        if (repository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new PatientAlreadyExistsException("Ya existe un paciente con ese documento");
        }

        Patient patient = Patient.builder()
                .id(UUID.randomUUID())
                .authUserId(null)
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .firstLastName(request.getFirstLastName())
                .secondLastName(request.getSecondLastName())
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .email(request.getEmail())
                .cellNumber(request.getCellNumber())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        Patient savedPatient = repository.save(patient);
        eventPublisher.publish(PatientCreatedEvent.from(savedPatient));
        return savedPatient;
    }

    public Patient createFromAuthUser(AuthUserCreatedEvent event) {
        if (event == null || event.getDocumentNumber() == null || event.getDocumentNumber().isBlank()) {
            throw new IllegalArgumentException("El evento de usuario creado no tiene documento");
        }
        if (event.getRole() != null && !"PATIENT".equalsIgnoreCase(event.getRole())) {
            return null;
        }

        return repository.findByDocumentNumber(event.getDocumentNumber())
                .orElseGet(() -> {
                    Patient patient = Patient.builder()
                            .id(UUID.randomUUID())
                            .authUserId(null)
                            .firstName(event.getFirstName())
                            .secondName(event.getSecondName())
                            .firstLastName(event.getFirstLastName())
                            .secondLastName(event.getSecondLastName())
                            .documentType("CC")
                            .documentNumber(event.getDocumentNumber())
                            .email(event.getEmail())
                            .cellNumber(event.getPhone())
                            .gender(event.getGender())
                            .birthDate(event.getBirthDate())
                            .active(event.isActive())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(null)
                            .build();

                    Patient savedPatient = repository.save(patient);
                    eventPublisher.publish(PatientCreatedEvent.from(savedPatient));
                    return savedPatient;
                });
    }

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));
    }

    public Patient findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));
    }

    public Patient findByAuthUserId(UUID authUserId) {
        return repository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));
    }

    public List<Patient> searchByName(String name) {
        return repository.findByFirstNameContainingIgnoreCaseOrFirstLastNameContainingIgnoreCase(name, name);
    }

    public List<Patient> searchByEmail(String email) {
        return repository.findByEmailContainingIgnoreCase(email);
    }

    public Patient update(UUID id, UpdatePatientRequest request) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));

        if (!patient.getDocumentNumber().equals(request.getDocumentNumber())
                && repository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new PatientAlreadyExistsException("Ya existe un paciente con ese documento");
        }

        patient.setFirstName(request.getFirstName());
        patient.setSecondName(request.getSecondName());
        patient.setFirstLastName(request.getFirstLastName());
        patient.setSecondLastName(request.getSecondLastName());
        patient.setDocumentType(request.getDocumentType());
        patient.setDocumentNumber(request.getDocumentNumber());
        patient.setEmail(request.getEmail());
        patient.setCellNumber(request.getCellNumber());
        patient.setGender(request.getGender());
        patient.setBirthDate(request.getBirthDate());
        patient.setActive(request.getActive() != null ? request.getActive() : patient.isActive());
        patient.setUpdatedAt(LocalDateTime.now());

        return repository.save(patient);
    }

    public Patient linkAuthUser(UUID patientId, UUID authUserId) {
        Patient patient = repository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado"));

        patient.setAuthUserId(authUserId);
        patient.setUpdatedAt(LocalDateTime.now());
        return repository.save(patient);
    }
}
