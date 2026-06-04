package co.unicauca.application.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingDayResponse(Long id, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Integer appointmentDurationMinutes) {}
