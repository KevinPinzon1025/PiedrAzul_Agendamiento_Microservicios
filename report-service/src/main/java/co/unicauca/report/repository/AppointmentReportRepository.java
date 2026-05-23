package co.unicauca.report.repository;

import co.unicauca.report.entity.AppointmentReportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentReportRepository extends JpaRepository<AppointmentReportRecord, Long> {

    List<AppointmentReportRecord> findByProfessionalIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(
            Long professionalId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<AppointmentReportRecord> findByProfessionalNameAndAppointmentDateBetweenOrderByAppointmentDateAsc(
            String professionalName,
            LocalDateTime start,
            LocalDateTime end
    );
}
