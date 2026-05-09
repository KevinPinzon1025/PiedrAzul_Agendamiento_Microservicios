package co.unicauca.frontend.dto;

import java.time.LocalDateTime;

public class CreateAppointmentRequestDTO {

    private Long patientId;
    private Long professionalId;
    private Long schedulerId;
    private String observation;
    private LocalDateTime appointmentDate;
    private SchedulingType schedulingType;

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public SchedulingType getSchedulingType() {
        return schedulingType;
    }
    public void setSchedulingType(SchedulingType schedulingType) {
        this.schedulingType = schedulingType;
    }

}