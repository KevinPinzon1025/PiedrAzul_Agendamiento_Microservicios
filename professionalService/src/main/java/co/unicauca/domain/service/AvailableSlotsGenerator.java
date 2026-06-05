package co.unicauca.domain.service;

import co.unicauca.domain.model.Professional;
import co.unicauca.domain.model.WorkingDay;
import co.unicauca.domain.valueobject.AvailableSlot;
import co.unicauca.domain.valueobject.SchedulingWindow;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AvailableSlotsGenerator {
    public List<AvailableSlot> generateForDate(Professional professional,
                                               LocalDate date,
                                               LocalDate today,
                                               SchedulingWindow window,
                                               Set<LocalDate> holidays) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (date.isBefore(today)) {
            return List.of();
        }
        if (date.isAfter(today.plusWeeks(window.weeks()))) {
            return List.of();
        }
        if (holidays != null && holidays.contains(date)) {
            return List.of();
        }

        List<AvailableSlot> slots = new ArrayList<>();
        for (WorkingDay workingDay : professional.getWorkingDays()) {
            if (!workingDay.getDayOfWeek().equals(date.getDayOfWeek())) {
                continue;
            }
            LocalTime current = workingDay.getTimeRange().startTime();
            LocalTime end = workingDay.getTimeRange().endTime();
            int duration = workingDay.getAppointmentDuration().minutes();
            while (!current.plusMinutes(duration).isAfter(end)) {
                LocalTime slotEnd = current.plusMinutes(duration);
                slots.add(new AvailableSlot(date, current, slotEnd, true));
                current = slotEnd;
            }
        }
        return slots;
    }
}
