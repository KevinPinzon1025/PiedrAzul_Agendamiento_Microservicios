package co.unicauca.application.dto;

import java.time.LocalDate;

public record HolidayRequest(LocalDate date, String name) {}
