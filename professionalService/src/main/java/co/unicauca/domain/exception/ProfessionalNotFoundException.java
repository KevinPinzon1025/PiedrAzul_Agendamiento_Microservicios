package co.unicauca.domain.exception;

public class ProfessionalNotFoundException extends DomainException {
    public ProfessionalNotFoundException(Long id) {
        super("Profesional no encontrado con id " + id);
    }
}
