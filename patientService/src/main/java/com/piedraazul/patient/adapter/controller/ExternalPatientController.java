package com.piedraazul.patient.adapter.controller;

import com.piedraazul.patient.adapter.service.ExternalPatientQueryService;
import com.piedraazul.patient.dto.PatientResponse;
import com.piedraazul.patient.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients/external")
@RequiredArgsConstructor
public class ExternalPatientController {

    private final ExternalPatientQueryService externalPatientQueryService;
    private final PatientMapper patientMapper;

    @GetMapping
    public PatientResponse getExternalPatient() {
        return patientMapper.toResponse(externalPatientQueryService.getExternalPatient());
    }
}
