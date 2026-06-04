package co.unicauca.infra.config;
/*
* TODO en el microservicio profesional crear la infraestructura para mandar mensajes de profesional creado
*  el EXCHANGE a usar es "piedrAzul.professional.exchange"
*
* TODO en el microservicio PACIENTE crear la infraestructura para mandar mensajes de PACIENTE creado
 *  el EXCHANGE a usar es "piedrAzul.patient.exchange"
 *
 * TODO en el microservicio USER crear la infraestructura para mandar mensajes de SCHEDULER creado
 *  el EXCHANGE a usar es "piedrAzul.scheduler.exchange"
*
*  TODO revisar que funcione la comunicacion asincrona entre los microservicios antes mencionados
* */
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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

    public static final String EXCHANGE = "piedrAzul.appointments.exchange";
    public static final String QUEUE = "appointment.created.queue";
    public static final String ROUTING_KEY = "appointment.created";

    public static final String PROFESSIONAL_CREATED_QUEUE = "professional.created.queue";
    public static final String PROFESSIONAL_UPDATED_QUEUE = "professional.updated.queue";

    //esto si se hace aqui o en el que publica?
    public static final String PROFESSIONAL_CREATED_KEY = "professional.created";
    public static final String PROFESSIONAL_UPDATED_KEY = "professional.updated";
    public static final String PROFESSIONAL_EXCHANGE = "piedrAzul.professional.exchange";

    public static final String PATIENT_CREATED_QUEUE = "patient.created.queue";

    //esto si se hace aqui o en el que publica?
    public static final String PATIENT_CREATED_KEY = "patient.created";
    public static final String PATIENT_EXCHANGE = "piedrAzul.patient.exchange";

    public static final String SCHEDULER_CREATED_QUEUE = "scheduler.created.queue";

    //esto si se hace aqui o en el que publica?
    public static final String SCHEDULER_CREATED_KEY = "scheduler.created";
    public static final String SCHEDULER_EXCHANGE = "piedrAzul.scheduler.exchange";

    //creacion de exchanges
    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE);
    }

        //esto si se hace aqui o en el que publica?
    @Bean
    public TopicExchange professionalExchange() {
        return new TopicExchange(PROFESSIONAL_EXCHANGE);
    }

        //esto si se hace aqui o en el que publica?
    @Bean
    public TopicExchange patientExchange() {
        return new TopicExchange(PATIENT_EXCHANGE);
    }

        //esto si se hace aqui o en el que publica?
    @Bean
    public TopicExchange schedulerExchange() {
        return new TopicExchange(SCHEDULER_EXCHANGE);
    }

    //creacion de colas
    @Bean
    public Queue appointmentQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue professionalCreatedQueue() {
        return new Queue(PROFESSIONAL_CREATED_QUEUE, true);
    }

    @Bean
    public Queue professionalUpdatedQueue() {
        return new Queue(PROFESSIONAL_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue patientCreatedQueue() {
        return new Queue(PATIENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue schedulerCreatedQueue() {
        return new Queue(SCHEDULER_CREATED_QUEUE, true);
    }

    //creacion de bindings
        //esto si se hace aqui o en el que publica?
    @Bean
    public Binding schedulerCreatedBinding(Queue schedulerCreatedQueue,
                                         TopicExchange schedulerExchange) {
        return BindingBuilder.bind(schedulerCreatedQueue)
                .to(schedulerExchange)
                .with(SCHEDULER_CREATED_KEY);
    }
        //esto si se hace aqui o en el que publica?
    @Bean
    public Binding patientCreatedBinding(Queue patientCreatedQueue,
                                              TopicExchange patientExchange) {
        return BindingBuilder.bind(patientCreatedQueue)
                .to(patientExchange)
                .with(PATIENT_CREATED_KEY);
    }
        //esto si se hace aqui o en el que publica?
    @Bean
    public Binding professionalCreatedBinding(Queue professionalCreatedQueue,
                                              TopicExchange professionalExchange) {
        return BindingBuilder.bind(professionalCreatedQueue)
                .to(professionalExchange)
                .with(PROFESSIONAL_CREATED_KEY);
    }

    @Bean
    public Binding professionalUpdatedBinding(Queue professionalUpdatedQueue,
                                              TopicExchange professionalExchange) {
        return BindingBuilder.bind(professionalUpdatedQueue)
                .to(professionalExchange)
                .with(PROFESSIONAL_UPDATED_KEY);
    }

    @Bean
    public Binding appointmentCreatedBinding(Queue appointmentQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(appointmentQueue).to(appointmentExchange).with(ROUTING_KEY);
    }

    //Enviar mensajes
    @Bean
    public MessageConverter jsonMessageConverter() {
        JsonMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        return new JacksonJsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}