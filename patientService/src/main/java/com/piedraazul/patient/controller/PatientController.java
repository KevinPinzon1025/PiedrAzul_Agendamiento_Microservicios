package com.piedraazul.patient.controller;

import com.piedraazul.patient.dto.CreatePatientRequest;
import com.piedraazul.patient.dto.LinkAuthUserRequest;
import com.piedraazul.patient.dto.PatientResponse;
import com.piedraazul.patient.dto.UpdatePatientRequest;
import com.piedraazul.patient.mapper.PatientMapper;
import com.piedraazul.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service;
    private final PatientMapper mapper;

    @PostMapping
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        return mapper.toResponse(service.create(request));
    }

    @GetMapping
    public List<PatientResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PatientResponse findById(@PathVariable UUID id) {
        return mapper.toResponse(service.findById(id));
    }

    @GetMapping("/search")
    public Object search(
            @RequestParam(required = false) String documentNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UUID authUserId
    ) {
        if (documentNumber != null) {
            return mapper.toResponse(service.findByDocumentNumber(documentNumber));
        }
        if (authUserId != null) {
            return mapper.toResponse(service.findByAuthUserId(authUserId));
        }
        if (name != null) {
            return service.searchByName(name).stream().map(mapper::toResponse).toList();
        }
        if (email != null) {
            return service.searchByEmail(email).stream().map(mapper::toResponse).toList();
        }

        throw new IllegalArgumentException("Debe enviar al menos un criterio de búsqueda");
    }

    @PutMapping("/{id}")
    public PatientResponse update(@PathVariable UUID id, @Valid @RequestBody UpdatePatientRequest request) {
        return mapper.toResponse(service.update(id, request));
    }

    @PatchMapping("/{id}/auth-user")
    public PatientResponse linkAuthUser(@PathVariable UUID id, @Valid @RequestBody LinkAuthUserRequest request) {
        return mapper.toResponse(service.linkAuthUser(id, request.getAuthUserId()));
    }
}