package co.unicauca.domain.valueobject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AvailableSlot(LocalDate date, LocalTime startTime, LocalTime endTime, boolean available) {
    public LocalDateTime startDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    public LocalDateTime endDateTime() {
        return LocalDateTime.of(date, endTime);
    }
}
