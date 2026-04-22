package co.unicauca.notification.controller;

import co.unicauca.notification.dto.AppointmentNotificationRequest;
import co.unicauca.notification.dto.NotificationResponse;
import co.unicauca.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/appointment-confirmation")
    public ResponseEntity<NotificationResponse> sendAppointmentConfirmation(
            @Valid @RequestBody AppointmentNotificationRequest request) {
        return ResponseEntity.ok(notificationService.sendAppointmentConfirmation(request));
    }
}
