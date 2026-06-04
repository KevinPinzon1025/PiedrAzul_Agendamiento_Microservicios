package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.AvailabilityResponse;
import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import co.unicauca.port.in.AvailabilityManagementPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/professionals/{professionalId}")
public class AvailabilityController {
    private final AvailabilityManagementPort availabilityManagementPort;

    public AvailabilityController(AvailabilityManagementPort availabilityManagementPort) {
        this.availabilityManagementPort = availabilityManagementPort;
    }

    @PutMapping("/availability")
    public ResponseEntity<AvailabilityResponse> configureAvailability(@PathVariable Long professionalId,
                                                                      @RequestBody List<WorkingDayRequest> workingDays) {
        return ResponseEntity.ok(availabilityManagementPort.configureAvailability(professionalId, workingDays));
    }

    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponse> getAvailability(@PathVariable Long professionalId) {
        return ResponseEntity.ok(availabilityManagementPort.getAvailability(professionalId));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(@PathVariable Long professionalId,
                                                                @RequestParam LocalDate date) {
        return ResponseEntity.ok(availabilityManagementPort.getAvailableSlots(professionalId, date));
    }
}
