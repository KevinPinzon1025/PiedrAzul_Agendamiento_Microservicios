package com.piedraazul.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePatientRequest {

    @NotBlank
    private String firstName;

    private String secondName;

    @NotBlank
    private String firstLastName;

    private String secondLastName;

    @NotBlank
    private String documentType;

    @NotBlank
    private String documentNumber;

    @Email
    private String email;

    private String cellNumber;

    private String gender;

    private LocalDate birthDate;
}