package co.unicauca.port.out.db;

import co.unicauca.domain.model.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayDbPort {

    List<Holiday> findAll();

    List<LocalDate> findDatesBetween(LocalDate start, LocalDate end);

    Holiday save(Holiday holiday);

    void deleteByDate(LocalDate date);
}
