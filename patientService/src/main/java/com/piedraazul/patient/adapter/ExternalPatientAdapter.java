package com.piedraazul.patient.adapter;

import com.piedraazul.patient.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExternalPatientAdapter implements PatientDataProvider {

    private final ExternalService externalService;

    @Override
    public Patient getPatient() {
        String patientJson = externalService.getPatientData();
        JSONObject jsonObject = new JSONObject(patientJson);

        String fullName = jsonObject.optString("name", "").trim();
        String[] nameParts = fullName.isBlank() ? new String[0] : fullName.split("\\s+");

        String firstName = nameParts.length > 0 ? nameParts[0] : "Paciente";
        String firstLastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "Externo";
        String secondName = nameParts.length > 2 ? nameParts[1] : null;
        String secondLastName = nameParts.length > 3 ? nameParts[nameParts.length - 2] : null;

        return Patient.builder()
                .id(UUID.randomUUID())
                .authUserId(null)
                .firstName(firstName)
                .secondName(secondName)
                .firstLastName(firstLastName)
                .secondLastName(secondLastName)
                .documentType("EXTERNAL")
                .documentNumber(jsonObject.optString("documentNumber", "EXT-" + UUID.randomUUID()))
                .email(jsonObject.optString("email", null))
                .cellNumber(jsonObject.optString("cellNumber", null))
                .gender(jsonObject.optString("gender", null))
                .birthDate(null)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }
}
