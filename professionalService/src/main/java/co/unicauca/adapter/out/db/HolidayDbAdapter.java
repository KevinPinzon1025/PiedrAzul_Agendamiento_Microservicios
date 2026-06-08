package co.unicauca.adapter.out.db;

import co.unicauca.adapter.out.db.model.HolidayEntity;
import co.unicauca.adapter.out.db.repository.SpringDataHolidayRepository;
import co.unicauca.domain.model.Holiday;
import co.unicauca.port.out.db.HolidayDbPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class HolidayDbAdapter implements HolidayDbPort {

    private final SpringDataHolidayRepository repository;

    public HolidayDbAdapter(SpringDataHolidayRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Holiday> findAll() {
        return repository.findAll()
                .stream()
                .map(entity ->
                        new Holiday(entity.getDate(), entity.getName())
                )
                .toList();
    }

    @Override
    public List<LocalDate> findDatesBetween(
            LocalDate start,
            LocalDate end
    ) {
        return repository.findByDateBetween(start, end)
                .stream()
                .map(HolidayEntity::getDate)
                .toList();
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
