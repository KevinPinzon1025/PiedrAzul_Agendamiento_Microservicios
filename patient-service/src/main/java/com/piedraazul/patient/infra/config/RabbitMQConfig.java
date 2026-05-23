package com.piedraazul.patient.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PATIENT_EXCHANGE = "piedrAzul.patient.exchange";
    public static final String PATIENT_CREATED_ROUTING_KEY = "patient.created";
    public static final String AUTH_EXCHANGE = "auth.exchange";
    public static final String AUTH_USER_CREATED_ROUTING_KEY = "auth.user.created";
    public static final String PATIENT_AUTH_USER_CREATED_QUEUE = "patient.auth.user.created.queue";

    @Bean
    public TopicExchange patientExchange() {
        return new TopicExchange(PATIENT_EXCHANGE);
    }

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    @Bean
    public Queue patientAuthUserCreatedQueue() {
        return new Queue(PATIENT_AUTH_USER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding patientAuthUserCreatedBinding(Queue patientAuthUserCreatedQueue,
                                                 DirectExchange authExchange) {
        return BindingBuilder
                .bind(patientAuthUserCreatedQueue)
                .to(authExchange)
                .with(AUTH_USER_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
