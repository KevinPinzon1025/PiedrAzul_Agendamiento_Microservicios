package co.unicauca.notification.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración RabbitMQ del consumidor.
 * Patrón idéntico al de booking-microservice en BookingGym-Async:
 *   - misma cola durable
 *   - Jackson2JsonMessageConverter para deserializar el JSON al DTO
 *
 * La cola DEBE declararse aquí también: si NotificationService arranca
 * antes que AppointmetService, RabbitMQ la crea y los mensajes no se pierden.
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "appointment.created.queue";

    @Bean
    public Queue appointmentCreatedQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
