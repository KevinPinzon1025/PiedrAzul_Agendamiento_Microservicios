package co.unicauca.frontend.dto;

import java.time.LocalDate;

public class CreatePatientRequestDTO {

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

    public CreatePatientRequestDTO() {
    }

    public CreatePatientRequestDTO(
            String firstName,
            String secondName,
            String firstLastName,
            String secondLastName,
            String documentType,
            String documentNumber,
            String email,
            String cellNumber,
            String gender,
            LocalDate birthDate
    ) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.email = email;
        this.cellNumber = cellNumber;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}