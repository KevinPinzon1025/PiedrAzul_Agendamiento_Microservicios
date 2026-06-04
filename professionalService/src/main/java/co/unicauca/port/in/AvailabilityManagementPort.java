package co.unicauca.port.in;

import co.unicauca.application.dto.AvailabilityResponse;
import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityManagementPort {
    AvailabilityResponse configureAvailability(Long professionalId, List<WorkingDayRequest> workingDays);
    AvailabilityResponse getAvailability(Long professionalId);
    List<SlotResponse> getAvailableSlots(Long professionalId, LocalDate date);
}
