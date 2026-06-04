package co.unicauca.domain.valueobject;

public record ProfessionalId(Long value) {
    public ProfessionalId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El id del profesional es obligatorio y debe ser positivo");
        }
    }
}
