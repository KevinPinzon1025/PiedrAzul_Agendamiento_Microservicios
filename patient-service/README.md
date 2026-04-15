\# Patient Service - Microservicio



Microservicio desarrollado en \*\*Spring Boot\*\* para la gestión de pacientes, siguiendo arquitectura por capas e integración con base de datos PostgreSQL.



Incluye implementación del patrón \*\*Adapter\*\* para consumir servicios externos.



\---



\## Tecnologías



\- Java 21

\- Spring Boot 4

\- Spring Data JPA

\- PostgreSQL

\- Flyway

\- Docker

\- Maven



\---



\## Arquitectura



El proyecto corresponde a un microservicio, pero internamente está organizado en capas para separar responsabilidades:



\- Controller → exposición de endpoints REST

\- Service → lógica de negocio

\- Repository → acceso a datos

\- Entity / DTO / Mapper → modelado y transformación de datos

\- Adapter → integración con servicios externos



\---



\## Patrón Adapter



Se implementa el patrón Adapter para integrar un servicio externo incompatible.



\### Problema



El servicio externo retorna datos en formato JSON:



```json

{"name":"Jose Lopez"}

\--



\## Obtener paciente externo (Adapter)



``http

GET /api/patients/external

``



Ejemplo de respuesta:



``json

{

&#x20; "firstName": "Jose",

&#x20; "firstLastName": "Lopez",

&#x20; "email": "jose.lopez@example.com"

}

``



\--



\# Health Check



``http

GET /actuator/health

``



\--



\# Estado del proyecto



\* ✔ CRUD de pacientes

\* ✔ Integración con PostgreSQL

\* ✔ Migraciones con Flyway

\* ✔ Implementación de Adapter

\* ✔ Docker funcional



\---



\# Autor



\* Kevin Pinzón



\---



