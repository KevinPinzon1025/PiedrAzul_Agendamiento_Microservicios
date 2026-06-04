# AuthService con Keycloak/JWT

Este microservicio fue ajustado para autenticar contra Keycloak y devolver tokens JWT emitidos por el realm `sistema`.

## Usuarios de prueba incluidos en `keycloak/import/sistema-realm.json`

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 123456 | ADMIN |
| agendador | 123456 | SCHEDULER |
| medico | 123456 | PROFESSIONAL |
| paciente | 123456 | PATIENT |

## Levantar servicios

Desde la raíz del proyecto:

```bash
docker compose up --build
```

## Login desde AuthService

```http
POST http://localhost:8082/api/auth/login
Content-Type: application/json
```

```json
{
  "login": "admin",
  "password": "123456"
}
```

La respuesta incluye `access_token`, `refresh_token`, `expires_in` y `token_type`.

## Validar token

```http
GET http://localhost:8082/api/auth/validate
Authorization: Bearer TU_ACCESS_TOKEN
```

## Gestión de usuarios y roles por Postman

1. Obtener token admin:

```http
POST http://localhost:8080/realms/master/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded
```

Body `x-www-form-urlencoded`:

```text
grant_type=password
client_id=admin-cli
username=admin
password=admin
```

2. Crear usuario:

```http
POST http://localhost:8080/admin/realms/sistema/users
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json
```

```json
{
  "username": "paciente2",
  "enabled": true,
  "firstName": "Paciente",
  "lastName": "Dos",
  "email": "paciente2@test.com",
  "credentials": [
    {
      "type": "password",
      "value": "123456",
      "temporary": false
    }
  ]
}
```
