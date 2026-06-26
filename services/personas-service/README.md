# personas-service · SMID 6.2 — Personas

Servicio **Personas** del Sistema de Monitoreo Integral de Derechos (SMID) de la **Defensoría de los Derechos
de la Niñez**. Es el registro maestro de los *sujetos de derechos* y demás personas (NNA,
adultos, personas jurídicas y testigos) sobre el que se apoyan el resto de los módulos del
ecosistema (Requerimientos, Causas, etc.).

Microservicio **Spring Boot** con **arquitectura hexagonal** (puertos y adaptadores). Se integra
con el resto del clúster SMID detrás del **API Gateway** y revalida el token emitido por
**Identidad (6.1)** como defensa en profundidad.

---

## 1. Tabla de contenidos

1. [Stack tecnológico](#2-stack-tecnológico)
2. [Arquitectura](#3-arquitectura)
3. [Decisión de arranque sin migración](#4-decisión-de-arranque-sin-migración)
4. [Modelo de datos](#5-modelo-de-datos)
5. [Seguridad y contexto territorial](#6-seguridad-y-contexto-territorial)
6. [Contrato de la API](#7-contrato-de-la-api)
7. [Códigos de error](#8-códigos-de-error)
8. [Eventos de dominio](#9-eventos-de-dominio)
9. [Variables de entorno](#10-variables-de-entorno)
10. [Ejecución](#11-ejecución)
11. [Pruebas](#12-pruebas)
12. [Decisiones de diseño y discrepancias resueltas](#13-decisiones-de-diseño-y-discrepancias-resueltas)

---

## 2. Stack tecnológico

| Componente | Versión / detalle |
|---|---|
| Java | 21 (hilos virtuales habilitados) |
| Spring Boot | 3.5.15 |
| Build | Maven (se usa el `mvn` del sistema, sin wrapper) |
| Base de datos | MySQL 8 (InnoDB, `utf8mb4_0900_ai_ci`) |
| Esquema | Flyway (`ddl-auto=validate`) |
| Tokens | jjwt 0.12.5 (HS256 con `kid`) |
| OpenAPI | SpringDoc OpenAPI 2.8.x — Swagger UI MVC |
| Mensajería | Spring AMQP (RabbitMQ), conmutable |
| Utilidades | Lombok (sólo en entidades/adaptadores) |
| Puerto | `8088` |

El servicio queda detrás del Gateway en la ruta `/api/personas/**`. El Gateway aplica
`StripPrefix` sobre `/api`, por lo que **internamente los controladores cuelgan de
`/personas/...`**.

---

## 3. Arquitectura

Hexagonal estricta. El **dominio es puro**: sin anotaciones de framework, sin Lombok, sin JPA.

```
cl.smid.personas
├── api                  Adaptador de entrada HTTP (controladores, DTOs, sobre de error)
│   ├── dto              Records de petición/respuesta
│   └── error            ErrorResponse + @RestControllerAdvice
├── dominio              Núcleo puro (no conoce Spring)
│   ├── modelo           Agregados y objetos de valor (Persona, Rut, ClaveDedup, ...)
│   ├── excepcion        Errores de negocio + catálogo de códigos
│   ├── servicio         Lógica: ServicioPersonas, EvaluadorAlcance, dedup, similitud
│   └── puerto
│       ├── entrada      Casos de uso (GestionPersonasUseCase)
│       └── salida       Puertos hacia el exterior (repositorio, eventos, reloj, ...)
├── infraestructura      Adaptadores de salida
│   ├── persistencia     JPA: entidades, repositorios, mapeadores
│   ├── seguridad        Validación JWT, filtro, principal, handlers 401/403
│   └── eventos          Publicadores de eventos (log / RabbitMQ)
└── config               Raíz de composición (cablea los POJOs del dominio como beans)
```

**Reglas que se respetan en todo el código:**

- El dominio expresa *qué* necesita mediante **puertos**; la infraestructura los implementa.
- Las **transacciones se demarcan en el controlador** (`@Transactional`), no en el dominio.
- La PK interna (`BIGINT id`) **jamás cruza la API**: hacia afuera sólo viaja el `altKey` (UUID
  opaco). Esto es *encapsulación jerárquica* (G7).
- El `id_sede` / `id_unidad` son `alt_key` (UUID), nunca claves internas.

---

## 4. Decisión de arranque sin migración

> **Regla anulatoria del proyecto.** La base de datos arranca **vacía**. Flyway crea
> **únicamente el esquema** (`V1__inicial.sql`). **No** hay semilla, **no** hay ETL ni migración
> del padrón histórico **SIGER** (~27.000 registros), **no** hay cadena de custodia de datos
> legados.

Aunque parte de la documentación funcional contempla migrar el padrón, en esta entrega esa tarea
**se revierte deliberadamente**. El padrón SIGER permanece congelado y se consultará **sólo** a
través de un futuro `siger-service` (un servicio aparte, detrás del Gateway). El
`personas-service` **nunca** lee ni escribe el sistema legado.

Esta decisión deja una **costura limpia para el futuro**: el puerto de salida
`BuscadorDuplicados` permite añadir más adelante un adaptador que, además de la base local,
consulte a `siger-service` (por RUT y por nombre) **sin reescribir el dominio**.

El personal interno (funcionarios) **no es alcance** de Personas: vive en Identidad (6.1).

---

## 5. Modelo de datos

Una migración Flyway: `src/main/resources/db/migration/V1__inicial.sql`.

**Tabla `persona`** (campos principales):

| Columna | Tipo | Notas |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Interna; no sale por la API |
| `alt_key` | CHAR(36) UNIQUE | Identificador público (UUID) |
| `tipo` | VARCHAR(20) + CHECK | `NNA`, `ADULTO`, `JURIDICA`, `TESTIGO` |
| `rut` | VARCHAR(12) UNIQUE | **Nulable**; único entre los no nulos |
| `dv` | CHAR(1) | Dígito verificador |
| `nombres`, `apellido_paterno`, `apellido_materno` | VARCHAR | Persona natural |
| `razon_social` | VARCHAR(200) | Persona jurídica |
| `fecha_nacimiento` | DATE | |
| `sexo` | VARCHAR(20) + CHECK | `F`, `M`, `OTRO`, `NO_INFORMA` |
| `nacionalidad` | VARCHAR(60) | |
| `hash_dedup` | VARCHAR(64) | SHA-256 de la clave de deduplicación |
| `id_sede`, `id_unidad` | CHAR(36) | `alt_key` territoriales (del token) |
| `vigente` | TINYINT(1) | |
| `creado_en`, `actualizado_en` | DATETIME(6) | UTC |
| `creado_por` | CHAR(36) | `alt_key` del autor (claim `sub`) |

**Tabla `persona_contacto`**: `id` PK, `id_persona` FK, `tipo` (`TELEFONO`/`EMAIL`/`DIRECCION`)
+ CHECK, `valor` VARCHAR(240).

Índices para deduplicación (`hash_dedup`, `apellido_paterno`, `fecha_nacimiento`), acceso
territorial (`id_sede`, `id_unidad`) y vigencia.

> **Nota sobre el RUT único y nulable:** MySQL permite múltiples `NULL` en una columna `UNIQUE`,
> lo que habilita registrar varios NNA sin RUT y, a la vez, garantizar unicidad cuando el RUT sí
> está presente.

---

## 6. Seguridad y contexto territorial

Cada servicio **revalida el JWT** aunque el Gateway ya autentique (defensa en profundidad).

- **Firma:** HS256 con `kid` en el encabezado. Se admite **rotación** con clave *activa* +
  *previa*: el `kid` del token selecciona la clave. Un `kid` desconocido ⇒ 401.
- **Claims validados:** `sub`, `iss` (= `smid-auth`), `aud` (debe **contener** `smid-servicios`),
  `jti`, `roles[]`, `idSede`, `idUnidad`, `alcance` (`UNIDAD`|`SEDE`|`NACIONAL`), `nombre`,
  `iat`, `exp`. Cualquier fallo ⇒ 401 (`AUTZ-003`).

**Acceso territorial registro a registro** (la diferencia esencial con el Catálogo, que es dato
compartido):

| Alcance | Visibilidad |
|---|---|
| `NACIONAL` | Todos los registros |
| `SEDE` | Registros cuya `id_sede` coincide con la del token |
| `UNIDAD` | Registros cuya `id_unidad` coincide con la del token |

Un registro **fuera de alcance se trata como inexistente (404)**, no como prohibido (403), para
no revelar la existencia de registros de otras jurisdicciones.

---

## 7. Contrato de la API

> Rutas internas (tras el `StripPrefix` del Gateway). Desde el exterior, anteponer `/api`.
> Todos los endpoints requieren `Authorization: Bearer <token>`.

### 7.0 Documentación OpenAPI / Swagger

La especificación OpenAPI queda disponible en:

```text
GET /v3/api-docs
```

La interfaz Swagger UI queda disponible en:

```text
GET /swagger-ui/index.html
```

Swagger muestra las rutas internas del servicio (`/personas/**`). El frontend y
los consumidores externos deben anteponer `/api` porque el Gateway publica el
servicio bajo `/api/personas/**` y aplica `StripPrefix=1`. Por ejemplo:

| Swagger interno | Consumo vía Gateway |
|---|---|
| `GET /personas?rut=12345678-5` | `GET /api/personas?rut=12345678-5` |
| `GET /personas/{altKey}` | `GET /api/personas/{altKey}` |
| `POST /personas` | `POST /api/personas` |
| `PUT /personas/{altKey}` | `PUT /api/personas/{altKey}` |
| `POST /personas/buscar-duplicados` | `POST /api/personas/buscar-duplicados` |

La documentación declara seguridad Bearer JWT para probar los endpoints desde
Swagger, pero no cambia la validación real: el servicio sigue revalidando el JWT
en cada petición.

### 7.1 Prevalidación de duplicados — `POST /personas/buscar-duplicados`

Operación de sólo lectura, **cross-territorial por diseño** (la deduplicación debe ver todas las
sedes). Sólo **informa**; nunca fusiona.

Petición:
```json
{
  "tipo": "ADULTO",
  "rut": "12.345.678-5",
  "nombres": "Juan",
  "apellidoPaterno": "Pérez",
  "apellidoMaterno": "Soto",
  "fechaNacimiento": "1990-05-20"
}
```

Respuesta `200`:
```json
{
  "coincidenciaExacta": { "altKey": "f1c2...", "motivo": "RUT" },
  "coincidenciasProbables": [
    { "altKey": "a9b8...", "nombre": "Juan Pérez", "fechaNacimiento": "1990-05-20", "score": 0.97 }
  ]
}
```
`coincidenciaExacta` es `null` si no hay match por RUT. Las probables vienen ordenadas por
`score` descendente (umbral 0.78, máximo 10). Todos los campos del criterio son opcionales salvo
`tipo`.

### 7.2 Búsqueda — `GET /personas?rut=...` | `GET /personas?q=...`

Búsqueda **territorial**. Debe informarse **exactamente uno** de los parámetros:

- `rut`: búsqueda exacta; devuelve una página de a lo sumo un elemento. Un RUT **malformado**
  produce `422` (`PER-002`), *fail fast* (ver §13).
- `q`: búsqueda parcial por nombre (insensible a tildes/mayúsculas), **paginada**
  (`pagina`, `tamano`; `tamano` máx. 100, por defecto 20).

Sin ninguno de los dos ⇒ `400` (`PER-001`).

Respuesta `200`:
```json
{
  "contenido": [
    { "altKey": "a9b8...", "tipo": "ADULTO", "nombre": "Juan Pérez", "rut": "12345678-5", "fechaNacimiento": "1990-05-20" }
  ],
  "pagina": 0,
  "tamano": 20,
  "total": 1
}
```

### 7.3 Detalle — `GET /personas/{altKey}`

Territorial. Fuera de alcance ⇒ `404` (`PER-404`). Respuesta `200`: objeto persona completo
(ver §7.5).

### 7.4 Alta — `POST /personas`

La **sede**, la **unidad** y la **autoría** se estampan desde el token (no se aceptan del
cliente). El único campo siempre obligatorio es `tipo`; el RUT es opcional (un NNA puede
registrarse sólo con su nombre).

Petición:
```json
{
  "tipo": "NNA",
  "nombres": "Camila",
  "apellidoPaterno": "Reyes",
  "fechaNacimiento": "2015-03-10",
  "contactos": [ { "tipo": "TELEFONO", "valor": "+56 9 1234 5678" } ]
}
```

Respuesta `201` con cabecera `Location: /personas/{altKey}` y el cuerpo de la persona creada.
RUT duplicado ⇒ `409` (`PER-003`). RUT inválido ⇒ `422` (`PER-002`). Emite `persona.creada`.

### 7.5 Actualización parcial — `PUT /personas/{altKey}`

Territorial. *Partial-merge*: todo campo nulo significa **"no tocar"**. Convenciones:

- El `tipo` **no se modifica** tras el alta.
- `rut`: nulo = mantener; `""` (cadena en blanco) = **limpiar**; otro valor = revalidar.
- `contactos`: nulo = mantener los actuales; lista (incluida vacía) = **reemplazar** el conjunto.

Respuesta `200` con la persona actualizada. Emite `persona.actualizada`.

Objeto persona (respuesta de detalle/alta/edición):
```json
{
  "altKey": "a9b8...",
  "tipo": "NNA",
  "rut": null,
  "dv": null,
  "nombres": "Camila",
  "apellidoPaterno": "Reyes",
  "apellidoMaterno": null,
  "razonSocial": null,
  "nombreLegible": "Camila Reyes",
  "fechaNacimiento": "2015-03-10",
  "sexo": null,
  "nacionalidad": null,
  "idSede": "sede-a",
  "idUnidad": "unidad-1",
  "vigente": true,
  "creadoEn": "2026-01-15T12:00:00Z",
  "actualizadoEn": "2026-01-15T12:00:00Z",
  "contactos": [ { "tipo": "TELEFONO", "valor": "+56 9 1234 5678" } ]
}
```

---

## 8. Códigos de error

Todos los errores usan el **sobre unificado** del ecosistema (§2.5 del Núcleo):

```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "codigo": "PER-002",
  "mensaje": "El RUT '12345678-9' no supera la validación de dígito verificador (módulo 11).",
  "detalles": { "campo": "mensaje" },
  "ruta": "/personas",
  "timestamp": "2026-01-15T12:00:00Z"
}
```

> El campo de ruta se llama **`ruta`** (no `path`). El campo `detalles` (mapa campo→mensaje) sólo
> aparece en errores de validación (`PER-001`) y se omite del JSON en los demás casos.
> `timestamp` es un instante UTC ISO-8601.

| Código | HTTP | Situación |
|---|---|---|
| `PER-001` | 400 | Solicitud inválida (validación de DTO o falta de criterio de búsqueda) |
| `PER-002` | 422 | RUT presente que no supera módulo 11 |
| `PER-003` | 409 | RUT duplicado (ya existe una persona vigente con ese RUT) |
| `PER-404` | 404 | Persona inexistente **o fuera de alcance territorial** |
| `PER-500` | 500 | Error interno no esperado |
| `AUTZ-003` | 401 | No autenticado / token ausente, mal firmado, expirado o inválido |
| `AUTZ-004` | 403 | Autenticado pero sin permiso por rol |

> **Sobre `AUTZ-004` (403):** está **cableado pero no es alcanzable** con los endpoints actuales.
> Ningún endpoint exige un rol específico y la denegación territorial se expresa como 404. El
> manejador 403 se incluye para mantener idéntico el contrato del ecosistema y para soportar, sin
> cambios, una futura autorización por rol. Por eso las pruebas ejercitan **401 y 404**, no 403.

---

## 9. Eventos de dominio

El servicio publica eventos hacia la futura **Auditoría** (§2.8 del Núcleo) a través del puerto
`EventoPublicador`. El transporte se elige por configuración (`smid.eventos.transporte`):

- **`log`** (por defecto): registra el evento como JSON en el canal de auditoría
  `AUDIT.SMID.PERSONAS`. No requiere RabbitMQ.
- **`rabbitmq`**: publica en el *exchange* tipo *topic* `smid.eventos`, usando el nombre del
  evento como *routing key*.

Eventos emitidos: `persona.creada`, `persona.actualizada`.

> **Sólo metadatos no sensibles (G7).** El cuerpo del evento jamás contiene nombres, RUT ni
> contactos: a lo sumo el `tipo` de persona y una bandera `conRut`. El recurso se identifica por
> su `altKey`. La publicación es **tolerante a fallos**: un problema de mensajería no aborta la
> operación de negocio ya confirmada. La salud de RabbitMQ no se incluye en el *health check*
> para no marcar el servicio como caído cuando el transporte es `log`.

---

## 10. Variables de entorno

Ver `.env.example`. Resumen:

| Variable | Descripción |
|---|---|
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | Conexión MySQL 8 |
| `JWT_ISSUER`, `JWT_AUDIENCE` | Emisor y audiencia esperados |
| `JWT_KID`, `JWT_SECRET` | Clave de firma **activa** (secreto ≥ 32 bytes para HS256) |
| `JWT_KID_PREVIO`, `JWT_SECRET_PREVIO` | Clave **previa** opcional (rotación) |
| `EVENTOS_TRANSPORTE` | `log` (def.) o `rabbitmq` |
| `RABBITMQ_HOST/PORT/USER/PASSWORD` | Sólo si `EVENTOS_TRANSPORTE=rabbitmq` |

Los secretos provienen **exclusivamente** de variables de entorno; no hay secretos en el código
ni en los `application*.yml`.

---

## 11. Ejecución

### 11.1 Requisitos

- JDK 21
- Maven 3.9+
- MySQL 8 en marcha con una base `db_personas` creada (vacía; el esquema lo crea Flyway)
- (Opcional) RabbitMQ si se usa `EVENTOS_TRANSPORTE=rabbitmq`

### 11.2 Local (perfil `local`, con trazas)

```bash
# Cargar variables de entorno
cp .env.example .env && export $(grep -v '^#' .env | xargs)

# Compilar y ejecutar
mvn clean package
mvn spring-boot:run -Dspring-boot.run.profiles=local
# o bien:
java -jar target/personas-service-1.0.0.jar --spring.profiles.active=local
```

El servicio queda en `http://localhost:8088`. Comprobación de salud:
`GET http://localhost:8088/actuator/health`.

### 11.3 Producción

Sin el perfil `local` (sin trazas verbosas). Proveer las variables de entorno por el mecanismo
del orquestador. El servicio se publica detrás del Gateway en `/api/personas/**`.

---

## 12. Pruebas

```bash
mvn test
```

**Dos niveles, con un matiz importante sobre Docker:**

- **Pruebas unitarias de dominio** (`src/test/.../dominio`): ejercitan el núcleo puro con dobles
  en memoria (repositorio, reloj, generador de `alt_key`, publicador de eventos). **No requieren
  Docker** y cubren los criterios de aceptación del Núcleo: alta parcial de un NNA sin RUT,
  unicidad de RUT, validación de RUT por módulo 11 (incluido el dígito `K`), filtro territorial
  (404) y emisión de eventos con metadatos no sensibles.

- **Prueba de integración** (`src/test/.../integracion/PersonaIntegracionTest`): de extremo a
  extremo con **Testcontainers (MySQL 8)** sobre una base **vacía**. Verifica 401 sin token, 401
  con token mal firmado, alta `201`, prevalidación de duplicados y acceso territorial `404`.
  **Requiere un demonio Docker** en la máquina que ejecuta las pruebas; en un entorno sin Docker,
  esta clase no puede ejecutarse (las unitarias sí).

> **Nota de transparencia sobre la construcción.** Este proyecto se entrega como código fuente
> listo para compilar. La compilación con Maven y la resolución de dependencias (incluido
> Flyway/Hibernate validando el esquema) se realizan **en el entorno local con acceso a la red**
> de artefactos; la prueba de integración, además, **necesita Docker**. El empaquetado entregado
> no incluye binarios compilados.

---

## 13. Decisiones de diseño y discrepancias resueltas

Durante la implementación se resolvieron las siguientes discrepancias entre los documentos
funcionales y el contrato técnico congelado del ecosistema:

1. **Migración del padrón revertida.** Arranque con BD vacía; el padrón SIGER se consultará vía
   un futuro `siger-service`. (Ver §4.)
2. **Campo `ruta` en el sobre de error** (no `path`), por coherencia con el español del Núcleo.
3. **`id_sede` / `id_unidad` como `CHAR(36)`**: el token sólo transporta `alt_key` (UUID), no
   claves internas. Se **añade `id_unidad`** (no estaba en §5.3) para poder filtrar por alcance
   `UNIDAD`.
4. **ENUMs como `VARCHAR(20)` + `CHECK`** (en vez de tipos `ENUM` nativos), y **timestamps como
   `DATETIME(6)` en UTC**, para portabilidad y precisión.
5. **Se añade `actualizado_en`**, ya que existe el endpoint `PUT`.
6. **Versión del artefacto: `1.0.0`.**

Otras decisiones relevantes:

- **`GET /personas?rut=` con RUT malformado responde 422 (`PER-002`)**, *fail fast*, en lugar de
  devolver una página vacía: un RUT estructuralmente inválido es un error del cliente en
  cualquier endpoint, y conviene señalarlo de inmediato.
- **Denegación territorial = 404, no 403**, para no revelar registros fuera de alcance (§6).
- **Deduplicación cross-territorial pero acotada**: *blocking* por prefijo de apellido o fecha de
  nacimiento, *scoring* combinando Jaro-Winkler del nombre (peso 0.82) con coincidencia de fecha
  (peso 0.18), umbral 0.78. El puerto `BuscadorDuplicados` deja la costura para sumar
  `siger-service` en el futuro sin tocar el dominio.
- **Validación de RUT sólo si está presente** (el RUT es nulable a nivel de persona).
```
