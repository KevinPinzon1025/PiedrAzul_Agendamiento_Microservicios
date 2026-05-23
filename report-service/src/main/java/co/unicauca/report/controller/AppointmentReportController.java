package co.unicauca.report.controller;

import co.unicauca.report.service.CsvAppointmentReportService;
import co.unicauca.report.service.ReportFileStorageService;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reports/appointments")
@Validated
public class AppointmentReportController {

    private static final DateTimeFormatter FILE_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final CsvAppointmentReportService csvAppointmentReportService;
    private final ReportFileStorageService reportFileStorageService;

    public AppointmentReportController(CsvAppointmentReportService csvAppointmentReportService,
                                       ReportFileStorageService reportFileStorageService) {
        this.csvAppointmentReportService = csvAppointmentReportService;
        this.reportFileStorageService = reportFileStorageService;
    }

    @GetMapping(value = "/csv", produces = "text/csv;charset=UTF-8")
    public ResponseEntity<String> exportCsv(
            @RequestParam(required = false) Long professionalId,
            @RequestParam(required = false) String professional,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        validateProfessionalFilter(professionalId, professional);

        String csv = csvAppointmentReportService.exportAppointmentsByProfessionalAndDate(professionalId, professional, date);
        String filename = buildCsvFileName(professionalId, professional, date);
        Path savedReportPath = reportFileStorageService.saveCsv(filename, csv);

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header("X-Report-File", savedReportPath.toString())
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv);
    }

    @GetMapping(value = "/csv/preview", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> previewCsv(
            @RequestParam(required = false) Long professionalId,
            @RequestParam(required = false) String professional,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        validateProfessionalFilter(professionalId, professional);

        String csv = csvAppointmentReportService.exportAppointmentsByProfessionalAndDate(professionalId, professional, date);
        String filename = buildCsvFileName(professionalId, professional, date);
        Path savedReportPath = reportFileStorageService.saveCsv(filename, csv);
        return ResponseEntity.ok()
                .header("X-Report-File", savedReportPath.toString())
                .body(csv);
    }

    private void validateProfessionalFilter(Long professionalId, String professional) {
        if (professionalId == null && (professional == null || professional.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar professionalId o professional");
        }
    }

    private String fileProfessionalName(Long professionalId, String professional) {
        return professionalId == null ? professional : "profesional_" + professionalId;
    }

    private String sanitizeFileName(String value) {
        return value == null ? "profesional" : value.trim().replaceAll("[^a-zA-Z0-9-_]", "_");
    }

    private String buildCsvFileName(Long professionalId, String professional, LocalDate date) {
        String professionalName = sanitizeFileName(fileProfessionalName(professionalId, professional));
        String timestamp = FILE_TIMESTAMP_FORMAT.format(LocalDateTime.now());
        return "citas_" + professionalName + "_" + date + "_" + timestamp + ".csv";
    }
}
