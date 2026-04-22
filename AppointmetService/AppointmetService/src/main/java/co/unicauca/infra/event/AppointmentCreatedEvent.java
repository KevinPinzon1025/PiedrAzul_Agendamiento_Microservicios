package co.unicauca.infra.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AppointmentCreatedEvent implements Serializable {

    private long idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmenDate;
    private String observation;
    private long scheduler;
    private long patient;
    private long professional;

    public AppointmentCreatedEvent() {}

    public AppointmentCreatedEvent(long idAppointment,
                                   LocalDateTime schedulingDate,
                                   LocalDateTime appointmenDate,
                                   String observation,
                                   long scheduler,
                                   long patient,
                                   long professional) {
        this.idAppointment  = idAppointment;
        this.schedulingDate = schedulingDate;
        this.appointmenDate = appointmenDate;
        this.observation    = observation;
        this.scheduler      = scheduler;
        this.patient        = patient;
        this.professional   = professional;
    }

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

    @Override
    public String toString() {
        return "AppointmentCreatedEvent{" +
                "idAppointment=" + idAppointment +
                ", patient=" + patient +
                ", professional=" + professional +
                ", appointmenDate=" + appointmenDate +
                ", observation='" + observation + '\'' +
                '}';
    }
}
