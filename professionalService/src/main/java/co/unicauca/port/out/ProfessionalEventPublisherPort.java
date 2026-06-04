package co.unicauca.port.out;

import co.unicauca.domain.model.Professional;

public interface ProfessionalEventPublisherPort {
    void publishProfessionalCreated(Professional professional);
    void publishProfessionalUpdated(Professional professional);
}
