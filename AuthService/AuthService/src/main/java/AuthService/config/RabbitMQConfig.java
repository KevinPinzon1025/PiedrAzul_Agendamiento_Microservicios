package AuthService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AUTH_EXCHANGE = "auth.exchange";
    public static final String USER_CREATED_QUEUE = "auth.user.created.queue";
    public static final String USER_CREATED_ROUTING_KEY = "auth.user.created";

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, DirectExchange authExchange) {
        return BindingBuilder
                .bind(userCreatedQueue)
                .to(authExchange)
                .with(USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
