package co.unicauca.notification.service;

import co.unicauca.notification.dto.AppointmentNotificationRequest;
import co.unicauca.notification.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationResponse sendAppointmentConfirmation(AppointmentNotificationRequest request) {
        String body = String.format(
                "Cita #%d agendada para %s con profesional %d. Observacion: %s",
                request.getAppointmentId(),
                request.getAppointmentDateTime(),
                request.getProfessionalId(),
                request.getObservation()
        );

        logger.info("de: sistema, para: paciente-{}, asunto: confirmacion de cita, body: {}",
                request.getPatientId(), body);

        return new NotificationResponse(true, "Notificacion enviada correctamente");
    }
}
