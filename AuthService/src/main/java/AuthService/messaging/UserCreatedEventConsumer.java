package AuthService.messaging;

import AuthService.config.RabbitMQConfig;
import AuthService.dto.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCreatedEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void receiveUserCreatedEvent(UserCreatedEvent event) {
        log.info("Evento recibido desde RabbitMQ. Usuario creado: id={}, login={}, rol={}",
                event.getId(), event.getLogin(), event.getRole());
    }
}
