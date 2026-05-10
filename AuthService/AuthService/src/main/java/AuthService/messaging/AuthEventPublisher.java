package AuthService.messaging;

import AuthService.config.RabbitMQConfig;
import AuthService.dto.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserCreated(UserCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AUTH_EXCHANGE,
                    RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                    event
            );
            log.info("Evento de usuario creado publicado en RabbitMQ: {}", event.getLogin());
        } catch (AmqpException exception) {
            log.error("No fue posible publicar el evento de usuario creado en RabbitMQ", exception);
        }
    }
}
