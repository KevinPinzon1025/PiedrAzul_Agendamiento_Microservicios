# Notification Service

Microservicio REST para el escenario 2 del taller: recibe una solicitud síncrona desde AppointmentService y simula el envío de la notificación mediante log.

## Endpoint

`POST /api/notifications/appointment-confirmation`

### Request body

```json
{
  "appointmentId": 1,
  "patientId": 25,
  "professionalId": 8,
  "appointmentDateTime": "2026-05-05T10:30:00",
  "observation": "Control general"
}
```

### Respuesta

```json
{
  "sent": true,
  "message": "Notificacion enviada correctamente"
}
```
