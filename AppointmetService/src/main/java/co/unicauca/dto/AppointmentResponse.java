package co.unicauca.dto;

import java.time.LocalDateTime;

public class AppointmentResponse {
    private Integer idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmenDate;
    private String observation;
    private Integer scheduler;
    private Integer patient;
    private Integer professional;
    private String status;

    public AppointmentResponse() {
    }

    public Integer getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Integer idAppointment) {
        this.idAppointment = idAppointment;
    }

    public LocalDateTime getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(LocalDateTime schedulingDate) {
        this.schedulingDate = schedulingDate;
    }

    public LocalDateTime getAppointmenDate() {
        return appointmenDate;
    }

    public void setAppointmenDate(LocalDateTime appointmenDate) {
        this.appointmenDate = appointmenDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getScheduler() {
        return scheduler;
    }

    public void setScheduler(Integer scheduler) {
        this.scheduler = scheduler;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }

    public Integer getProfessional() {
        return professional;
    }

    public void setProfessional(Integer professional) {
        this.professional = professional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}