# instituciones-service · SMID 6.10 — Instituciones

Microservicio del ecosistema **SMID** (Sistema de Monitoreo Integral de Derechos) de la Defensoría de
los Derechos de la Niñez. Mantiene el **catálogo nacional de instituciones**, sus
**tipos** de clasificación y sus **puntos focales** (contactos). Es un *servicio hoja*:
no invoca a otros servicios; solo valida el JWT emitido por Identidad (6.1) y emite
eventos de dominio *metadata-only*.

> **Desviación de alcance documentada (override #6).** A diferencia de los servicios de
> negocio territoriales, las instituciones son **datos de referencia nacionales**. Por
> tanto: las **lecturas** son visibles para cualquier usuario autenticado (sin filtro por
> sede/unidad y **sin** denegación territorial 404), y las **escrituras** exigen un **rol
> administrador** (`smid.seguridad.roles-admin`). Sin ese rol, la respuesta es
> `AUTZ-004 / 403` (no 404).

---

## 1. Stack

| Capa | Tecnología |
| --- | --- |
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.x (Web, Data JPA, Validation, Security, AMQP, Actuator) |
| Persistencia | MySQL 8 (InnoDB, `utf8mb4_0900_ai_ci`), Flyway (`ddl-auto=validate`) |
| Mensajería | RabbitMQ (transporte conmutable; *log* por defecto) |
| JWT | jjwt 0.12.5 (HS256 con `kid`) |
| Build | Maven (sin wrapper) |
| Pruebas | JUnit 5, AssertJ, Testcontainers (MySQL) |

Puerto por defecto: **8093**. Tras el Gateway (`StripPrefix=1`), la ruta pública
`/api/instituciones/**` llega al servicio como `/instituciones/**` (y `/tipos/**`).

---

## 2. Arquitectura (hexagonal estricta)

```
api/                     Controladores REST, DTOs, ensamblador y sobre de error
  dto/                   Records de request/response
  error/                 SobreError + ManejadorGlobalExcepciones
dominio/                 POJOs puros (sin Spring/JPA/Lombok)
  modelo/                Agregados (TipoInstitucion, Institucion, PuntoFocal), VOs, filtros
  excepcion/             Jerarquía de excepciones + CodigoError
  puerto/entrada/        Casos de uso (+ comandos)
  puerto/salida/         Puertos de repositorio, eventos, reloj e identificadores
  servicio/              ServicioInstituciones (orquestador de los 3 casos de uso)
infraestructura/
  persistencia/          Entidades JPA, repositorios Spring Data, mapeadores y adaptadores
  eventos/               Publicador por log / por RabbitMQ
  seguridad/             Validación de JWT, filtro, contexto, handlers 401/403
  soporte/               RelojSistema, GeneradorUuid
config/                  CableadoDominio, ConfiguracionSeguridad, ConfiguracionRabbitComun
```

El **dominio** es POJO puro y se **cablea** en `config.CableadoDominio`. El
`@Transactional` vive **solo** en los controladores (frontera). Los identificadores
internos `BIGINT` nunca se exponen: hacia afuera solo viaja el `alt_key` (UUID).

---

## 3. Modelo

- **TipoInstitucion** — catálogo plano de clasificación. `nombre` único, `ambito`
  (`JUDICIAL`, `SALUD`, `EDUCACION`, `PROTECCION`, `POLICIAL`, `MUNICIPAL`, `OTRO`),
  `descripcion` opcional, `vigente`. CRUD de administrador; sin máquina de estados.
- **Institucion** — raíz del agregado. `codigo` opcional y único, `nombre` obligatorio,
  `tipoAlt` obligatorio (el tipo debe existir y estar **vigente**), `rut` opcional
  (validado por **módulo 11**, almacenado en `rut`+`dv`), datos de ubicación/contacto
  opcionales, `activa`. Búsqueda por RUT vía `GET /instituciones?rut=`.
- **PuntoFocal** — contacto de una institución. `nombre` obligatorio, `cargo`/`email`/
  `telefono` opcionales, `principal`, `activo`. **Invariante:** a lo sumo **un** punto
  focal principal **activo** por institución (se sostiene con un desmarcado masivo dentro
  de la transacción del controlador).

---

## 4. API

Todas las rutas requieren `Authorization: Bearer <jwt>`. Las **escrituras** requieren rol
administrador.

### Tipos

| Método | Ruta | Descripción | Escritura |
| --- | --- | --- | --- |
| `POST` | `/tipos` | Crear tipo | sí |
| `GET` | `/tipos/{altKey}` | Obtener tipo | no |
| `PUT` | `/tipos/{altKey}` | Editar tipo | sí |
| `POST` | `/tipos/{altKey}/activacion` | Cambiar vigencia | sí |
| `GET` | `/tipos` | Listar (filtros: `ambito`, `texto`, `vigente`, `pagina`, `tamano`) | no |

```json
// POST /tipos
{ "nombre": "Hospital", "ambito": "SALUD", "descripcion": "Establecimientos de salud" }

// 201 Created
{
  "altKey": "0f4c...-uuid",
  "nombre": "Hospital",
  "ambito": "SALUD",
  "descripcion": "Establecimientos de salud",
  "vigente": true,
  "creadoEn": "2027-01-01T12:00:00Z",
  "actualizadoEn": "2027-01-01T12:00:00Z"
}

// POST /tipos/{altKey}/activacion
{ "vigente": false }
```

### Instituciones

| Método | Ruta | Descripción | Escritura |
| --- | --- | --- | --- |
| `POST` | `/instituciones` | Crear institución | sí |
| `GET` | `/instituciones/{altKey}` | Detalle (incluye puntos focales) | no |
| `PUT` | `/instituciones/{altKey}` | Editar | sí |
| `POST` | `/instituciones/{altKey}/activacion` | Activar/desactivar | sí |
| `GET` | `/instituciones` | Listar (filtros: `tipoAlt`, `ambito`, `texto`, `region`, `activa`, `rut`, `pagina`, `tamano`) | no |

```json
// POST /instituciones
{
  "codigo": "H-001",
  "nombre": "Hospital Central",
  "tipoAlt": "0f4c...-uuid",
  "rut": "12.345.678-5",
  "regionCodigo": "13",
  "comunaCodigo": "13101",
  "direccion": "Av. Principal 100",
  "telefono": "+56 2 2555 0000",
  "email": "contacto@hospital.cl",
  "sitioWeb": "https://hospital.cl"
}

// 201 Created (detalle)
{
  "altKey": "9a1b...-uuid",
  "codigo": "H-001",
  "nombre": "Hospital Central",
  "tipoAlt": "0f4c...-uuid",
  "tipoNombre": "Hospital",
  "ambito": "SALUD",
  "rut": "12345678-5",
  "regionCodigo": "13",
  "comunaCodigo": "13101",
  "direccion": "Av. Principal 100",
  "telefono": "+56 2 2555 0000",
  "email": "contacto@hospital.cl",
  "sitioWeb": "https://hospital.cl",
  "activa": true,
  "creadoEn": "2027-01-01T12:00:00Z",
  "actualizadoEn": "2027-01-01T12:00:00Z",
  "puntosFocales": []
}
```

### Puntos focales

| Método | Ruta | Descripción | Escritura |
| --- | --- | --- | --- |
| `POST` | `/instituciones/{institucionAlt}/puntos-focales` | Crear punto focal | sí |
| `PUT` | `/instituciones/puntos-focales/{altKey}` | Editar | sí |
| `POST` | `/instituciones/puntos-focales/{altKey}/activacion` | Activar/desactivar | sí |

La lectura de puntos focales se obtiene a través del **detalle** de la institución.

```json
// POST /instituciones/{institucionAlt}/puntos-focales
{ "nombre": "Ana Pérez", "cargo": "Coordinadora", "email": "ana@hospital.cl", "telefono": "+56 9 9999 9999", "principal": true }
```

### Paginación

```json
{ "contenido": [ ... ], "pagina": 0, "tamano": 20, "totalElementos": 42, "totalPaginas": 3 }
```

---

## 5. Eventos de dominio (metadata-only)

El `alt_key` viaja en el **sobre** del evento; los metadatos llevan solo contexto mínimo
(nunca relatos, nombres de personas ni RUT de personas). Transporte conmutable con
`smid.eventos.transporte` (`log` por defecto, `matchIfMissing=true`; `rabbitmq` publica en
el exchange de tópicos `smid.eventos` usando el tipo como *routing key*).

| Evento | Metadatos |
| --- | --- |
| `tipo.creado` | `nombre`, `ambito` |
| `tipo.activacion` | `vigente` |
| `institucion.creada` | `nombre`, `tipoAlt`, `ambito` |
| `institucion.actualizada` | — |
| `institucion.activacion` | `activa` |
| `puntofocal.creado` | `institucionAlt` |
| `puntofocal.activacion` | `activo` |

---

## 6. Sobre de error unificado

Campo de ruta **`ruta`** (no `path`); `detalles` solo en validación; `timestamp` UTC
ISO-8601.

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "codigo": "INS-422",
  "mensaje": "El tipo de institución '...' no está vigente y no puede asociarse.",
  "ruta": "/instituciones",
  "timestamp": "2027-01-01T12:00:00Z"
}
```

| Código | HTTP | Situación |
| --- | --- | --- |
| `INS-001` | 400 | Validación de entrada / cuerpo ilegible / parámetro inválido |
| `INS-404` | 404 | Recurso inexistente |
| `INS-409` | 409 | Conflicto de unicidad (nombre de tipo o código de institución) |
| `INS-422` | 422 | Regla de negocio (tipo inexistente/no vigente, RUT inválido) |
| `INS-500` | 500 | Error interno |
| `AUTZ-003` | 401 | No autenticado (token ausente/inválido/expirado) |
| `AUTZ-004` | 403 | Autenticado sin rol administrador |

---

## 7. Variables de entorno

| Variable | Descripción | Default |
| --- | --- | --- |
| `DB_URL` | URL JDBC MySQL | `jdbc:mysql://localhost:3306/db_instituciones?...&tinyInt1isBit=true&connectionTimeZone=UTC` |
| `DB_USER` | Usuario de BD | `smid_instituciones` |
| `DB_PASSWORD` | Clave de BD | *(obligatoria)* |
| `SERVER_PORT` | Puerto HTTP | `8093` |
| `JWT_SECRET` | Clave HS256 (≥ 256 bits). **Compartida byte a byte con el Gateway/Identidad.** | *(obligatoria)* |
| `JWT_KID_ACTIVO` | `kid` de la clave activa | `smid-2026-06` |
| `JWT_KID_PREVIO` | `kid` de la clave previa (rotación) | *(vacío)* |
| `JWT_SECRET_PREVIO` | Clave previa (rotación) | *(vacío)* |
| `JWT_ISSUER` | Claim `iss` esperado | `smid-auth` |
| `JWT_AUDIENCE` | Claim `aud` esperado (contenido en `aud`) | `smid-servicios` |
| `ROLES_ADMIN` | Roles que habilitan escritura | `ADMIN_INSTITUCIONES,COORDINACION_NACIONAL` |
| `EVENTOS_TRANSPORTE` | `log` o `rabbitmq` | `log` |
| `RABBIT_HOST` / `RABBIT_PORT` / `RABBIT_USER` / `RABBIT_PASSWORD` / `RABBIT_VHOST` | Conexión RabbitMQ (si `rabbitmq`) | `localhost` / `5672` / `guest` / `guest` / `/` |

---

## 8. Build, pruebas y ejecución

```bash
# Compilar y empaquetar
mvn clean package

# Solo pruebas unitarias de dominio (sin Docker)
mvn test -Dtest='RutTest,ServicioInstitucionesTest'

# Pruebas de integración (requieren Docker; si no hay, se omiten automáticamente)
mvn verify

# Ejecutar (perfil local: eventos por log, SQL visible)
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

> El esquema lo crea **Flyway** (`V1__inicial.sql`) e Hibernate solo **valida**
> (`ddl-auto=validate`). La prueba de integración usa **Testcontainers** y se anula sola
> sin Docker (`@Testcontainers(disabledWithoutDocker = true)`).

---

## 9. Decisiones de diseño propias de 6.10

1. **Datos de referencia nacionales (override #6):** lectura pública autenticada;
   escritura por rol administrador; sin columnas ni filtros territoriales; sin 404
   territorial.
2. **Autorización en el dominio:** `ServicioInstituciones` recibe el `ContextoSesion` y el
   conjunto de roles administradores; las lecturas no requieren contexto.
3. **RUT opcional:** objeto de valor `Rut` (módulo 11) almacenado descompuesto en
   `rut`+`dv`; se reconstruye sin re-validar al leer.
4. **Relaciones por PK escalar:** `tipo_id` / `institucion_id` sin asociaciones JPA
   bidireccionales; los adaptadores traducen `alt_key ↔ id`. Los filtros por tipo/ámbito
   se resuelven con **subconsultas** sobre `tipo_institucion`.
5. **Sin N+1 en listados:** el adaptador de instituciones carga los tipos de la página por
   lotes (`findByIdIn`).
6. **Invariante de principal único:** desmarcado masivo (`@Modifying`) ejecutado por el
   dominio tras guardar, dentro de la transacción del controlador.
7. **`ProveedorContexto`** respaldado por atributos de request (no `@RequestScope`) para
   evitar problemas de temporización con el filtro de seguridad.
