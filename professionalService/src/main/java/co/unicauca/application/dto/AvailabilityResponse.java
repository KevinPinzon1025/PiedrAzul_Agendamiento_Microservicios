package co.unicauca.application.dto;

import java.util.List;

public record AvailabilityResponse(Long professionalId, List<WorkingDayResponse> workingDays) {}
