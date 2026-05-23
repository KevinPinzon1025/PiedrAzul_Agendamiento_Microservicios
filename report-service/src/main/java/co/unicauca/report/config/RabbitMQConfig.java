package co.unicauca.report.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${piedrAzul.rabbitmq.appointments.exchange}")
    private String appointmentExchangeName;

    @Value("${piedrAzul.rabbitmq.appointments.created-routing-key}")
    private String appointmentCreatedRoutingKey;

    @Value("${piedrAzul.rabbitmq.appointments.report-created-queue}")
    private String reportAppointmentCreatedQueueName;

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(appointmentExchangeName);
    }

    @Bean
    public Queue reportAppointmentCreatedQueue() {
        return new Queue(reportAppointmentCreatedQueueName, true);
    }

    @Bean
    public Binding reportAppointmentCreatedBinding(Queue reportAppointmentCreatedQueue,
                                                   TopicExchange appointmentExchange) {
        return BindingBuilder.bind(reportAppointmentCreatedQueue)
                .to(appointmentExchange)
                .with(appointmentCreatedRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
