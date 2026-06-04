package co.unicauca.application.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingDayRequest(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Integer appointmentDurationMinutes) {}
