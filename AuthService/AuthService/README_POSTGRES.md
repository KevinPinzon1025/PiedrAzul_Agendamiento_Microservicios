# Ejecutar AuthService con PostgreSQL y RabbitMQ

## 1. Levantar PostgreSQL y RabbitMQ

Desde la carpeta del proyecto:

```bash
docker compose up -d
```

Esto crea:

- PostgreSQL en `localhost:5432`
- Base de datos: `authdb`
- Usuario: `postgres`
- Password: `postgres`
- RabbitMQ en `localhost:5672`
- Panel RabbitMQ: `http://localhost:15672` con usuario `guest` y password `guest`

## 2. Ejecutar el microservicio

Ejecuta `AuthServiceApplication` desde IntelliJ o:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## 3. JPA crea las tablas

La propiedad:

```properties
spring.jpa.hibernate.ddl-auto=update
```

hace que Hibernate/JPA cree la tabla `user_credentials` automáticamente si no existe.

## 4. Ver tablas en PostgreSQL

Puedes entrar al contenedor:

```bash
docker exec -it auth-postgres psql -U postgres -d authdb
```

Y consultar:

```sql
SELECT * FROM user_credentials;
```

## 5. Probar en Postman

POST `http://localhost:8081/api/auth/register`

```json
{
  "login": "kevin",
  "password": "Kevin123!",
  "firstName": "Kevin",
  "firstLastName": "Santiago",
  "role": "PATIENT",
  "active": true
}
```
