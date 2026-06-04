package co.unicauca.domain.valueobject;

public enum ProfessionalType {
    MEDICO,
    TERAPISTA;

    public static ProfessionalType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El tipo de profesional es obligatorio");
        }
        String normalized = value.trim().toUpperCase()
                .replace("É", "E")
                .replace("Í", "I");
        return ProfessionalType.valueOf(normalized);
    }
}
