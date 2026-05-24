package co.unicauca.Controller;

import co.unicauca.Entity.facade.AppointmentFacade;
import co.unicauca.Entity.model.Appointment;
import co.unicauca.Service.AppointmentService;
import co.unicauca.infra.dto.CreateAppointmentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentFacade appointmentFacade;

    public AppointmentController(AppointmentFacade appointmentFacade, AppointmentService appointmentService) {
        this.appointmentFacade = appointmentFacade;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> listAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Appointment>> findByProfessionalAndDate(
            @RequestParam(required = false) Long professionalId,
            @RequestParam(required = false) String professional,
            @RequestParam LocalDate date
    ) {
        if (professionalId == null && (professional == null || professional.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "professionalId o professional es requerido");
        }

        return ResponseEntity.ok(
                appointmentService.findByProfessionalAndDate(
                        professionalId,
                        professional,
                        date
                )
        );
    }

  /*  @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment created = appointmentFacade.createAppointment(appointment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdAppointment())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
*/

    @PostMapping
    public ResponseEntity<Appointment> create(
            @RequestBody CreateAppointmentRequestDTO request
    ) {

        Appointment created = appointmentFacade.createAppointment(request);

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
