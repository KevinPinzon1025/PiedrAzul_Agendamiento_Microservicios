package com.piedraazul.patient.adapter.service;

import com.piedraazul.patient.adapter.PatientDataProvider;
import com.piedraazul.patient.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalPatientQueryService {

    private final PatientDataProvider patientDataProvider;

    public Patient getExternalPatient() {
        return patientDataProvider.getPatient();
    }
}
