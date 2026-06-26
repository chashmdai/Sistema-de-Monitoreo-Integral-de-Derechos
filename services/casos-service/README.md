# casos-service · SMID 6.4 — Casos

Servicio del **Núcleo Fundacional SMID** (Defensoría de los Derechos de la Niñez). Es el
**agregado raíz del expediente**: en la arquitectura objetivo, _cuando un requerimiento se asigna a un
profesional, nace un Caso_. Es el **primer consumidor de eventos asíncronos** del clúster: materializa
un `Caso` por cada evento `requerimiento.asignado` que publica `requerimientos-service` (6.3).

> **Sin especificación formal.** Casos (6.4) está fuera del alcance del Núcleo (§1.3). Este servicio se
> diseñó de forma coherente con el evento de entrada, los principios transversales del capítulo 2 y las
> costuras dejadas por `requerimientos`. Cada supuesto se documenta aquí y en la cabecera de
> `V1__inicial.sql`.

---

## 1. Stack

- Java 21 · Spring Boot 3.5.15 · Maven (del sistema, **sin** wrapper)
- MySQL 8 (InnoDB, utf8mb4) · Flyway (`ddl-auto=validate`, nunca `update`)
- RabbitMQ (consumo + publicación, transporte conmutable)
- jjwt 0.12.5 (HS256 con `kid`) · Testcontainers (pruebas de integración)
- SpringDoc OpenAPI 2.8.x (Swagger UI)

Coordenadas: `artifactId=casos-service`, paquete base `cl.smid.casos`, versión `1.0.0`, BD `db_casos`,
puerto `8090`. Tras el Gateway, `/api/casos/**` llega como `/casos/**` (StripPrefix=1).

---

## 2. Arquitectura (hexagonal estricta)

```
api              → controladores y DTOs (solo identificadores opacos)
dominio
  ├─ modelo      → entidades de dominio y objetos de valor (POJO puro)
  ├─ servicio    → lógica de negocio (máquina de estados, alcance, ServicioCasos)
  ├─ puerto      → entrada (casos de uso) y salida (repositorios, mensajería, clientes)
  └─ excepcion   → excepciones de negocio + catálogo de códigos
infraestructura
  ├─ persistencia→ JPA, mapeo explícito, correlativo atómico (JDBC)
  ├─ seguridad   → validación JWT, filtro, contexto
  ├─ eventos     → listener idempotente, topología Rabbit, publicador
  ├─ cliente     → enriquecimiento on-demand (REST opcional)
  └─ soporte     → reloj, generador de UUID, directorio de sedes
config           → propiedades y cableado del dominio
```

Principios aplicados: el **dominio es POJO puro** (sin Spring/JPA/Lombok); la **PK numérica interna
nunca cruza la frontera** (se expone solo `alt_key` UUID); enums como `VARCHAR + CHECK`; marcas de
tiempo `DATETIME(6)` en UTC; **`@Transactional` se demarca en el controlador y en el listener**, no en
el dominio.

---

## 3. Máquina de estados del expediente

```
ABIERTO ──INICIAR_INVESTIGACION──▶ EN_INVESTIGACION
ABIERTO ──CERRAR──────────────────▶ CERRADO
EN_INVESTIGACION ──DERIVAR_A_SEGUIMIENTO─▶ EN_SEGUIMIENTO
EN_INVESTIGACION ──SUSPENDER─────────────▶ SUSPENDIDO
EN_INVESTIGACION ──CERRAR─────────────────▶ CERRADO
EN_SEGUIMIENTO ──REANUDAR──────────────▶ EN_INVESTIGACION
EN_SEGUIMIENTO ──SUSPENDER──────────────▶ SUSPENDIDO
EN_SEGUIMIENTO ──CERRAR──────────────────▶ CERRADO
SUSPENDIDO ──REANUDAR──────────────────▶ EN_INVESTIGACION
SUSPENDIDO ──CERRAR──────────────────────▶ CERRADO
CERRADO ──REABRIR──────────────────────▶ EN_INVESTIGACION
CERRADO ──ARCHIVAR──────────────────────▶ ARCHIVADO   (terminal)
```

La máquina es una **tabla pura** `Map<(estado, acción) → estado>`. Una transición no contemplada
responde **CAS-409**. Las acciones **administrativas** (`CERRAR`, `REABRIR`, `ARCHIVAR`) exigen rol de
**Coordinación** (si falta, **AUTZ-004 / 403**); las operativas solo requieren alcance territorial.

El asiento de **apertura** se registra con la pseudo-acción `MATERIALIZACION` y `estado_origen = NULL`,
atribuido al actor de sistema `00000000-0000-0000-0000-000000000000` (el listener no porta usuario).

---

## 4. Número de expediente

Formato: `EXP-{CODIGO_SEDE}-{[B]CORRELATIVO}/{AÑO}`.

- Oficial: `EXP-RM-1/2027`
- Beta (marcha blanca): `EXP-RM-B1/2027`

El correlativo es **atómico y único por `(sede, año, serie)`**, reservado con un UPSERT de MySQL
(`INSERT ... ON DUPLICATE KEY UPDATE ... LAST_INSERT_ID(...)`) sobre una sola conexión; corre dentro de
la transacción del listener, de modo que un rollback no deja huecos. La **serie BETA está aislada** de
la OFICIAL en filas distintas. La serie del caso se hereda del evento (`esBeta`); si el evento no la
trae, se decide por fecha (`smid.expediente.inicio-oficial`, por defecto `2027-01-01`).

---

## 5. Seguridad y territorialidad

Defensa en profundidad (DT-3): **cada servicio revalida el JWT**. Firma HS256 con `kid` (clave activa y,
opcionalmente, previa para rotación), `iss = smid-auth`, y `aud` debe **contener** `smid-servicios`;
`exp` validado. Claims usados: `sub`, `roles[]`, `idSede`, `idUnidad`, `alcance`
(`UNIDAD|SEDE|NACIONAL`), `nombre`.

Acceso territorial **registro a registro**: NACIONAL ve todo; SEDE filtra por sede; UNIDAD por unidad.
Un recurso fuera de alcance responde **404** (no 403), para no revelar su existencia.

---

## 6. Contrato del listener (consumo de eventos)

| Elemento        | Valor                                            |
| --------------- | ------------------------------------------------ |
| Exchange        | `smid.eventos` (topic, durable)                  |
| Clave de enlace | `requerimiento.asignado`                         |
| Cola            | `casos.requerimiento-asignado` (durable)         |
| DLX / DLQ       | `casos.dlx` / `casos.requerimiento-asignado.dlq` |
| Concurrencia    | 1 consumidor (reentrega secuencial)              |
| Reintentos      | 3 intentos (1s, x2, máx 10s); al agotarse → DLQ  |

**Payload consumido** (deserialización tolerante; se ignoran campos desconocidos):

```json
{
  "tipo": "requerimiento.asignado",
  "altKey": "<alt_key del requerimiento de origen>",
  "ocurridoEn": "2027-02-01T10:00:00Z",
  "metadatos": {
    "folio": "F-100",
    "estado": "ASIGNADO",
    "idSede": "<alt_key sede>",
    "idUnidadDestino": "<alt_key unidad>",
    "complejidad": "MEDIANA",
    "requiereFichaReservada": false,
    "esBeta": false,
    "accion": "ASIGNAR",
    "idProfesionalAsignadoAlt": "<alt_key profesional>"
  }
}
```

**Idempotencia (entrega at-least-once).** La unicidad de `id_requerimiento_origen_alt` garantiza un
único caso por origen. Flujo:

1. _Reentrega secuencial_: el pre-chequeo encuentra el caso y la operación es **no-op** (ack limpio,
   sin emitir `caso.abierto` de nuevo).
2. _Carrera entre instancias_: la restricción única dispara un conflicto; el reintento del contenedor
   reejecuta y, en el segundo intento, el pre-chequeo lo resuelve como no-op.
3. _Evento malformado/ inválido_: se rechaza sin reencolar y el broker lo deriva a la **DLQ**.

**Materialización "esqueleto".** El listener no tiene token de usuario; el caso se crea solo con las
referencias `alt_key` del evento. El **enriquecimiento se difiere** a la consulta on-demand, que usa el
token del usuario (respetando territorial y G7).

---

## 7. Eventos emitidos

Publicados en `smid.eventos` con clave igual al tipo. Transporte conmutable (`log` / `rabbitmq`).
Carga útil **solo con metadatos no sensibles** (G7).

| Evento                 | Cuándo                      |
| ---------------------- | --------------------------- |
| `caso.abierto`         | al materializar el caso     |
| `caso.cerrado`         | al transicionar a CERRADO   |
| `caso.archivado`       | al transicionar a ARCHIVADO |
| `caso.estado_cambiado` | resto de transiciones       |

Sobre emitido:

```json
{
  "tipo": "caso.abierto",
  "altKey": "<alt_key del caso>",
  "ocurridoEn": "2027-02-01T10:00:00Z",
  "metadatos": {
    "numeroExpediente": "EXP-RM-1/2027",
    "estado": "ABIERTO",
    "idSede": "<alt_key sede>",
    "idUnidad": "<alt_key unidad>",
    "complejidad": "MEDIANA",
    "requiereFichaReservada": false,
    "esBeta": false
  }
}
```

---

## 8. API REST

No hay endpoint de creación: **los casos nacen de eventos**. Todas las rutas exigen autenticación.
Swagger documenta las rutas internas del servicio (`/casos/**`); el frontend consume siempre vía
Gateway anteponiendo `/api`, por ejemplo `/api/casos`.

Documentación local:

- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

### 8.1 Detalle de un caso

`GET /casos/{altKey}`

```json
{
  "altKey": "…",
  "numeroExpediente": "EXP-RM-1/2027",
  "idRequerimientoOrigen": "…",
  "folioRequerimiento": "F-100",
  "estado": "ABIERTO",
  "complejidad": "MEDIANA",
  "idSede": "…",
  "idUnidad": "…",
  "idProfesionalResponsable": "…",
  "requiereFichaReservada": false,
  "esBeta": false,
  "abiertoEn": "2027-02-01T10:00:00Z",
  "cerradoEn": null,
  "creadoEn": "2027-02-01T10:00:00Z",
  "actualizadoEn": "2027-02-01T10:00:00Z",
  "enriquecimiento": {
    "disponible": false,
    "estadoRequerimiento": null,
    "canal": null,
    "cantidadNnaAfectados": null
  },
  "historial": [
    {
      "altKey": "…",
      "estadoOrigen": null,
      "estadoDestino": "ABIERTO",
      "accion": "MATERIALIZACION",
      "observacion": "…",
      "actor": "00000000-0000-0000-0000-000000000000",
      "ocurridoEn": "2027-02-01T10:00:00Z"
    }
  ]
}
```

### 8.2 Listado paginado

`GET /casos?estado=ABIERTO&unidad={altKey}&pagina=0&tamano=20`

```json
{
  "contenido": [
    {
      "altKey": "…",
      "numeroExpediente": "EXP-RM-1/2027",
      "estado": "ABIERTO",
      "...": "…"
    }
  ],
  "pagina": 0,
  "tamano": 20,
  "total": 1
}
```

### 8.3 Transición de estado

`POST /casos/{altKey}/transiciones`

```json
{ "accion": "INICIAR_INVESTIGACION", "observacion": "Inicio de investigación" }
```

Respuesta: el detalle del caso con el estado actualizado. Acciones administrativas (`CERRAR`,
`REABRIR`, `ARCHIVAR`) requieren rol de Coordinación.

---

## 9. Sobre de error (unificado)

Campos: `status`, `error`, `codigo`, `mensaje`, `detalles` (solo validación), **`ruta`** (no "path"),
`timestamp` (UTC ISO-8601).

```json
{
  "status": 404,
  "error": "Not Found",
  "codigo": "CAS-404",
  "mensaje": "No existe un caso accesible…",
  "ruta": "/casos/abc",
  "timestamp": "2027-02-01T10:00:00Z"
}
```

| Código     | HTTP | Significado                                  |
| ---------- | ---- | -------------------------------------------- |
| `CAS-001`  | 400  | Validación o solicitud ilegible              |
| `CAS-404`  | 404  | No existe o fuera de alcance territorial     |
| `CAS-409`  | 409  | Transición inválida para el estado actual    |
| `CAS-422`  | 422  | Regla de negocio incumplida                  |
| `CAS-500`  | 500  | Error interno                                |
| `AUTZ-003` | 401  | No autenticado (token ausente/ inválido)     |
| `AUTZ-004` | 403  | Autenticado sin rol requerido (Coordinación) |

---

## 10. Variables de entorno

Ver `.env.example`. Resumen de las principales:

| Variable                                        | Descripción                                           |
| ----------------------------------------------- | ----------------------------------------------------- |
| `SERVER_PORT`                                   | Puerto (por defecto 8090)                             |
| `DB_HOST/PORT/NAME/USER/DB_PASSWORD`            | Conexión MySQL (`DB_PASSWORD` sin valor por defecto)  |
| `RABBITMQ_HOST/PORT/USER/PASSWORD/VHOST`        | Conexión RabbitMQ                                     |
| `JWT_ISSUER/AUDIENCE/KID/SECRET`                | Validación JWT (`JWT_SECRET` obligatorio)             |
| `JWT_KID_PREVIO/SECRET_PREVIO`                  | Ventana de rotación (opcional)                        |
| `ROLES_COORDINACION`                            | Roles que habilitan acciones administrativas          |
| `EXPEDIENTE_INICIO_OFICIAL`                     | Fecha de corte serie OFICIAL (por defecto 2027-01-01) |
| `CODIGO_SEDE_DEFECTO`                           | Código de sede de respaldo                            |
| `EVENTOS_CONSUMO`                               | `rabbitmq` (listener activo) o `none`                 |
| `EVENTOS_TRANSPORTE`                            | `rabbitmq` o `log`                                    |
| `ENRIQUECIMIENTO_ACTIVO` / `REQUERIMIENTOS_URL` | Cruce on-demand opcional contra 6.3                   |

---

## 11. Compilación, pruebas y ejecución

> **Requisitos de red y Docker (honestidad operativa):**
>
> - La compilación con Maven necesita **acceso a la red** para resolver dependencias la primera vez.
> - Las **pruebas unitarias** (dominio) corren **sin Docker**.
> - Las **pruebas de integración y de concurrencia** usan **Testcontainers** y requieren **Docker**;
>   si no hay Docker, se **omiten automáticamente** (no fallan).

```bash
# Compilar y ejecutar solo pruebas unitarias (sin Docker):
mvn -q test

# Empaquetar:
mvn -q clean package

# Ejecutar (perfil local: sin broker por defecto, publica por log):
SPRING_PROFILES_ACTIVE=local \
DB_PASSWORD=*** JWT_SECRET=*** \
mvn spring-boot:run
```

En despliegue real, exportar las variables de `.env` y levantar MySQL y RabbitMQ.

---

## 12. Decisiones, supuestos y costuras

**Arranque limpio — sin migración (override).** La BD arranca **vacía**: Flyway crea solo el esquema,
sin semilla ni migración histórica. El historial del legado (SIGER) permanece **congelado** y se
accederá vía un futuro `siger-service`. Los casos se materializan **exclusivamente** desde
`requerimiento.asignado` en adelante. Cualquier indicación de migrar casos/historial desde el legado se
ignora. Se deja una **costura reservada** para el cruce con el legado, **no implementada**.

**Discrepancias resueltas** (alineadas con `requerimientos`): identificadores `alt_key` (UUID) en
columnas `VARCHAR(36)`; enums `VARCHAR + CHECK`; `DATETIME(6)` en UTC; booleanos `TINYINT(1)`; sobre de
error con campo `ruta`. Configuración JWT con `kid/secreto` explícitos (activo + previo opcional), no un
mapa dinámico (los marcadores de entorno no resuelven sobre claves dinámicas de YAML).

**Costuras futuras (preparadas, no construidas):** Vulneraciones/FIR (6.5) — se propaga
`requiere_ficha_reservada`; Productos/Tareas (6.6); Antecedentes (6.8). El enriquecimiento on-demand
(`ClienteRequerimientos`) está **desactivado por defecto**; al activarse, propaga el token del usuario y
expone solo metadatos no sensibles.
