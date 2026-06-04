package co.unicauca.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotResponse(LocalDate date, LocalTime startTime, LocalTime endTime, boolean available) {}
