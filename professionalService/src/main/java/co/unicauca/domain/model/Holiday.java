package co.unicauca.domain.model;

import java.time.LocalDate;

public class Holiday {
    private final LocalDate date;
    private final String name;

    public Holiday(LocalDate date, String name) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha del festivo es obligatoria");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del festivo es obligatorio");
        }
        this.date = date;
        this.name = name;
    }

    public LocalDate getDate() { return date; }
    public String getName() { return name; }
}
