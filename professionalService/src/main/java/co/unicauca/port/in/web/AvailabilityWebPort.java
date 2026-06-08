package co.unicauca.port.in.web;

import co.unicauca.application.dto.AvailabilityResponse;
import co.unicauca.application.dto.SlotResponse;
import co.unicauca.application.dto.WorkingDayRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityWebPort {

    @PutMapping("/professionals/{professionalId}/availability")
    AvailabilityResponse configureAvailability(
            @PathVariable Long professionalId,
            @RequestBody List<WorkingDayRequest> workingDays
    );

    @GetMapping("/professionals/{professionalId}/availability")
    AvailabilityResponse getAvailability(@PathVariable Long professionalId);

    @GetMapping("/professionals/{professionalId}/available-slots")
    List<SlotResponse> getAvailableSlots(
            @PathVariable Long professionalId,
            @RequestParam LocalDate date
    );
}
