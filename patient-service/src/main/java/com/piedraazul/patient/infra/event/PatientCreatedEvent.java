package com.piedraazul.patient.infra.event;

import com.piedraazul.patient.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreatedEvent implements Serializable {

    private UUID id;
    private String patName;

    public static PatientCreatedEvent from(Patient patient) {
        return new PatientCreatedEvent(patient.getId(), buildPatientName(patient));
    }

    private static String buildPatientName(Patient patient) {
        return Stream.of(
                        patient.getFirstName(),
                        patient.getSecondName(),
                        patient.getFirstLastName(),
                        patient.getSecondLastName()
                )
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.joining(" "));
    }
}
