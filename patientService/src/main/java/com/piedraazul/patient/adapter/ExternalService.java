package com.piedraazul.patient.adapter;

import org.springframework.stereotype.Component;

@Component
public class ExternalService {

    public String getPatientData() {
        return "{\"name\":\"Jose Lopez\",\"email\":\"jose.lopez@example.com\",\"documentNumber\":\"EXT-001\"}";
    }
}
