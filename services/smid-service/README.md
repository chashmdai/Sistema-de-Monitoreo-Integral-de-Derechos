# smid-service

Microservicio Core del Sistema Integral de Monitoreo de Derechos (SMID), refactorizado desde el monolito SIMD original.

## Stack

- Java 21
- Spring Boot 3.5.14
- Spring Data JPA + Hibernate
- MySQL 8
- Lombok

## Puerto

`8085` (ruteado desde el BFF Gateway via `/api/smid/**` con `StripPrefix=2`).

## Arquitectura

Stateless. Sin Spring Security local: el BFF Gateway (puerto 8080) valida JWT HS256 antes de rutear. Sin CORS local: el Gateway lo maneja.

## Estructura

```
cl.smid.smidservice/
├── SmidServiceApplication.java
├── config/        DataLoader (catalogos), WebConfig
├── controller/    5 RestControllers
├── dto/           5 DTOs de respuesta
├── entity/        9 entidades + 2 enums
├── repository/    9 repositorios JPA
├── service/       4 servicios (Scoring, Deadline, Legislativo, Reporte)
└── exception/     ResourceNotFoundException
```

## Endpoints

Todos los paths a continuacion son **internos del microservicio**. Desde el front React se acceden con el prefijo `/api/smid/...` via Gateway.

### Requerimientos
- `GET    /requerimientos`              listar todos
- `GET    /requerimientos/form-data`    factores para form de creacion
- `POST   /requerimientos`              crear
- `GET    /requerimientos/{codigo}`     ver caso completo (con evaluaciones, oficios e indicadores)
- `DELETE /requerimientos/{codigo}`     eliminar

### Evaluaciones
- `GET  /evaluaciones/form-data/{reqId}`   instituciones, derechos y requerimiento
- `POST /evaluaciones`                     crear (calcula indice y flag de alerta critica)

### Oficios
- `GET  /oficios/form-data/{evalId}`   evaluacion, tipos documento y plazo default
- `POST /oficios`                      crear (calcula vencimiento habiles y estado)

### Legislativo
- `GET  /legislativo/leyes`                       listar
- `POST /legislativo/leyes`                       crear ley
- `GET  /legislativo/leyes/{id}`                  ver ley + tramites ordenados
- `GET  /legislativo/tramites/form-data/{leyId}`  data para crear tramite
- `POST /legislativo/tramites`                    crear (calcula IRN, IRRI, indice compuesto)

### Reportes
- `GET /reportes/dashboard`              payload consolidado del dashboard
- `GET /reportes/comparar?g1=&g2=`       comparacion entre dos instituciones

## Base de datos

Configuracion en `application.properties`. Por defecto:
- URL: `jdbc:mysql://localhost:3306/smid_db`
- usuario: `root`
- timezone: `America/Santiago`

El `DataLoader` inicializa al primer arranque los catalogos base: 4 Ejes, 6 Derechos, 5 Factores de Riesgo, 6 Instituciones.

## Que NO esta aqui (vs monolito SIMD)

Estos componentes migraron al Auth Service y NO existen en este microservicio:
- Login, autenticacion, usuarios, roles
- Auditoria (`AuditoriaService`, tabla `auditoria`)
- Backup encriptado (`BackupService`, `EncryptionUtil`)
- Templates Thymeleaf (front migrado a React)
- `CustomErrorController` (Gateway maneja errores globales)

## Ruta en el BFF Gateway

En este monorepo ya esta registrado en el Gateway:

```yaml
- id: smid-service
  uri: ${SMID_SERVICE_URL:http://localhost:8085}
  predicates:
    - Path=/api/smid/**
  filters:
    - StripPrefix=2
```
