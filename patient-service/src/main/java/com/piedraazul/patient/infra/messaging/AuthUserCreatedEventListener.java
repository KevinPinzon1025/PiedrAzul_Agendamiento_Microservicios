package com.piedraazul.patient.infra.messaging;

import com.piedraazul.patient.infra.config.RabbitMQConfig;
import com.piedraazul.patient.infra.event.AuthUserCreatedEvent;
import com.piedraazul.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthUserCreatedEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserCreatedEventListener.class);

    private final PatientService patientService;

    @RabbitListener(queues = RabbitMQConfig.PATIENT_AUTH_USER_CREATED_QUEUE)
    public void handle(Map<String, Object> payload) {
        AuthUserCreatedEvent event = mapPayload(payload);
        logger.info(
                "[CONSUMER] Evento auth.user.created recibido documentNumber={}, role={}",
                event.getDocumentNumber(),
                event.getRole()
        );
        patientService.createFromAuthUser(event);
    }

    private AuthUserCreatedEvent mapPayload(Map<String, Object> payload) {
        AuthUserCreatedEvent event = new AuthUserCreatedEvent();
        event.setEventType(stringValue(payload.get("eventType")));
        event.setId(longValue(payload.get("id")));
        event.setLogin(stringValue(payload.get("login")));
        event.setDocumentNumber(stringValue(payload.get("documentNumber")));
        event.setFirstName(stringValue(payload.get("firstName")));
        event.setSecondName(stringValue(payload.get("secondName")));
        event.setFirstLastName(stringValue(payload.get("firstLastName")));
        event.setSecondLastName(stringValue(payload.get("secondLastName")));
        event.setPhone(stringValue(payload.get("phone")));
        event.setGender(stringValue(payload.get("gender")));
        event.setBirthDate(localDateValue(payload.get("birthDate")));
        event.setEmail(stringValue(payload.get("email")));
        event.setRole(stringValue(payload.get("role")));
        event.setActive(booleanValue(payload.get("active")));
        return event;
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private Long longValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private boolean booleanValue(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(value.toString());
    }

    private LocalDate localDateValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String text) {
            return text.isBlank() ? null : LocalDate.parse(text);
        }
        if (value instanceof List<?> values && values.size() >= 3) {
            return LocalDate.of(
                    numberAt(values, 0),
                    numberAt(values, 1),
                    numberAt(values, 2)
            );
        }
        return null;
    }

    private int numberAt(List<?> values, int index) {
        Object value = values.get(index);
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
