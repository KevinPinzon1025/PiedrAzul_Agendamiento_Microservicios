package co.unicauca.port.out.notification;

import co.unicauca.domain.model.Professional;

public interface ProfessionalNotificationPort {

    void publishProfessionalCreated(Professional professional);

    void publishProfessionalUpdated(Professional professional);

    void publishProfessionalAvailabilityConfigured(Professional professional);
}
