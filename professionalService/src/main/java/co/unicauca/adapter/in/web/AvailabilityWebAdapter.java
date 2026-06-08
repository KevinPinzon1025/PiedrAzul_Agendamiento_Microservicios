package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.AvailabilityResponse;
import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import co.unicauca.application.service.ProfessionalApplicationService;
import co.unicauca.port.in.web.AvailabilityWebPort;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AvailabilityWebAdapter implements AvailabilityWebPort {

    private final ProfessionalApplicationService professionalApplicationService;

    public AvailabilityWebAdapter(
            ProfessionalApplicationService professionalApplicationService
    ) {
        this.professionalApplicationService = professionalApplicationService;
    }

    @Override
    public AvailabilityResponse configureAvailability(
            Long professionalId,
            List<WorkingDayRequest> workingDays
    ) {
        return professionalApplicationService.configureAvailability(
                professionalId,
                workingDays
        );
    }

    @Override
    public AvailabilityResponse getAvailability(Long professionalId) {
        return professionalApplicationService.getAvailability(professionalId);
    }

    @Override
    public List<SlotResponse> getAvailableSlots(
            Long professionalId,
            LocalDate date
    ) {
        return professionalApplicationService.getAvailableSlots(
                professionalId,
                date
        );
    }
}
