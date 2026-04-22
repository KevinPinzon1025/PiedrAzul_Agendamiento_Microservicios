package co.unicauca.infra.messaging;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.event.AppointmentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos hacia RabbitMQ.
 * Patrón idéntico a UserService.createUser() en information-microservice
 * de BookingGym-Async, que llama:
 *   rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE, userSaved);
 *
 * Aquí lo encapsulamos en un componente dedicado para respetar
 * el principio de responsabilidad única y no mezclar lógica de mensajería
 * dentro del Facade.
 */
@Component
public class AppointmentEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public AppointmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica el evento de cita creada en RabbitMQ.
     * Se invoca SIEMPRE después de que appointmentRepository.save() fue exitoso.
     *
     * @param event datos de la cita recién persistida
     */
    public void publish(AppointmentCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    event
            );
            logger.info("[PUBLISHER] Evento publicado → exchange={}, routingKey={}, citaId={}",
                    RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event.getIdAppointment());
        } catch (Exception e) {
           logger.error("[PUBLISHER] Error publicando evento para citaId={}. " +
                    "La cita fue guardada pero la notificación no se entregó. Error: {}",
                    event.getIdAppointment(), e.getMessage());
        }
    }
}
