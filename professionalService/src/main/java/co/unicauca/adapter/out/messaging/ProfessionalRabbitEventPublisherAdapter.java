package co.unicauca.adapter.out.messaging;

import co.unicauca.config.RabbitMQConfig;
import co.unicauca.domain.model.Professional;
import co.unicauca.infra.event.ProfessionalCreatedEvent;
import co.unicauca.infra.event.ProfessionalUpdatedEvent;
import co.unicauca.port.out.ProfessionalEventPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfessionalRabbitEventPublisherAdapter implements ProfessionalEventPublisherPort {
    private final RabbitTemplate rabbitTemplate;

    public ProfessionalRabbitEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishProfessionalCreated(Professional professional) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROFESSIONAL_EXCHANGE,
                RabbitMQConfig.PROFESSIONAL_CREATED_KEY,
                new ProfessionalCreatedEvent(professional.getId().value(), professional.getName().value())
        );
    }

    @Override
    public void publishProfessionalUpdated(Professional professional) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROFESSIONAL_EXCHANGE,
                RabbitMQConfig.PROFESSIONAL_UPDATED_KEY,
                new ProfessionalUpdatedEvent(professional.getId().value(), professional.getName().value())
        );
    }
}
