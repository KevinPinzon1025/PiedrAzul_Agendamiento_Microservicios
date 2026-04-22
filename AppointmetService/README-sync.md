# Appointment Service con comunicación síncrona

Esta versión agrega comunicación síncrona REST hacia `NotificationService`.

## Flujo
1. `POST /appointment`
2. Se guarda la cita en PostgreSQL.
3. El servicio invoca `POST http://localhost:8082/api/notifications/appointment-confirmation`.
4. Si Notification responde con `sent=true`, la cita queda creada y se responde al cliente.
5. Si Notification falla, se lanza excepción para evidenciar el acoplamiento síncrono del escenario 2.

## Configuración
En `application.yaml`:

```yaml
server:
  port: 8081

notification:
  service:
    url: http://localhost:8082
```

## Ejemplo de creación de cita
```json
{
  "schedulingDate": "2026-05-02T09:00:00",
  "appointmenDate": "2026-05-05T10:30:00",
  "observation": "Control general",
  "scheduler": 1,
  "patient": 25,
  "professional": 8
}
```
