package co.unicauca.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpringDataHolidayRepository extends JpaRepository<HolidayEntity, LocalDate> {
    List<HolidayEntity> findByDateBetween(LocalDate start, LocalDate end);
}
