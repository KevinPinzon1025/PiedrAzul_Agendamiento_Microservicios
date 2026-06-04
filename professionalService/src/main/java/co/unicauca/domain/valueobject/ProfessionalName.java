package co.unicauca.domain.valueobject;

public record ProfessionalName(String value) {
    public ProfessionalName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El nombre del profesional es obligatorio");
        }
        if (value.length() > 150) {
            throw new IllegalArgumentException("El nombre del profesional no puede superar 150 caracteres");
        }
    }
}
