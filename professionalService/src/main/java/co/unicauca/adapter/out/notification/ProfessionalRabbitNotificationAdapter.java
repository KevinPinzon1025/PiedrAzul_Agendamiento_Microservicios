package co.unicauca.adapter.out.notification;

import co.unicauca.config.RabbitMQConfig;
import co.unicauca.domain.model.Professional;
import co.unicauca.infra.event.ProfessionalCreatedEvent;
import co.unicauca.infra.event.ProfessionalUpdatedEvent;
import co.unicauca.port.out.notification.ProfessionalNotificationPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalRabbitNotificationAdapter
        implements ProfessionalNotificationPort {

    private final RabbitTemplate rabbitTemplate;

    public ProfessionalRabbitNotificationAdapter(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishProfessionalCreated(Professional professional) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROFESSIONAL_EXCHANGE,
                RabbitMQConfig.PROFESSIONAL_CREATED_KEY,
                new ProfessionalCreatedEvent(
                        professional.getId().value(),
                        professional.getName().value()
                )
        );
    }

    @Override
    public void publishProfessionalUpdated(Professional professional) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROFESSIONAL_EXCHANGE,
                RabbitMQConfig.PROFESSIONAL_UPDATED_KEY,
                new ProfessionalUpdatedEvent(
                        professional.getId().value(),
                        professional.getName().value()
                )
        );
    }

    @Override
    public void publishProfessionalAvailabilityConfigured(
            Professional professional
    ) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROFESSIONAL_EXCHANGE,
                RabbitMQConfig.PROFESSIONAL_AVAILABILITY_CONFIGURED_KEY,
                new ProfessionalUpdatedEvent(
                        professional.getId().value(),
                        professional.getName().value()
                )
        );
    }
}
