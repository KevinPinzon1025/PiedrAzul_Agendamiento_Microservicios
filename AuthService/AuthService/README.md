# AuthService

Microservicio de autenticacion para Piedra Azul construido con Spring Boot.

## Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/validate`

## Ejecutar

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

## Usuario semilla

- login: `admin`
- password: `Admin1*`

## Ejemplo de registro

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "login": "paciente1",
    "password": "Paciente1*",
    "firstName": "Kevin",
    "firstLastName": "Santiago",
    "role": "PATIENT",
    "active": true
  }'
```

## Ejemplo de login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "paciente1",
    "password": "Paciente1*"
  }'
```

## Ejemplo de validacion

```bash
curl http://localhost:8081/api/auth/validate \
  -H "Authorization: Bearer TU_TOKEN"
```

## Notas de integracion con el monolito

Este microservicio conserva la politica de contrasenas y el hashing PBKDF2 con salt que aparecian en el monolito original.
