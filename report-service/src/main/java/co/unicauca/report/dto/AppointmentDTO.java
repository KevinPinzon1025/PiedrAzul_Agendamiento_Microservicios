package co.unicauca.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDTO {
    private Long idAppointment;
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmentDate;
    private String observation;
    private SchedulerDTO scheduler;
    private PatientDTO patient;
    private ProfessionalDTO professional;
}
