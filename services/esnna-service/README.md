# esnna-service · SMID (legado IA) — ESNNA Motor

Microservicio de **análisis y gestión de casos ESNNA** (Explotación Sexual de Niñas, Niños y
Adolescentes) del ecosistema **SMID** — *Sistema de Monitoreo Integral de Derechos* de la
**Defensoría de los Derechos de la Niñez**. Es un **sistema heredado** integrado tras el
**API Gateway** mediante el patrón *strangler*: revalida el token emitido por **Identidad
(`smid-auth`, 6.1)** y se publica bajo `/api/esnna/**`.

El motor de entrada es un pipeline de **IA (OpenAI)** en dos fases (MAP/REDUCE) que extrae y
consolida la información de los oficios en PDF; sobre eso, el servicio aporta ciclo de vida del
caso, un **semáforo de priorización determinista** (calculado en el backend, no por la IA),
expediente auditable y un tablero de priorización.

> El alcance cerrado del refactor, las decisiones de diseño y los hallazgos resueltos están en
> [`DISENO.md`](DISENO.md). Este README es la vista operativa.

---

## Tabla de contenidos

1. [Stack tecnológico](#1-stack-tecnológico)
2. [Arquitectura](#2-arquitectura)
3. [Modelo de datos](#3-modelo-de-datos)
4. [Seguridad y compatibilidad con `smid-auth`](#4-seguridad-y-compatibilidad-con-smid-auth)
5. [Contrato de la API](#5-contrato-de-la-api)
6. [Semáforo determinista](#6-semáforo-determinista)
7. [Variables de entorno](#7-variables-de-entorno)
8. [Ejecución](#8-ejecución)
9. [Estado de integración al ecosistema](#9-estado-de-integración-al-ecosistema)

---

## 1. Stack tecnológico

| Componente | Detalle |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.x |
| Base de datos | MySQL 8 (`db_esnna`) |
| Esquema | Hibernate `ddl-auto=update` (servicio legado; **no usa Flyway**) |
| Seguridad | `oauth2-resource-server`, JWT **HS256** con secreto compartido |
| IA | OpenAI (extracción `gpt-4o-mini`, consolidación `gpt-5.5`) |
| Almacenamiento | MinIO (respaldo de PDFs, **opcional**) |
| Mapeo | MapStruct (entidad ↔ DTO) · Lombok |
| Puerto | `8086` |
| Ruta tras Gateway | `/api/esnna/**` (StripPrefix=0: el controlador espera la ruta completa) |

---

## 2. Arquitectura

Arquitectura **por capas** (controlador → servicio → repositorio), no hexagonal. Es lo esperable de
un sistema heredado integrado vía *strangler*: el contrato externo (ruta, JWT, sobre de error) se
alinea con el ecosistema, pero la estructura interna conserva su forma original.

```
cl.smid.esnna
├── controller     Endpoints REST (EsnnaController)
├── service        Motor IA (EsnnaMotorService), gestión del caso (EsnnaCasoService),
│                  cliente OpenAI (EsnnaGptClient), extracción PDF (PdfExtractionService),
│                  semáforo (SemaforoService), Excel/informe, MinIO, costo, borradores con TTL
├── domain         Enums del dominio (Semaforo, EstadoGestion, RespuestaSiNo, SexoNna)
├── entity         JPA: EsnnaEntity (caso), Imputado, EsnnaAnalisisAudit, DocumentoAnalizado
├── repository     Spring Data JPA + Specifications (filtros combinables del tablero)
├── dto            Records de petición/respuesta (no se serializa la entidad)
├── mapper         MapStruct (mapeo de ~44 campos)
├── config         Seguridad (resource server JWT), RestClient, async, OpenAPI
├── exception      ApiError + @RestControllerAdvice + handlers 401/403
└── util           Utilidades (hash del lote para deduplicación de borradores)
```

---

## 3. Modelo de datos

Tres tablas principales (esquema creado por Hibernate en `db_esnna`):

| Tabla | Rol |
|---|---|
| `esnna_casos` | Caso/expediente. Desdobla el semáforo (`semaforoIA` inmutable + `semaforoFinal` con override humano), estado de gestión, borrado lógico (`anulado` + motivo + autor) y `@Version` (bloqueo optimista). |
| `esnna_imputados` | Una fila por imputado (FK al caso). Reemplaza las tres listas paralelas del legado; `orden` preserva la aparición. |
| `esnna_analisis_audit` | Auditoría **inmutable**, una entrada por guardado: usuario (`sub` del JWT), modelos + snapshot, versión de protocolo, hashes SHA-256 de los documentos (no el contenido), semáforo IA, tokens y costo. |

Detalle de columnas, enums y transiciones de estado en [`DISENO.md`](DISENO.md) §3.

---

## 4. Seguridad y compatibilidad con `smid-auth`

El servicio valida el JWT como **resource server** con `NimbusJwtDecoder.withSecretKey` y
**HS256**: la misma firma simétrica que emite `smid-auth`. La clave (`JWT_SECRET`) debe coincidir
**byte a byte** con la del resto del ecosistema (el *Doctor* del dashboard verifica esa
coherencia). El `kid` del encabezado se ignora al usar clave única, por lo que la rotación de
`smid-auth` no afecta la validación.

- La **autenticación es obligatoria**: el `sub` del token es la trazabilidad legal sobre PII de NNA.
- `iss`/`aud` se validan **solo si** se configuran (`JWT_ISSUER`, `JWT_AUDIENCE`); el emisor real
  es `smid-auth` y la audiencia `smid-servicios`.
- Los roles se leen del claim `roles`.

---

## 5. Contrato de la API

> Rutas tal cual las publica el Gateway (sin StripPrefix). Requieren `Authorization: Bearer <token>`.

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/esnna/casos` | Tablero con filtros combinables (estado, fecha, delito, región, RUC, RUT), paginado |
| `GET` | `/api/esnna/casos/{id}` | Detalle del caso |
| `POST` | `/api/esnna/procesar` | Procesa ≤30 PDFs por IA (MAP/REDUCE); devuelve resultado tipado con `estadoCalidad` y omitidos |
| `POST` | `/api/esnna/guardar` | Persiste el caso (semáforo IA inmutable + override humano), imputados, PDFs en MinIO y auditoría |
| `PATCH` | `/api/esnna/casos/{id}` | Edición parcial |
| `POST` | `/api/esnna/casos/{id}/estado` | Transición de estado validada |
| `DELETE` | `/api/esnna/casos/{id}` | Borrado lógico con motivo y autor |
| `GET` | `/api/esnna/casos/{id}/informe` | Informe PDF del caso |
| `GET` | `/api/esnna/casos/{id}/documento` | PDF fuente desde MinIO |
| `GET` | `/api/esnna/exportar-excel` | Exportación en streaming (saneada contra inyección de fórmulas) |
| `GET` | `/api/esnna/metricas/concordancia` | Concordancia IA vs humano por criterio |

OpenAPI/Swagger: `GET /v3/api-docs` · `GET /swagger-ui/index.html`.

---

## 6. Semáforo determinista

La Fase 2 (consolidación) entrega **evidencia estructurada** de cinco criterios (C1–C5), no el
semáforo. El backend aplica la regla copulativa:

- exclusión o improcedencia aplicable → **VERDE**
- ≥3 criterios cumplidos → **ROJO** · exactamente 2 → **AMARILLO** · ≤1 → **VERDE**

El `semaforoIA` queda inmutable y auditable; el `semaforoFinal` admite override humano con autor y
fecha. Ver [`DISENO.md`](DISENO.md) §3.4.

---

## 7. Variables de entorno

Ver [`.env.example`](.env.example). Resumen:

| Variable | Descripción |
|---|---|
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | Conexión MySQL 8 (`db_esnna`) |
| `JWT_SECRET` | Clave HS256 **compartida** con `smid-auth` (obligatoria) |
| `JWT_ISSUER`, `JWT_AUDIENCE` | Validación opcional de emisor/audiencia |
| `OPENAI_API_KEY` | Clave de OpenAI (**obligatoria**: es el motor) |
| `MINIO_*` | Respaldo de PDFs (opcional, `MINIO_ENABLED=false` por defecto) |

Los secretos provienen **exclusivamente** de variables de entorno; no hay secretos en el código
ni en `application.properties` (DT-2).

---

## 8. Ejecución

```bash
cp .env.example .env   # y completar OPENAI_API_KEY, JWT_SECRET, DB_PASSWORD
mvn spring-boot:run
```

Queda en `http://localhost:8086`. Salud: `GET /actuator/health`. En el ecosistema se levanta con
el runner: `./scripts/siger-services.ps1 start -Services esnna-service` (o desde el dashboard).

---

## 9. Estado de integración al ecosistema

- ✅ **Auth compatible** con `smid-auth` (HS256, secreto compartido).
- ✅ **Secretos externalizados** a `.env` (antes estaban hardcodeados, incluida la API key de OpenAI).
- ⚠️ **Esquema por Hibernate (`ddl-auto=update`)**, no Flyway. Deuda reconocida: migrar a
  `validate` + Flyway en producción, como el resto del Núcleo (y como ya hace `sgs-service`).
- ⚠️ **Marcado `optional`** en `scripts/services.json` mientras se estabiliza.
- Fuera de alcance (deuda explícita): cifrado en reposo de PII, OCR de PDFs escaneados. Ver
  [`DISENO.md`](DISENO.md) §7.
