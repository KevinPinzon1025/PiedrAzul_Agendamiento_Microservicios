package co.unicauca.notification.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

/**
 * Espejo del evento publicado por AppointmetService.
 * Se ubica en infra/dto/ igual que UserRequest en information-microservice
 * de BookingGym-Async.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) protege si AppointmetService
 * añade campos nuevos en el futuro — el consumidor no rompe.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentCreatedEvent {

    private long idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmenDate;  // typo original de la entidad, respetado
    private String observation;
    private long scheduler;
    private long patient;
    private long professional;

    public AppointmentCreatedEvent() {}

    public long getIdAppointment() { return idAppointment; }
    public void setIdAppointment(long v) { this.idAppointment = v; }

    public LocalDateTime getSchedulingDate() { return schedulingDate; }
    public void setSchedulingDate(LocalDateTime v) { this.schedulingDate = v; }

    public LocalDateTime getAppointmenDate() { return appointmenDate; }
    public void setAppointmenDate(LocalDateTime v) { this.appointmenDate = v; }

    public String getObservation() { return observation; }
    public void setObservation(String v) { this.observation = v; }

    public long getScheduler() { return scheduler; }
    public void setScheduler(long v) { this.scheduler = v; }

    public long getPatient() { return patient; }
    public void setPatient(long v) { this.patient = v; }

    public long getProfessional() { return professional; }
    public void setProfessional(long v) { this.professional = v; }
}
