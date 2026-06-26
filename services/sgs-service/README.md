# sgs-service · SMID (legado IA) — SGS, Gestión y Seguimiento

Microservicio del **Sistema de Gestión y Seguimiento (SGS)** del ecosistema **SMID** — *Sistema
de Monitoreo Integral de Derechos* de la **Defensoría de los Derechos de la Niñez**. Es un
**sistema heredado** integrado tras el **API Gateway** mediante el patrón *strangler*: revalida el
token de **Identidad (`smid-auth`, 6.1)** y se publica bajo `/api/sgs/**`.

SGS procesa **oficios** (documentos oficiales) con un motor de **IA (OpenAI)**: extrae las
**recomendaciones**, las **evalúa contra una rúbrica** de cumplimiento, y mantiene el
**seguimiento** en el tiempo (fases, plazos y **alertas**). El procesamiento pesado corre de forma
**asíncrona** (RabbitMQ + jobs consultables).

---

## Tabla de contenidos

1. [Stack tecnológico](#1-stack-tecnológico)
2. [Arquitectura](#2-arquitectura)
3. [Modelo de datos](#3-modelo-de-datos)
4. [Seguridad y compatibilidad con `smid-auth`](#4-seguridad-y-compatibilidad-con-smid-auth)
5. [Contrato de la API](#5-contrato-de-la-api)
6. [Variables de entorno](#6-variables-de-entorno)
7. [Ejecución](#7-ejecución)
8. [Estado de integración al ecosistema](#8-estado-de-integración-al-ecosistema)

---

## 1. Stack tecnológico

| Componente | Detalle |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.x |
| Base de datos | MySQL 8 (`db_sgs`) |
| Esquema | **Flyway** (`db/migration/V1__inicial.sql`) + Hibernate `ddl-auto=validate` |
| Seguridad | `oauth2-resource-server`, JWT **HS256** con secreto compartido |
| IA | OpenAI (extracción `gpt-5-mini`, evaluación `gpt-5.5`) |
| Mensajería | RabbitMQ (procesamiento asíncrono) |
| Almacenamiento | MinIO (expedientes) |
| Resiliencia | Resilience4j (retry sobre 429/5xx de OpenAI) |
| Notificaciones | Telegram (alertas, **opcional**, desactivado por defecto) |
| Mapeo | MapStruct · Lombok |
| Puerto | `8083` |
| Ruta tras Gateway | `/api/sgs/**` (StripPrefix=0: el controlador espera la ruta completa) |

---

## 2. Arquitectura

Arquitectura **por capas** (controlador → servicio → repositorio), no hexagonal. Es lo esperable de
un sistema heredado integrado vía *strangler*: el contrato externo (ruta, JWT, esquema) se alinea
con el ecosistema, pero la estructura interna conserva su forma original.

```
cl.smid.sgs
├── controller   4 controladores: Procesamiento, Consulta, Oficio, Job
├── service      Extracción (ExtraccionService) y evaluación (EvaluacionService) con IA,
│                alertas (AlertaService), catálogos, jobs (JobService), métricas, Excel, informe
├── client       OpenAiClient (llamadas al modelo)
├── messaging    RabbitMQ: encola y consume los jobs de procesamiento asíncrono
├── scheduler    Alertas programadas (cron de seguimiento)
├── entity       JPA: Oficio, Recomendacion, Seguimiento, AlertaSeguimiento, Catalogo,
│                Accion, JobEstado, DocumentoHash, SgsAnalisisAudit
├── repository   Spring Data JPA
├── dto          in / internal / out (entrada, intermedios IA, respuesta)
├── enums        EstadoGestion, EvaluacionCumplimiento, FaseSeguimiento, JobStatus, ...
├── mapper       MapStruct (entidad ↔ DTO)
├── config       Seguridad JWT, OpenAI, MinIO, RabbitMQ, Telegram
└── exception    ApiError + @RestControllerAdvice
```

---

## 3. Modelo de datos

Nueve tablas en `db_sgs`, versionadas en [`V1__inicial.sql`](src/main/resources/db/migration/V1__inicial.sql):

| Tabla | Rol |
|---|---|
| `sgs_oficio` | Oficio/documento oficial procesado |
| `sgs_recomendacion` | Recomendaciones extraídas de un oficio (FK a oficio, materia y categoría) |
| `sgs_accion` | Acciones de una recomendación (FK a recomendación, `orden`) |
| `sgs_seguimiento` | Seguimiento en el tiempo de cada recomendación (fase, plazo, evaluación) |
| `sgs_alerta` | Alertas de seguimiento generadas (vencimientos, hitos) |
| `sgs_catalogo` | Catálogos del dominio (materias, tipos, etc.) |
| `sgs_job` | Estado de los jobs asíncronos de procesamiento IA |
| `sgs_analisis_audit` | Auditoría del análisis IA (modelo, versión de rúbrica, tokens, costo) |
| `sgs_audit_documento` | Hashes de los documentos analizados (trazabilidad, no contenido) |

Flyway es dueño del esquema: en una BD vacía crea las 9 tablas; en una BD ya poblada hace baseline
sin re-ejecutar el script (ver §8). Hibernate solo valida el mapeo.

---

## 4. Seguridad y compatibilidad con `smid-auth`

Valida el JWT como **resource server** con `NimbusJwtDecoder.withSecretKey` y **HS256** — la misma
firma simétrica que emite `smid-auth`. La clave (`JWT_SECRET`) debe coincidir **byte a byte** con
la del ecosistema (el *Doctor* del dashboard lo verifica). El `kid` se ignora con clave única.

- **Autenticación obligatoria**: el `sub` del token es la trazabilidad legal sobre PII de NNA.
- No se validan `iss`/`aud` (decisión heredada: cualquier autenticado opera; los roles del claim
  `roles` quedan mapeados y listos para cerrar endpoints sin recablear).

---

## 5. Contrato de la API

> Rutas tal cual las publica el Gateway. Requieren `Authorization: Bearer <token>`.

**Procesamiento (asíncrono)**

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/sgs/procesar-pdf` | Encola la extracción de recomendaciones desde un PDF; devuelve un *job* aceptado |
| `POST` | `/api/sgs/procesar-respuesta` | Encola el análisis de una respuesta/seguimiento |
| `POST` | `/api/sgs/evaluacion-aplicar` | Aplica la evaluación de cumplimiento propuesta por la IA |
| `POST` | `/api/sgs/guardar` | Persiste el resultado revisado |
| `GET` | `/api/sgs/jobs/{jobId}` | Estado del job asíncrono |

**Oficios, recomendaciones y seguimiento**

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/sgs/oficios` | Tablero paginado de oficios |
| `GET` | `/api/sgs/oficios/{id}` | Detalle del oficio |
| `GET` | `/api/sgs/oficios/{id}/informe` | Informe PDF |
| `GET` | `/api/sgs/oficios/{id}/documento` | Documento fuente (MinIO) |
| `PATCH` | `/api/sgs/recomendaciones/{id}` | Edición parcial de una recomendación |
| `POST` | `/api/sgs/recomendaciones/{id}/estado` | Transición de estado |
| `DELETE` | `/api/sgs/recomendaciones/{id}` | Anulación (borrado lógico) |

**Consulta y métricas**

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/sgs/alertas` | Alertas de seguimiento vigentes |
| `GET` | `/api/sgs/catalogos` | Catálogos del dominio |
| `GET` | `/api/sgs/metricas/concordancia` | Concordancia IA vs humano |
| `GET` | `/api/sgs/exportar-excel` | Exportación a Excel |

OpenAPI/Swagger: `GET /v3/api-docs` · `GET /swagger-ui/index.html`.

---

## 6. Variables de entorno

Ver [`.env.example`](.env.example). Resumen:

| Variable | Descripción |
|---|---|
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | Conexión MySQL 8 (`db_sgs`) |
| `JWT_SECRET` | Clave HS256 **compartida** con `smid-auth` (obligatoria) |
| `OPENAI_API_KEY` | Clave de OpenAI (**obligatoria**: es el motor) |
| `RABBITMQ_*` | Conexión a RabbitMQ (procesamiento asíncrono) |
| `MINIO_*` | Almacenamiento de expedientes |
| `TELEGRAM_*` | Notificación de alertas (opcional) |

Los secretos provienen **exclusivamente** de variables de entorno; no hay secretos en el código
ni en `application.properties` (DT-2).

---

## 7. Ejecución

```bash
cp .env.example .env   # y completar OPENAI_API_KEY, JWT_SECRET, DB_PASSWORD, RABBITMQ_*
mvn spring-boot:run
```

Queda en `http://localhost:8083`. Salud: `GET /actuator/health`. En el ecosistema se levanta con
el runner: `./scripts/siger-services.ps1 start -Services sgs-service` (o desde el dashboard).
Requiere **MySQL** y **RabbitMQ** en marcha.

---

## 8. Estado de integración al ecosistema

- ✅ **Auth compatible** con `smid-auth` (HS256, secreto compartido).
- ✅ **Secretos externalizados** a `.env` (antes estaban hardcodeados: API key de OpenAI, token de
  Telegram y credenciales MinIO).
- ✅ **Esquema versionado con Flyway** (`db/migration/V1__inicial.sql`, reconstruido desde la BD
  del legado). En una `db_sgs` **vacía** Flyway crea las 9 tablas; en una BD **ya poblada**,
  `baseline-on-migrate` marca V1 como aplicado sin re-ejecutarlo (conserva los datos). Hibernate
  solo valida.
- ⚠️ Sigue marcado `optional` en `scripts/services.json` por sus dependencias externas pesadas
  (OpenAI obligatorio, RabbitMQ y MinIO), no por el esquema.
