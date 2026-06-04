package co.unicauca.adapter.out.persistence;

import co.unicauca.domain.model.Holiday;
import co.unicauca.port.out.HolidayRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class HolidayJpaAdapter implements HolidayRepositoryPort {
    private final SpringDataHolidayRepository repository;

    public HolidayJpaAdapter(SpringDataHolidayRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Holiday> findAll() {
        return repository.findAll().stream()
                .map(entity -> new Holiday(entity.getDate(), entity.getName()))
                .toList();
    }

    @Override
    public Set<LocalDate> findDatesBetween(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end).stream()
                .map(HolidayEntity::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public Holiday save(Holiday holiday) {
        HolidayEntity entity = new HolidayEntity();
        entity.setDate(holiday.getDate());
        entity.setName(holiday.getName());
        HolidayEntity saved = repository.save(entity);
        return new Holiday(saved.getDate(), saved.getName());
    }

    @Override
    public void deleteByDate(LocalDate date) {
        repository.deleteById(date);
    }
}
