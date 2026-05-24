package co.unicauca.Repository;

import co.unicauca.Entity.model.ProfessionalWorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface IProfessionalWorkingDayRepository extends JpaRepository<ProfessionalWorkingDay, Long> {

    List<ProfessionalWorkingDay> findByProfessionalId(Long professionalId);

    void deleteByProfessionalId(Long professionalId);

    List<ProfessionalWorkingDay> findByProfessionalIdAndDayOfWeek(
            Long professionalId,
            DayOfWeek dayOfWeek
    );
}