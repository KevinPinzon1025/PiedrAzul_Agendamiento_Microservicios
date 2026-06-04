package co.unicauca.domain.valueobject;

public record ProfessionalEmail(String value) {
    public ProfessionalEmail {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El correo del profesional es obligatorio");
        }
        if (!value.contains("@")) {
            throw new IllegalArgumentException("El correo del profesional no tiene un formato válido");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("El correo del profesional no puede superar 200 caracteres");
        }
    }
}
