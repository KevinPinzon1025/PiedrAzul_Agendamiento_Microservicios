package co.unicauca.Controller;

import co.unicauca.Service.ProfessionalWorkingDayService;
import co.unicauca.infra.dto.SlotResponseDTO;
import co.unicauca.infra.dto.WorkingDayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment/agenda")
public class ProfessionalAvailabilityController {


    private final ProfessionalWorkingDayService service;

    public ProfessionalAvailabilityController(
            ProfessionalWorkingDayService service
    ) {
        this.service = service;
    }

    @PostMapping("/{professionalId}/availability")
    public ResponseEntity<String> configureAvailability(
            @PathVariable Long professionalId,
            @RequestBody List<WorkingDayDTO> workingDays
    ) {

        service.configureAvailability(
                professionalId,
                workingDays
        );

        return ResponseEntity.ok(
                "Disponibilidad configurada correctamente"
        );
    }

    @GetMapping("/availability")
    public ResponseEntity<List<WorkingDayDTO>> listAvailability() {

        return ResponseEntity.ok(
                service.findAllConfiguredSlots()
        );
    }

    @PutMapping("/availability/{id}")
    public ResponseEntity<WorkingDayDTO> updateAvailability(
            @PathVariable Long id,
            @RequestBody WorkingDayDTO workingDay
    ) {

        return ResponseEntity.ok(
                service.updateWorkingDay(id, workingDay)
        );
    }

    @DeleteMapping("/availability/{id}")
    public ResponseEntity<Void> deleteAvailability(
            @PathVariable Long id
    ) {

        service.deleteWorkingDay(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{professionalId}/available-slots")
    public ResponseEntity<List<SlotResponseDTO>>
    getAvailableSlots(
            @PathVariable Long professionalId,
            @RequestParam LocalDate date
    ) {

        List<SlotResponseDTO> slots =
                service.getAvailableSlots(
                        professionalId,
                        date
                );

        return ResponseEntity.ok(slots);
    }
}
