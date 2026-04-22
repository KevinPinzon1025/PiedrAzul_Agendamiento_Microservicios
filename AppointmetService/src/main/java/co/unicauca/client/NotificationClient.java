package co.unicauca.client;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.dto.AppointmentNotificationRequest;
import co.unicauca.dto.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {
    private final RestTemplate restTemplate;
    private final String notificationBaseUrl;

    public NotificationClient(RestTemplate restTemplate,
                              @Value("${notification.service.url}") String notificationBaseUrl) {
        this.restTemplate = restTemplate;
        this.notificationBaseUrl = notificationBaseUrl;
    }

    public NotificationResponse sendAppointmentConfirmation(Appointment appointment) {
        AppointmentNotificationRequest request = new AppointmentNotificationRequest(
                appointment.getIdAppointment(),
                appointment.getPatient(),
                appointment.getProfessional(),
                appointment.getAppointmenDate().toString(),
                appointment.getObservation()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(
                notificationBaseUrl + "/api/notifications/appointment-confirmation",
                new HttpEntity<>(request, headers),
                NotificationResponse.class
        );

        return response.getBody();
    }
}
