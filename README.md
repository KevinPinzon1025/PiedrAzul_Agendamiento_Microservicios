# Piedra Azul - API Gateway, Keycloak y microservicios

Este paquete deja el sistema listo para levantarlo con Docker Compose y con el frontend usando contraseña en login/registro.

## Puertos

| Servicio | Puerto |
|---|---:|
| Keycloak | 8080 |
| API Gateway | 8081 |
| AuthService | 8082 |
| AppointmentService | 8083 |
| PatientService | 8084 |
| ProfessionalService | 8085 |

## Rutas por el API Gateway

- `http://localhost:8081/api/auth/**` -> `http://auth-service:8082/api/auth/**`
- `http://localhost:8081/appointment/**` -> `http://appointment-service:8083/appointment/**`
- `http://localhost:8081/api/patients/**` -> `http://patient-service:8084/api/patients/**`
- `http://localhost:8081/professionals/**` -> `http://professional-service:8085/professionals/**`
- `http://localhost:8081/types/**` -> `http://professional-service:8085/types/**`

## Levantar todo con Docker

Desde la raiz del proyecto:

```bash
docker compose up --build
```

Si el login falla con los usuarios 123/124/125 despues de cambiar versiones, reinicia los volumenes para borrar datos antiguos:

```bash
docker compose down -v
docker compose up --build
```

Para detener y borrar contenedores/volumenes:

```bash
docker compose down -v
```

## Keycloak

- URL: `http://localhost:8080`
- Admin: `admin`
- Password: `admin`
- Realm importado: `sistema`
- Cliente: `sistema-desktop`

## AuthService con contraseña

El AuthService ahora recibe contraseña en registro y login.

Regla aplicada:

- Minimo 6 caracteres.
- Maximo 72 caracteres.
- No exige mayusculas, numeros ni simbolos.

Usuarios iniciales de prueba:

| Usuario / documento | Password |
|---|---|
| 123 | 123456 |
| 124 | 123456 |
| 125 | 123456 |

## Frontend

El frontend JavaFX conserva el campo de contraseña en login y registro. Todas las ventanas principales tienen scroll, muestran la nota "Los campos con * son obligatorios." y marcan los campos obligatorios con *.

Clientes configurados:

- Auth: `http://localhost:8081/api/auth`
- Appointment: `http://localhost:8083/appointment`
- Patient: `http://localhost:8084/api/patients`

El frontend se ejecuta fuera de Docker porque abre interfaz grafica de escritorio.
