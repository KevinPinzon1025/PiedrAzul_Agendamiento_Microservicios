package co.unicauca.notification.listener;

import co.unicauca.notification.infra.config.RabbitMQConfig;
import co.unicauca.notification.infra.dto.AppointmentCreatedEvent;
import co.unicauca.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor RabbitMQ para el evento AppointmentCreated.
 *
 * Patrón idéntico a UserConsumerService en BookingGym-Async:
 *   @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
 *   public void receiveMessage(User user) { ... }
 *
 * Aquí escuchamos la cola "appointment.created.queue" y delegamos
 * a NotificationService para enviar log al paciente y al profesional.
 */
@Component
public class AppointmentCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentCreatedListener.class);

    private final NotificationService notificationService;

    public AppointmentCreatedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void onAppointmentCreated(AppointmentCreatedEvent event) {
        logger.info("[LISTENER] Mensaje recibido de RabbitMQ → citaId={}", event.getIdAppointment());

        try {
            notificationService.notifyPatient(event);
            notificationService.notifyProfessional(event);
            logger.info("[LISTENER] Notificaciones procesadas OK para citaId={}", event.getIdAppointment());
        } catch (Exception e) {
            // En producción: DLQ (Dead Letter Queue) para reintentos automáticos.
            logger.error("[LISTENER] Error procesando notificación para citaId={}. Error: {}",
                    event.getIdAppointment(), e.getMessage());
            throw e; // re-lanzar para que RabbitMQ gestione el nack/requeue
        }
    }
}
