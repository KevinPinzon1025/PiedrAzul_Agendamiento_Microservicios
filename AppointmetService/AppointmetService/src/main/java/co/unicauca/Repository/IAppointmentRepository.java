package co.unicauca.Repository;

import co.unicauca.Entity.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment,Long> {


    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.professional.profName = :professional
        AND a.appointmentDate BETWEEN :start AND :end
    """)
    List<Appointment> findByProfessionalAndDate(
            @Param("professional") String professional,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.professional.id = :professionalId
        AND FUNCTION('DATE', a.appointmentDate) = :date
    """)
    List<Appointment> findByProfessionalIdAndDate(
            @Param("professionalId") Long professionalId,
            @Param("date") LocalDate date
    );

}
