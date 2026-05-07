
package co.unicauca.frontend.dto;


import java.time.LocalDateTime;

public class AppointmentDTO {
    private long idAppointment;
    private LocalDateTime appointmenDate;
    private LocalDateTime schedulingDate;
    private String observation;

    private PatientDTO patient;
    private ProfessionalDTO professional;
    private SchedulerDTO scheduler;

    public long getIdAppointment() {
        return idAppointment;
    }
    public void setIdAppointment(long idAppointment) {
        this.idAppointment = idAppointment;
    }
    public LocalDateTime getAppointmenDate() {
        return appointmenDate;
    }
    public void setAppointmenDate(LocalDateTime appointmenDate) {
        this.appointmenDate = appointmenDate;
    }
    public LocalDateTime getSchedulingDate() {
        return schedulingDate;
    }
    public void setSchedulingDate(LocalDateTime schedulingDate) {
        this.schedulingDate = schedulingDate;
    }
    public String getObservation() {
        return observation;
    }
    public void setObservation(String observation) {
        this.observation = observation;
    }
    public PatientDTO getPatient() {
        return patient;
    }
    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }
    public ProfessionalDTO getProfessional() {
        return professional;
    }
    public void setProfessional(ProfessionalDTO professional) {
        this.professional = professional;
    }
    public SchedulerDTO getScheduler() {
        return scheduler;
    }
    public void setScheduler(SchedulerDTO scheduler) {
        this.scheduler = scheduler;
    }

}