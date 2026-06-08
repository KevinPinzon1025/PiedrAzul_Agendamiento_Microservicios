package co.unicauca.port.in.web;

import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface LegacyAppointmentAgendaWebPort {

    @PostMapping("/appointment/agenda/{professionalId}/availability")
    String configureAvailability(
            @PathVariable Long professionalId,
            @RequestBody List<WorkingDayRequest> workingDays
    );

    @GetMapping("/appointment/agenda/{professionalId}/available-slots")
    List<SlotResponse> getAvailableSlots(
            @PathVariable Long professionalId,
            @RequestParam LocalDate date
    );
}
