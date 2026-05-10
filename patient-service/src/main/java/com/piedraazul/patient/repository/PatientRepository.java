package com.piedraazul.patient.repository;

import com.piedraazul.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByDocumentNumber(String documentNumber);

    Optional<Patient> findByAuthUserId(UUID authUserId);

    List<Patient> findByFirstNameContainingIgnoreCaseOrFirstLastNameContainingIgnoreCase(
            String firstName, String firstLastName
    );

    List<Patient> findByEmailContainingIgnoreCase(String email);

    boolean existsByDocumentNumber(String documentNumber);
}