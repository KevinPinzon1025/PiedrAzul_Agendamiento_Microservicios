package co.unicauca.dto;

public class AppointmentNotificationRequest {
    private Long appointmentId;
    private Long patientId;
    private Long professionalId;
    private String appointmentDateTime;
    private String observation;

    public AppointmentNotificationRequest() {
    }

    public AppointmentNotificationRequest(Long appointmentId, Long patientId, Long professionalId, String appointmentDateTime, String observation) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.professionalId = professionalId;
        this.appointmentDateTime = appointmentDateTime;
        this.observation = observation;
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
