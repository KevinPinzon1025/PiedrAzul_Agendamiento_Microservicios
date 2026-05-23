package co.unicauca.report.event;

import java.time.LocalDateTime;

public class AppointmentCreatedEvent {

    private Long idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmentDate;
    private String observation;
    private Long scheduler;
    private Long patient;
    private String patientName;
    private Long professional;
    private String professionalName;

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public LocalDateTime getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(LocalDateTime schedulingDate) {
        this.schedulingDate = schedulingDate;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Long getScheduler() {
        return scheduler;
    }

    public void setScheduler(Long scheduler) {
        this.scheduler = scheduler;
    }

    public Long getPatient() {
        return patient;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getProfessional() {
        return professional;
    }

    public void setProfessional(Long professional) {
        this.professional = professional;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }
}
