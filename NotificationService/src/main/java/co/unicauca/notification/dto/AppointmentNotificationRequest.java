package co.unicauca.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppointmentNotificationRequest {
    @NotNull
    private Long appointmentId;
    @NotNull
    private Long patientId;
    @NotNull
    private Long professionalId;
    @NotBlank
    private String appointmentDateTime;
    @NotBlank
    private String observation;

    public AppointmentNotificationRequest() {
    }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }
    public String getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(String appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
