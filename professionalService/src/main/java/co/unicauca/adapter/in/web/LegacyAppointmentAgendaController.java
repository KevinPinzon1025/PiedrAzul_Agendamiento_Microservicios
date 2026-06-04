package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import co.unicauca.port.in.AvailabilityManagementPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment/agenda")
public class LegacyAppointmentAgendaController {
    private final AvailabilityManagementPort availabilityManagementPort;

    public LegacyAppointmentAgendaController(AvailabilityManagementPort availabilityManagementPort) {
        this.availabilityManagementPort = availabilityManagementPort;
    }

    @PostMapping("/{professionalId}/availability")
    public ResponseEntity<String> configureAvailability(@PathVariable Long professionalId,
                                                        @RequestBody List<WorkingDayRequest> workingDays) {
        availabilityManagementPort.configureAvailability(professionalId, workingDays);
        return ResponseEntity.ok("Disponibilidad configurada correctamente");
    }

    @GetMapping("/{professionalId}/available-slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(@PathVariable Long professionalId,
                                                                @RequestParam LocalDate date) {
        return ResponseEntity.ok(availabilityManagementPort.getAvailableSlots(professionalId, date));
    }
}
