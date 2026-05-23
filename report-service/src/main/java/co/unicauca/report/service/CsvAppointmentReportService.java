package co.unicauca.report.service;

import co.unicauca.report.entity.AppointmentReportRecord;
import co.unicauca.report.repository.AppointmentReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CsvAppointmentReportService {

    private static final String SEPARATOR = ";";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AppointmentReportRepository appointmentReportRepository;

    public CsvAppointmentReportService(AppointmentReportRepository appointmentReportRepository) {
        this.appointmentReportRepository = appointmentReportRepository;
    }

    public String exportAppointmentsByProfessionalAndDate(Long professionalId, String professional, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<AppointmentReportRecord> appointments = professionalId != null
                ? appointmentReportRepository.findByProfessionalIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(
                        professionalId,
                        start,
                        end
                )
                : appointmentReportRepository.findByProfessionalNameAndAppointmentDateBetweenOrderByAppointmentDateAsc(
                        professional,
                        start,
                        end
                );

        StringBuilder csv = new StringBuilder();
        csv.append("Hora;Paciente;Profesional;Observacion;Fecha agendamiento;Id cita\n");

        for (AppointmentReportRecord appointment : appointments) {
            csv.append(toCsvRow(appointment)).append("\n");
        }

        return csv.toString();
    }

    private String toCsvRow(AppointmentReportRecord appointment) {
        return String.join(SEPARATOR,
                csvValue(appointment.getAppointmentDate() == null
                        ? ""
                        : appointment.getAppointmentDate().toLocalTime().toString()),
                csvValue(appointment.getPatientName()),
                csvValue(appointment.getProfessionalName()),
                csvValue(appointment.getObservation()),
                csvValue(appointment.getSchedulingDate() == null ? "" : DATE_TIME_FORMAT.format(appointment.getSchedulingDate())),
                csvValue(appointment.getIdAppointment() == null ? "" : appointment.getIdAppointment().toString())
        );
    }

    String csvValue(String value) {
        String safe = value == null ? "" : value;
        boolean mustQuote = Stream.of(SEPARATOR, "\"", "\n", "\r").anyMatch(safe::contains);
        String escaped = safe.replace("\"", "\"\"");
        return mustQuote ? "\"" + escaped + "\"" : escaped;
    }
}
