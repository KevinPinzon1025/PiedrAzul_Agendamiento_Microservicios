package co.unicauca.port.out;

import co.unicauca.domain.model.Holiday;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface HolidayRepositoryPort {
    List<Holiday> findAll();
    Set<LocalDate> findDatesBetween(LocalDate start, LocalDate end);
    Holiday save(Holiday holiday);
    void deleteByDate(LocalDate date);
}
