package com.piedraazul.patient.infra.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String PATIENT_EXCHANGE = "piedrAzul.patient.exchange";
    public static final String PATIENT_CREATED_ROUTING_KEY = "patient.created";

    @Bean
    public TopicExchange patientExchange() {
        return new TopicExchange(PATIENT_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        JsonMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        return new JacksonJsonMessageConverter(mapper);
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
