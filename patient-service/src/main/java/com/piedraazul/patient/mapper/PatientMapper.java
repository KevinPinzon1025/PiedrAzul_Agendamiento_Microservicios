package com.piedraazul.patient.mapper;

import com.piedraazul.patient.dto.PatientResponse;
import com.piedraazul.patient.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .authUserId(patient.getAuthUserId())
                .firstName(patient.getFirstName())
                .secondName(patient.getSecondName())
                .firstLastName(patient.getFirstLastName())
                .secondLastName(patient.getSecondLastName())
                .documentType(patient.getDocumentType())
                .documentNumber(patient.getDocumentNumber())
                .email(patient.getEmail())
                .cellNumber(patient.getCellNumber())
                .gender(patient.getGender())
                .birthDate(patient.getBirthDate())
                .active(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}