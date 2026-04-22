package co.unicauca.notification.service;

import co.unicauca.notification.infra.dto.AppointmentCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Servicio de notificaciones.
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Simula el envío de notificación de confirmación al PACIENTE.
     */
    public void notifyPatient(AppointmentCreatedEvent event) {
        logger.info(
            "\n========================================================" +
            "\n  NOTIFICACIÓN → PACIENTE" +
            "\n  de     : sistema@piedrAzul.co" +
            "\n  para   : paciente id={}" +
            "\n  asunto : Confirmación de cita médica #{}" +
            "\n  body   : Estimado/a paciente, su cita ha sido confirmada." +
            "\n           Fecha y hora  : {}" +
            "\n           Profesional   : id={}" +
            "\n           Observación   : {}" +
            "\n========================================================",
            event.getPatient(),
            event.getIdAppointment(),
            event.getAppointmenDate(),
            event.getProfessional(),
            event.getObservation() != null ? event.getObservation() : "N/A"
        );
    }

    /**
     * Simula el envío de notificación de nueva cita al PROFESIONAL DE SALUD.
     */
    public void notifyProfessional(AppointmentCreatedEvent event) {
        logger.info(
            "\n========================================================" +
            "\n  NOTIFICACIÓN → PROFESIONAL DE SALUD" +
            "\n  de     : sistema@piedrAzul.co" +
            "\n  para   : profesional id={}" +
            "\n  asunto : Nueva cita agendada #{}" +
            "\n  body   : Dr/Dra., tiene una nueva cita médica agendada." +
            "\n           Fecha y hora  : {}" +
            "\n           Paciente      : id={}" +
            "\n           Observación   : {}" +
            "\n========================================================",
            event.getProfessional(),
            event.getIdAppointment(),
            event.getAppointmenDate(),
            event.getPatient(),
            event.getObservation() != null ? event.getObservation() : "N/A"
        );
    }
}
