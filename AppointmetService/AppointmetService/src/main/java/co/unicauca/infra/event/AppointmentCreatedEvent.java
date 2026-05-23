package co.unicauca.infra.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AppointmentCreatedEvent implements Serializable {

    private long idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmentDate;
    private String observation;
    private Long scheduler;
    private long patient;
    private String patientName;
    private long professional;
    private String professionalName;

    public AppointmentCreatedEvent() {}

    public AppointmentCreatedEvent(long idAppointment,
                                   LocalDateTime schedulingDate,
                                   LocalDateTime appointmentDate,
                                   String observation,
                                   Long scheduler,
                                   long patient,
                                   String patientName,
                                   long professional,
                                   String professionalName) {
        this.idAppointment  = idAppointment;
        this.schedulingDate = schedulingDate;
        this.appointmentDate = appointmentDate;
        this.observation    = observation;
        this.scheduler      = scheduler;
        this.patient        = patient;
        this.patientName    = patientName;
        this.professional   = professional;
        this.professionalName = professionalName;
    }

    public long getIdAppointment() { return idAppointment; }
    public void setIdAppointment(long v) { this.idAppointment = v; }

    public LocalDateTime getSchedulingDate() { return schedulingDate; }
    public void setSchedulingDate(LocalDateTime v) { this.schedulingDate = v; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime v) { this.appointmentDate = v; }

    public String getObservation() { return observation; }
    public void setObservation(String v) { this.observation = v; }

    public Long getScheduler() { return scheduler; }
    public void setScheduler(Long v) { this.scheduler = v; }

    public long getPatient() { return patient; }
    public void setPatient(long v) { this.patient = v; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String v) { this.patientName = v; }

    public long getProfessional() { return professional; }
    public void setProfessional(long v) { this.professional = v; }

    public String getProfessionalName() { return professionalName; }
    public void setProfessionalName(String v) { this.professionalName = v; }

    @Override
    public String toString() {
        return "AppointmentCreatedEvent{" +
                "idAppointment=" + idAppointment +
                ", patient=" + patient +
                ", professional=" + professional +
                ", appointmenDate=" + appointmentDate +
                ", observation='" + observation + '\'' +
                '}';
    }
}
