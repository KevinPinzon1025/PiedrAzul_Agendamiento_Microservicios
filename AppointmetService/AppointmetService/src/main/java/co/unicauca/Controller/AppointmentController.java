package co.unicauca.Controller;

import co.unicauca.Entity.facade.AppointmentFacade;
import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Service.AppointmentService;
import co.unicauca.Service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ProfessionalService professionalService;
    private final AppointmentFacade appointmentFacade;

    public AppointmentController(AppointmentFacade appointmentFacade, AppointmentService appointmentService, ProfessionalService professionalService) {
        this.appointmentFacade = appointmentFacade;
        this.appointmentService = appointmentService;
        this.professionalService = professionalService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> listAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Appointment>> findByProfessionalAndDate(
            @RequestParam String professional,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                appointmentService.findByProfessionalAndDate(
                        professional,
                        date
                )
        );
    }

    @GetMapping("/professionals")
    public ResponseEntity<List<Professional>> listAllProfessionals() {return ResponseEntity.ok(professionalService.findAll());}
    /*@PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment created = appointmentService.createManualAppointment(appointment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdAppointment())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    */

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment created = appointmentFacade.createAppointment(appointment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdAppointment())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") long id) {
        boolean cancelled = appointmentService.cancelById(id);
        if (!cancelled) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
