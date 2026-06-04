package co.unicauca.domain.valueobject;

public record SchedulingWindow(Integer weeks) {
    public SchedulingWindow {
        if (weeks == null || weeks <= 0) {
            throw new IllegalArgumentException("La ventana de agendamiento debe ser mayor que cero semanas");
        }
        if (weeks > 52) {
            throw new IllegalArgumentException("La ventana de agendamiento no puede superar 52 semanas");
        }
    }
}
