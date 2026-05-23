package co.unicauca.report.service;

import co.unicauca.report.entity.AppointmentReportRecord;
import co.unicauca.report.repository.AppointmentReportRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvAppointmentReportServiceTest {

    @Test
    void csvValueEscapesQuotesAndSeparators() {
        CsvAppointmentReportService service = new CsvAppointmentReportService(mock(AppointmentReportRepository.class));
        assertEquals("\"Paciente; con \"\"comillas\"\"\"", service.csvValue("Paciente; con \"comillas\""));
    }

    @Test
    void exportAppointmentsByProfessionalAndDateBuildsSortedSpreadsheetCsv() {
        AppointmentReportRepository repository = mock(AppointmentReportRepository.class);
        CsvAppointmentReportService service = new CsvAppointmentReportService(repository);
        LocalDate date = LocalDate.of(2026, 5, 20);

        AppointmentReportRecord earlier = appointment(
                1L,
                LocalDateTime.of(2026, 5, 20, 8, 30),
                "Paciente Uno",
                "Dra. Ana",
                "Consulta general"
        );
        AppointmentReportRecord later = appointment(
                2L,
                LocalDateTime.of(2026, 5, 20, 10, 0),
                "Paciente; Especial",
                "Dra. Ana",
                "Control \"prioritario\""
        );

        when(repository.findByProfessionalIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(
                7L,
                LocalDateTime.of(2026, 5, 20, 0, 0),
                LocalDateTime.of(2026, 5, 20, 23, 59, 59)
        )).thenReturn(List.of(earlier, later));

        String csv = service.exportAppointmentsByProfessionalAndDate(7L, null, date);

        assertEquals(
                "Hora;Paciente;Profesional;Observacion;Fecha agendamiento;Id cita\n" +
                        "08:30;Paciente Uno;Dra. Ana;Consulta general;2026-05-19 14:00;1\n" +
                        "10:00;\"Paciente; Especial\";Dra. Ana;\"Control \"\"prioritario\"\"\";2026-05-19 14:00;2\n",
                csv
        );
    }

    private AppointmentReportRecord appointment(Long id, LocalDateTime appointmentDate, String patientName, String professionalName, String observation) {
        AppointmentReportRecord appointment = new AppointmentReportRecord();
        appointment.setIdAppointment(id);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setSchedulingDate(LocalDateTime.of(2026, 5, 19, 14, 0));
        appointment.setPatientId(11L);
        appointment.setPatientName(patientName);
        appointment.setProfessionalId(7L);
        appointment.setProfessionalName(professionalName);
        appointment.setObservation(observation);
        return appointment;
    }
}
