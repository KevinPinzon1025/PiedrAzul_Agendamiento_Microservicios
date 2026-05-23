# Report Service

Microservicio Spring Boot para exportar las citas de una fecha y un profesional en CSV.
El servicio mantiene su propia base de datos y se sincroniza de forma asincrona consumiendo eventos `appointment.created` desde RabbitMQ.

## Ejecutar

```bash
cd report-service
mvn spring-boot:run
```

Por defecto corre en `http://localhost:8084`, usa PostgreSQL en `localhost:5435/reportdb` y consume RabbitMQ en `localhost:5672`.

## Endpoint CSV

```http
GET /reports/appointments/csv?professionalId=7&date=2026-05-20
```

Devuelve un archivo CSV descargable con separador `;`.

Tambien puede filtrar por nombre exacto cuando no se conoce el id:

```http
GET /reports/appointments/csv?professional=Nombre%20Profesional&date=2026-05-20
```

## Endpoint de vista previa

```http
GET /reports/appointments/csv/preview?professionalId=7&date=2026-05-20
```

Devuelve el CSV como texto plano para copiar/pegar.
