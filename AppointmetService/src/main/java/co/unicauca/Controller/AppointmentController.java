package co.unicauca.Controller;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    //Lista todas las citas registradas.
    @GetMapping
    public ResponseEntity<List<Appointment>> listAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    //Crea una nueva cita médica.
    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        Appointment created = appointmentService.createManualAppointment(appointment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getIdAppointment())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    //Cancela una cita existente.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") long id) {
        boolean cancelled = appointmentService.cancelById(id);
        if (!cancelled) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
