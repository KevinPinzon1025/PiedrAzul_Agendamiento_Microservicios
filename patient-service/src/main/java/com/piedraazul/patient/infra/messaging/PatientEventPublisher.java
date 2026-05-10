package com.piedraazul.patient.infra.messaging;

import com.piedraazul.patient.infra.config.RabbitMQConfig;
import com.piedraazul.patient.infra.event.PatientCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(PatientEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public void publish(PatientCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PATIENT_EXCHANGE,
                    RabbitMQConfig.PATIENT_CREATED_ROUTING_KEY,
                    event
            );
            logger.info(
                    "[PUBLISHER] Evento patient.created publicado exchange={}, routingKey={}, patientId={}",
                    RabbitMQConfig.PATIENT_EXCHANGE,
                    RabbitMQConfig.PATIENT_CREATED_ROUTING_KEY,
                    event.getId()
            );
        } catch (Exception exception) {
            logger.error(
                    "[PUBLISHER] Error publicando patient.created para patientId={}. El paciente fue guardado, pero el evento no se entrego. Error: {}",
                    event.getId(),
                    exception.getMessage()
            );
        }
    }
}
