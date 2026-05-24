package com.piedraazul.patient.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PatientResponse {
    private UUID id;
    private UUID authUserId;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String documentType;
    private String documentNumber;
    private String email;
    private String cellNumber;
    private String gender;
    private LocalDate birthDate;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}