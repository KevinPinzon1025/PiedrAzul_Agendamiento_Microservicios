package co.unicauca.domain.valueobject;

public record AppointmentDuration(Integer minutes) {
    public AppointmentDuration {
        if (minutes == null || minutes <= 0) {
            throw new IllegalArgumentException("La duración de la cita debe ser mayor que cero");
        }
        if (minutes > 480) {
            throw new IllegalArgumentException("La duración de la cita no puede superar 480 minutos");
        }
    }
}
