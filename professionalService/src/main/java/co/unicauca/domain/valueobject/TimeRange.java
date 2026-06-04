package co.unicauca.domain.valueobject;

import java.time.LocalTime;

public record TimeRange(LocalTime startTime, LocalTime endTime) {
    public TimeRange {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("La hora de inicio y fin son obligatorias");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
        }
    }

    public boolean overlaps(TimeRange other) {
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }
}
