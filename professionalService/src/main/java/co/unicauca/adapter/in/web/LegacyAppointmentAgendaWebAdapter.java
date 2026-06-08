package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import co.unicauca.application.service.ProfessionalApplicationService;
import co.unicauca.port.in.web.LegacyAppointmentAgendaWebPort;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LegacyAppointmentAgendaWebAdapter
        implements LegacyAppointmentAgendaWebPort {

    private final ProfessionalApplicationService professionalApplicationService;

    public LegacyAppointmentAgendaWebAdapter(
            ProfessionalApplicationService professionalApplicationService
    ) {
        this.professionalApplicationService = professionalApplicationService;
    }

    @Override
    public String configureAvailability(
            Long professionalId,
            List<WorkingDayRequest> workingDays
    ) {
        professionalApplicationService.configureAvailability(
                professionalId,
                workingDays
        );
        return "Disponibilidad configurada correctamente";
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
