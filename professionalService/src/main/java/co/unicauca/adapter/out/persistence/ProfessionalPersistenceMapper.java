package co.unicauca.adapter.out.persistence;

import co.unicauca.domain.model.Professional;
import co.unicauca.domain.model.WorkingDay;
import co.unicauca.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProfessionalPersistenceMapper {
    public Professional toDomain(ProfessionalEntity entity) {
        Professional professional = new Professional(
                new ProfessionalId(entity.getId()),
                new ProfessionalName(entity.getName()),
                new ProfessionalEmail(entity.getEmail()),
                ProfessionalType.from(entity.getType().getName())
        );
        var workingDays = entity.getWorkingDays().stream()
                .map(day -> new WorkingDay(
                        day.getDayOfWeek(),
                        new TimeRange(day.getStartTime(), day.getEndTime()),
                        new AppointmentDuration(day.getAppointmentDurationMinutes())
                ))
                .collect(Collectors.toList());
        if (!workingDays.isEmpty()) {
            professional.configureAvailability(workingDays);
        }
        return professional;
    }

    public Professional toDomainWithoutAvailability(ProfessionalEntity entity) {
        return new Professional(
                new ProfessionalId(entity.getId()),
                new ProfessionalName(entity.getName()),
                new ProfessionalEmail(entity.getEmail()),
                ProfessionalType.from(entity.getType().getName())
        );
    }

    public ProfessionalEntity toEntity(Professional professional, ProfessionalTypeEntity typeEntity) {
        ProfessionalEntity entity = new ProfessionalEntity();
        if (professional.getId() != null) {
            entity.setId(professional.getId().value());
        }
        entity.setName(professional.getName().value());
        entity.setEmail(professional.getEmail().value());
        entity.setType(typeEntity);
        professional.getWorkingDays().forEach(day -> {
            WorkingDayEntity workingDayEntity = new WorkingDayEntity();
            workingDayEntity.setProfessional(entity);
            workingDayEntity.setDayOfWeek(day.getDayOfWeek());
            workingDayEntity.setStartTime(day.getTimeRange().startTime());
            workingDayEntity.setEndTime(day.getTimeRange().endTime());
            workingDayEntity.setAppointmentDurationMinutes(day.getAppointmentDuration().minutes());
            entity.getWorkingDays().add(workingDayEntity);
        });
        return entity;
    }
}
