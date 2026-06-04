package co.unicauca.domain.model;

import co.unicauca.domain.exception.InvalidAvailabilityException;
import co.unicauca.domain.valueobject.AppointmentDuration;
import co.unicauca.domain.valueobject.TimeRange;

import java.time.DayOfWeek;

public class WorkingDay {
    private final DayOfWeek dayOfWeek;
    private final TimeRange timeRange;
    private final AppointmentDuration appointmentDuration;

    public WorkingDay(DayOfWeek dayOfWeek, TimeRange timeRange, AppointmentDuration appointmentDuration) {
        if (dayOfWeek == null) {
            throw new InvalidAvailabilityException("El día de atención es obligatorio");
        }
        this.dayOfWeek = dayOfWeek;
        this.timeRange = timeRange;
        this.appointmentDuration = appointmentDuration;
        validateDurationFitsInRange();
    }

    private void validateDurationFitsInRange() {
        long rangeMinutes = java.time.Duration.between(timeRange.startTime(), timeRange.endTime()).toMinutes();
        if (appointmentDuration.minutes() > rangeMinutes) {
            throw new InvalidAvailabilityException("La duración de la cita no cabe en la franja horaria");
        }
    }

    public boolean overlaps(WorkingDay other) {
        return dayOfWeek.equals(other.dayOfWeek) && timeRange.overlaps(other.timeRange);
    }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public TimeRange getTimeRange() { return timeRange; }
    public AppointmentDuration getAppointmentDuration() { return appointmentDuration; }
}
