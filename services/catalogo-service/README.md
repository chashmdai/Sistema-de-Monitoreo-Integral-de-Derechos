# catalogo-service · SMID 6.7 — Catálogo de Derechos

Servicio del ecosistema **SMID** (Defensoría de los Derechos de la Niñez) que administra la
**taxonomía jerárquica de derechos** vulnerados y sus **causas** asociadas. Es un servicio de
**dato de referencia**: lectura intensiva, escritura administrativa y **sin datos personales**.

- **Stack:** Java 21 · Spring Boot 3.5.15 · MySQL 8 (InnoDB, utf8mb4) · Flyway · jjwt 0.12.5 · SpringDoc OpenAPI 2.8.x
- **Puerto:** `8087`
- **Base de paquetes:** `cl.smid.catalogo`
- **Tras el Gateway:** se expone en `/api/catalogo/**`; el Gateway aplica `StripPrefix` de `/api`,
  por lo que internamente los controladores cuelgan de **`/catalogo/...`**.

---

## Arquitectura (hexagonal)

El código sigue una arquitectura de puertos y adaptadores, espejando la del servicio de
autenticación del ecosistema:

```
cl.smid.catalogo
├── api                 → controladores REST, DTOs (records) y traducción de errores
├── dominio             → NÚCLEO PURO (sin framework): modelo, puertos y servicio
│   ├── modelo          → Derecho, Causa, NodoArbol
│   ├── puerto/entrada  → casos de uso y comandos
│   ├── puerto/salida   → contratos de repositorios, reloj, eventos, generador de alt_key
│   ├── servicio        → ServicioCatalogo (lógica), EnsambladorArbol
│   └── excepcion       → CodigoError, CatalogoException
├── infraestructura
│   ├── persistencia    → entidades JPA, repositorios Spring Data y adaptadores
│   ├── seguridad       → validación de JWT y contexto de sesión
│   ├── mensajeria      → publicador de eventos (adaptador de log)
│   └── comun           → reloj de sistema, generador de UUID
└── config              → raíz de composición (beans, seguridad, propiedades)
```

**Principio rector:** el dominio no conoce Spring ni JPA. Spring "cablea" los componentes puros
como *beans* en `config/DominioConfig`. La demarcación transaccional vive en el controlador, lo
que mantiene el servicio de dominio como un POJO verificable en aislamiento.

### Identificadores opacos (cierre de IDOR)

El único identificador público es el **`alt_key` (UUID)**. Las PK/FK internas (`BIGINT`) jamás
cruzan la frontera de la API: `api/MapeadorRespuesta` es esa frontera y las elimina de toda
respuesta (en la vista plana, la relación con el padre se expresa con el `alt_key` del padre).

---

## Endpoints

Ruta base interna: `/catalogo`. Todas las lecturas requieren **autenticación**; todas las
escrituras requieren **rol de Administración**.

Swagger/OpenAPI queda disponible directamente en el servicio:

```text
GET /v3/api-docs
GET /swagger-ui/index.html
```

La documentación muestra las rutas internas (`/catalogo/**`). El frontend consume
vía Gateway anteponiendo `/api`, por ejemplo `/api/catalogo/derechos` o
`/api/catalogo/derechos/{altKey}/causas`.

| Método | Ruta                                   | Acceso         | Éxito | Descripción                                   |
|--------|----------------------------------------|----------------|-------|-----------------------------------------------|
| GET    | `/catalogo/derechos`                   | Autenticado    | 200   | Árbol de derechos vigentes (anidado)          |
| GET    | `/catalogo/derechos?formato=plano`     | Autenticado    | 200   | Lista plana (cada nodo con `idPadreAltKey`)   |
| GET    | `/catalogo/derechos/{altKey}`          | Autenticado    | 200   | Detalle de un derecho con sus hijos directos  |
| GET    | `/catalogo/derechos/{altKey}/causas`   | Autenticado    | 200   | Causas vigentes del derecho                   |
| GET    | `/catalogo/derechos/buscar?q=texto`    | Autenticado    | 200   | Búsqueda por nombre o código                  |
| POST   | `/catalogo/derechos`                   | Administración | 201   | Crea un derecho (raíz o hijo)                 |
| PUT    | `/catalogo/derechos/{altKey}`          | Administración | 200   | Actualiza un derecho                          |
| DELETE | `/catalogo/derechos/{altKey}`          | Administración | 204   | Baja lógica en cascada (idempotente)          |
| POST   | `/catalogo/derechos/{altKey}/causas`   | Administración | 201   | Crea una causa para el derecho                |

### Payloads (JSON)

**GET `/catalogo/derechos`** — árbol anidado:

```json
[
  {
    "altKey": "11111111-1111-4111-8111-000000000003",
    "codigo": "EDU",
    "nombre": "Derecho a la educación",
    "nivel": 0,
    "hijos": [
      {
        "altKey": "11111111-1111-4111-8111-000000000012",
        "codigo": "EDU.ACCESO",
        "nombre": "Acceso y permanencia",
        "nivel": 1,
        "hijos": []
      }
    ]
  }
]
```

**GET `/catalogo/derechos?formato=plano`**:

```json
[
  { "altKey": "…-000000000003", "codigo": "EDU", "nombre": "Derecho a la educación", "nivel": 0, "idPadreAltKey": null },
  { "altKey": "…-000000000012", "codigo": "EDU.ACCESO", "nombre": "Acceso y permanencia", "nivel": 1, "idPadreAltKey": "…-000000000003" }
]
```

**GET `/catalogo/derechos/{altKey}`** — detalle:

```json
{
  "altKey": "11111111-1111-4111-8111-000000000003",
  "codigo": "EDU",
  "nombre": "Derecho a la educación",
  "nivel": 0,
  "descripcion": "Acceso, permanencia y calidad en la educación.",
  "vigente": true,
  "hijos": [ { "altKey": "…", "codigo": "EDU.ACCESO", "nombre": "Acceso y permanencia", "nivel": 1, "hijos": [] } ]
}
```

**GET `/catalogo/derechos/{altKey}/causas`**:

```json
[
  { "altKey": "22222222-2222-4222-8222-000000000001", "codigo": "DESERCION", "nombre": "Deserción escolar", "vigente": true }
]
```

**POST `/catalogo/derechos`** — solicitud (un `idPadreAltKey` nulo/ausente crea una raíz):

```json
{ "idPadreAltKey": "…-000000000003", "codigo": "EDU.INCLUSION", "nombre": "Inclusión educativa", "descripcion": "…", "orden": 3 }
```

Responde **201** con el detalle del derecho creado y la cabecera `Location`.

**PUT `/catalogo/derechos/{altKey}`** — solicitud (el `codigo` es opcional; si se envía debe
coincidir con el actual o se rechaza con `CAT-006`; `idPadreAltKey` reubica el derecho):

```json
{ "codigo": "EDU.INCLUSION", "nombre": "Inclusión y diversidad", "descripcion": "…", "orden": 3, "idPadreAltKey": "…-000000000003" }
```

**POST `/catalogo/derechos/{altKey}/causas`** — solicitud:

```json
{ "codigo": "BARRERA_ACCESIBILIDAD", "nombre": "Barreras de accesibilidad" }
```

---

## Sobre de error (unificado)

Todas las respuestas de error comparten esta estructura (Núcleo 2.5):

```json
{
  "status": 404,
  "error": "Not Found",
  "codigo": "CAT-001",
  "mensaje": "El derecho solicitado no existe.",
  "detalles": { "campo": "mensaje" },
  "ruta": "/catalogo/derechos/abc",
  "timestamp": "2026-06-13T12:34:56.789Z"
}
```

- `error` es la frase de estado HTTP; `detalles` solo aparece en errores de validación.
- `timestamp` es un instante UTC en ISO-8601.

### Códigos de error

| Código     | HTTP | Significado                                              |
|------------|------|---------------------------------------------------------|
| `AUTZ-003` | 401  | No autenticado (token ausente, inválido o expirado)     |
| `AUTZ-004` | 403  | Acceso denegado (autenticado, sin rol de Administración) |
| `CAT-001`  | 404  | Derecho no encontrado                                   |
| `CAT-002`  | 409  | Código de derecho duplicado                             |
| `CAT-003`  | 409  | Código de causa duplicado (dentro del derecho)          |
| `CAT-004`  | 422  | Árbol inválido (padre inexistente, ciclo, padre de baja) |
| `CAT-005`  | 400  | Error de validación (incluye `detalles`)                |
| `CAT-006`  | 409  | Código inmutable (se intentó cambiar el código)         |
| `CAT-500`  | 500  | Error interno                                           |

---

## Seguridad (validación de JWT, defensa en profundidad)

Cada servicio **revalida** el JWT por su cuenta (DT-3), aunque el Gateway ya lo haya hecho. Se
verifica, según el contrato 2.4:

1. **Firma HS256**, seleccionando la clave por el `kid` de la cabecera.
2. **Emisor** (`iss` = `smid-auth`).
3. **Audiencia** (`aud` contiene `smid-servicios`).
4. **Expiración** (`exp`).

El secreto se interpreta como **bytes UTF-8** para HS256 (mínimo 32 caracteres) y debe coincidir,
junto con su `kid`, con el del servicio de autenticación.

**Rotación de claves:** se admiten dos claves simultáneas, la **activa** y una **previa**
opcional, para no invalidar tokens en vuelo durante una rotación. El validador elige según el
`kid` del token.

---

## Variables de entorno

Ver `.env.example`. Resumen:

| Variable                                | Obligatoria | Descripción                                          |
|-----------------------------------------|-------------|------------------------------------------------------|
| `DB_URL`                                | sí          | URL JDBC de MySQL (incluir `connectionTimeZone=UTC`) |
| `DB_USER`, `DB_PASSWORD`                | sí          | Credenciales de la base                              |
| `JWT_KID`                               | sí          | `kid` de la clave activa                             |
| `JWT_SECRET`                            | sí          | Secreto HS256 (≥ 32 caracteres)                      |
| `JWT_ISSUER`                            | no          | Emisor esperado (por defecto `smid-auth`)            |
| `JWT_AUDIENCE`                          | no          | Audiencia esperada (por defecto `smid-servicios`)    |
| `JWT_KID_PREVIO`, `JWT_SECRET_PREVIO`   | no          | Clave previa (solo durante una rotación)             |
| `SMID_SEGURIDAD_ROLES_ADMINISTRACION`   | no          | Roles de escritura (lista separada por comas)        |
| `SPRING_PROFILES_ACTIVE`                | no          | `local` activa trazas SQL y logs de depuración       |

---

## Ejecución

### Requisitos
- JDK 21
- Maven 3.9+
- MySQL 8 en marcha y base `db_catalogo` creada (Flyway crea las **tablas**, no la base):
  ```sql
  CREATE DATABASE db_catalogo CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
  ```

### Local
```bash
cp .env.example .env          # completar credenciales y secreto
set -a && source .env && set +a
mvn spring-boot:run
```
Al arrancar, **Flyway** aplica `V1` (esquema) y `V2` (semilla). Hibernate solo **valida** el
esquema (nunca lo modifica).

### Empaquetado
```bash
mvn clean package
java -jar target/catalogo-service-0.0.1-SNAPSHOT.jar
```

### Salud
```bash
curl http://localhost:8087/actuator/health
```

---

## Semilla de datos (Flyway `V2`)

`V2__semilla_derechos.sql` carga una taxonomía base inspirada en la Convención sobre los Derechos
del Niño (11 derechos raíz: vida, salud, educación, familia, protección, identidad, participación,
justicia, recreación, nivel de vida, no discriminación), algunos hijos de ejemplo (educación y
protección) y causas de muestra. Se insertan con `id` y `alt_key` **deterministas** y, al final, se
reposiciona el `AUTO_INCREMENT` en `1000` para que los registros creados por la aplicación queden
claramente separados de la semilla.

---

## Pruebas

```bash
mvn test
```

- **Dominio** (`ServicioCatalogoTest`, `EnsambladorArbolTest`): puras, en memoria, sin Docker.
  Cubren invariantes (código inmutable, sin ciclos), baja en cascada idempotente, recálculo de
  nivel al reubicar, y búsqueda acento-insensible.
- **Integración** (`CatalogoIntegracionTest`): usa **Testcontainers** con **MySQL real**, por lo
  que **requiere Docker**. Verifica Flyway + esquema + semilla, la cadena de seguridad (401/403),
  el árbol anidado y el ciclo de vida (crear → consultar → baja).

---

## Notas de diseño

- **Árbol como lista de adyacencia** (`id_padre`): el recorrido se resuelve con `WITH RECURSIVE`
  de MySQL 8 y el anidado se arma en memoria con un ensamblador O(n), en orden estable
  (`nivel, orden, codigo`).
- **Borrado lógico:** nunca se borra físicamente. La baja de un derecho marca `vigente=0` y
  `vigente_hasta`, y **cae en cascada** sobre los derechos descendientes (no sobre causas),
  registrando un evento con la cantidad de descendientes afectados.
- **Búsqueda acento- e insensible a mayúsculas:** se apoya en la colación
  `utf8mb4_0900_ai_ci`, sin funciones adicionales (p. ej. `educacion` encuentra `educación`).
- **Inmutabilidad del código:** el `nombre` es editable; el `codigo`, no. El `PUT` acepta el
  `codigo` opcionalmente y lo rechaza si difiere (`CAT-006`).
- **Reubicación:** el `PUT` admite mover un derecho bajo otro padre existente, verificando que no
  se forme un ciclo y recalculando el nivel de todo el subárbol. Mover a raíz no se admite en v1.
- **Eventos de dominio:** se publican vía un puerto `EventoPublicador`. El adaptador por defecto
  los escribe como JSON al canal de auditoría `AUDIT.SMID.CATALOGO` (Núcleo 2.8); migrar a
  RabbitMQ es cambiar una sola clase adaptadora.
- **Detalles de mapeo JPA en modo `validate`:** `alt_key` se mapea como `CHAR(36)`
  (`@JdbcTypeCode(SqlTypes.CHAR)`); `vigente` como `boolean` (Connector/J reporta `TINYINT(1)`
  como `BIT` con `tinyInt1isBit=true`); `nivel`/`orden` como `short`. La columna `creado_en` la
  gestiona la base y no se mapea.

### Discrepancias resueltas (criterio aplicado)

Donde los documentos del proyecto diferían, se priorizó el **Núcleo Fundacional** (autoritativo)
y la coherencia con el servicio de autenticación ya congelado:

1. **`ruta` vs `path` en el sobre de error:** se usa `ruta` y `error` = frase de estado HTTP, para
   alinear con el sobre del servicio de autenticación.
2. **Esquema:** se adoptó el del Núcleo §4.2 (con `descripcion`, `orden`, `vigente_desde/hasta`),
   más completo que el del README condensado.
3. **Configuración de claves JWT:** en lugar de un mapa con `kid` dinámico (no resoluble como
   clave en YAML), se modela como par **activa/previa**, igual de apto para rotación y directo de
   configurar por variables de entorno.
4. **Mensajería:** sin dependencia dura de RabbitMQ (el contrato no define el detalle de eventos
   para este servicio); se deja la costura mediante el puerto y un adaptador de log.
```
