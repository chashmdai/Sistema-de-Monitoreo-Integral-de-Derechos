# requerimientos-service · SMID 6.3 — Requerimientos

Microservicio de **Requerimientos** del Núcleo Fundacional SMID — Defensoría de los Derechos de la
Niñez. Cierra el ciclo **USR.01 (ingreso)** y **USR.02 (admisibilidad y derivación)** del
requerimiento, manteniendo el aislamiento entre sedes/unidades y la trazabilidad hacia Auditoría.

- **Stack:** Java 21 (hilos virtuales), Spring Boot 3.5.15, Maven, MySQL 8 (InnoDB, `utf8mb4_0900_ai_ci`),
  Flyway (`validate`), jjwt 0.12.5 (HS256 con `kid`), RabbitMQ, SpringDoc OpenAPI 2.8.x.
- **Arquitectura:** hexagonal estricta. El **dominio es POJO puro** (sin Spring/JPA/Lombok); los
  adaptadores concentran la tecnología. La PK interna (`BIGINT`) **nunca** cruza la frontera: la API
  expone solo `alt_key` (UUID), cerrando IDOR (Núcleo 2.2).
- **Coordenadas:** `artifactId` `requerimientos-service`, paquete base `cl.smid.requerimientos`,
  versión `1.0.0`, BD `db_requerimientos`, puerto `8089`, tras el Gateway en `/api/requerimientos/**`
  (con `StripPrefix=1`, los controladores cuelgan de `/requerimientos/...`).

> **Arranque sin migración.** El servicio arranca con **base vacía**: Flyway crea **solo el esquema**,
> sin semilla de negocio. Los requerimientos referencian personas SMID por `alt_key` + _snapshots_.

---

## 1. Arquitectura por capas

```
cl.smid.requerimientos
├── api                      Adaptadores de entrada (REST)
│   ├── dto                  Peticiones y respuestas (solo alt_key)
│   ├── error                Sobre de error unificado + @RestControllerAdvice
│   ├── MapeadorRespuesta    Dominio -> DTO
│   └── RequerimientoController  @Transactional demarcada por método
├── dominio                  POJO puro (sin Spring/JPA/Lombok)
│   ├── modelo               Agregado Requerimiento, VOs, enums, Folio, snapshots
│   ├── servicio             MaquinaEstados, GeneradorFolio, EvaluadorAlcance, ServicioRequerimientos
│   ├── excepcion            CodigoError + jerarquía de excepciones de dominio
│   └── puerto               entrada (caso de uso) / salida (repos, directorios, eventos, reloj)
├── infraestructura
│   ├── persistencia         Entidades JPA, repos Spring Data y adaptadores (mapeo explícito)
│   ├── seguridad            Validación JWT, filtro, 401/403, autorización por rol
│   ├── eventos              Publicador conmutable (log / RabbitMQ)
│   ├── cliente              Adaptadores REST (Personas, Catálogo, Identidad) + Sedes (config)
│   └── soporte              Reloj de sistema, generador de alt_key
└── config                   Cableado de dominio, seguridad y propiedades
```

---

## 2. Ciclo de vida y máquina de estados

Tabla de transiciones pura (`MaquinaEstados`), `Map<(origen, evento) → destino>`, O(1):

```
BORRADOR ──ENVIAR──▶ INGRESADO ──ABRIR_ADMISIBILIDAD──▶ EN_ADMISIBILIDAD
                                                         ├─ DECIDIR_INADMISIBLE ─▶ INADMISIBLE
                                                         ├─ DECIDIR_RESPUESTA_INMEDIATA ─▶ RESPONDIDO
                                                         └─ DECIDIR_ASIGNACION ─▶ ASIGNADO
```

- **Mutable** (admite edición y alta de NNA/anexos): `BORRADOR`, `INGRESADO`, `EN_ADMISIBILIDAD`.
- **Terminales**: `INADMISIBLE`, `RESPONDIDO`, `ASIGNADO`. `ASIGNADO` **cierra la edición**.
- Una transición inválida lanza `REQ-409`.
- **Mínimos de ingreso** (`BORRADOR → INGRESADO`): `canal`, `sede` y **≥ 1 NNA** afectado; si falta
  alguno, `REQ-422`.
- Al registrar una decisión sobre un requerimiento aún `INGRESADO`, la admisibilidad se **abre
  automáticamente** antes de aplicar la acción.

### Admisibilidad (USR.02): tres acciones disjuntas

`POST /requerimientos/{altKey}/admisibilidad` exige **rol de Coordinación** (si falta → `AUTZ-004`).

| Acción                | Efecto                                                                                          | Estado final  | Evento                      |
| --------------------- | ----------------------------------------------------------------------------------------------- | ------------- | --------------------------- |
| `INADMISIBLE`         | Registra; `escaladoADefensora` opcional                                                         | `INADMISIBLE` | `requerimiento.inadmisible` |
| `RESPUESTA_INMEDIATA` | **Registra y NO envía** comunicación saliente                                                   | `RESPONDIDO`  | `requerimiento.respondido`  |
| `ASIGNACION`          | Exige `idProfesionalAsignadoAlt`; valida pertenencia a la unidad de destino; **cierra edición** | `ASIGNADO`    | `requerimiento.asignado`    |

---

## 3. Folio oficial y serie Beta

Formato: `{CODIGO_SEDE}-{[B]CORRELATIVO}/{AÑO}` — p. ej. `RM-1/2027` (oficial), `RM-B1/2027` (beta).

- El correlativo es **atómico y único** por `(sede, año, serie)`. El adaptador lo reserva con un
  **UPSERT** de MySQL (`INSERT ... ON DUPLICATE KEY UPDATE`), que serializa las solicitudes
  concurrentes bajo bloqueo de fila; la transacción observa su propia escritura y obtiene un número
  exclusivo (probado con un test concurrente).
- La **serie BETA está aislada** (prefijo `B`): **no consume** la numeración oficial.
- **Política por fecha:** antes de `smid.folio.inicio-oficial` (por defecto `2027-01-01`) los
  requerimientos nuevos usan serie BETA (marcha blanca); desde esa fecha, serie OFICIAL **inmaculada
  desde 1**. Se admite `esBeta` como override explícito por requerimiento.
- `CODIGO_SEDE` se resuelve por un puerto `DirectorioSedes` respaldado por configuración
  (`smid.sedes.codigos.<alt_key>`), ya que el token solo transporta el `alt_key` de la sede.

---

## 4. Seguridad y alcance territorial

- **Defensa en profundidad (DT-3):** cada servicio valida el JWT por su cuenta (firma HS256, `iss`,
  `aud`, `exp`), con ventana de rotación de clave (`kid` activo + previo).
- **Claims usados** (Núcleo 2.3): `sub` (alt_key usuario), `roles[]`, `idSede`, `idUnidad`,
  `alcance` (`UNIDAD|SEDE|NACIONAL`), `nombre`.
- **Acceso territorial registro a registro** (Núcleo 2.3): `NACIONAL` ve todo; `SEDE` filtra por
  `id_sede`; `UNIDAD` filtra por `id_unidad_destino`. **Fuera de alcance ⇒ 404** (no se revela la
  existencia, no 403).
- **Rol de Coordinación** para la admisibilidad: configurable en
  `smid.seguridad.roles-coordinacion` (por defecto `COORDINADOR, COORDINACION, ADMIN_SEDE,
ADMIN_NACIONAL`).

---

## 5. Contratos de la API

Rutas tras el `StripPrefix` del Gateway (todas requieren `Authorization: Bearer <jwt>`).

Swagger/OpenAPI queda disponible directamente en el servicio:

```text
GET /v3/api-docs
GET /swagger-ui/index.html
```

La documentación muestra las rutas internas (`/requerimientos/**`). El frontend
consume siempre vía Gateway anteponiendo `/api`, por ejemplo:
`/api/requerimientos`, `/api/requerimientos/{altKey}` o
`/api/requerimientos/{altKey}/admisibilidad`.

| Método | Ruta                                              | Autorización         | Descripción                           |
| ------ | ------------------------------------------------- | -------------------- | ------------------------------------- |
| POST   | `/requerimientos`                                 | Autenticado          | Crea un requerimiento en BORRADOR     |
| PUT    | `/requerimientos/{altKey}`                        | Territorial          | Edita (partial-merge) si es mutable   |
| POST   | `/requerimientos/{altKey}/nna`                    | Territorial          | Agrega un NNA afectado y sus derechos |
| POST   | `/requerimientos/{altKey}/enviar`                 | Territorial          | Ingresa: BORRADOR → INGRESADO         |
| POST   | `/requerimientos/{altKey}/admisibilidad`          | **Rol Coordinación** | Decide la admisibilidad (USR.02)      |
| GET    | `/requerimientos/{altKey}`                        | Territorial          | Detalle (404 si fuera de alcance)     |
| GET    | `/requerimientos?estado=&unidad=&pagina=&tamano=` | Territorial          | Lista paginada dentro del alcance     |

### Ejemplos de payload

**Crear** — `POST /requerimientos`

```json
{
  "canal": "WEB",
  "complejidad": "MEDIANA",
  "urgencia": "AMARILLO",
  "idUnidadDestinoAlt": "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e",
  "resumen": "Posible vulneración de derecho a la educación.",
  "idRequirenteAlt": "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d",
  "esBeta": null
}
```

Respuesta `201 Created` (con `Location`), detalle (extracto):

```json
{
  "altKey": "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f",
  "folio": "RM-1/2027",
  "idSede": "3b2a1c0d-...",
  "idUnidadDestino": "5f1d2c8e-...",
  "estado": "BORRADOR",
  "canal": "WEB",
  "complejidad": "MEDIANA",
  "urgencia": "AMARILLO",
  "requiereFichaReservada": true,
  "esBeta": false,
  "nnas": [],
  "anexos": [],
  "admisibilidades": []
}
```

**Agregar NNA** — `POST /requerimientos/{altKey}/nna`

```json
{
  "idPersonaAlt": "7d6c5b4a-3e2f-1a0b-9c8d-7e6f5a4b3c2d",
  "derechos": [
    { "idDerechoAlt": "a1b2c3d4-...", "idCausaAlt": "e5f6a7b8-..." },
    { "idDerechoAlt": "b2c3d4e5-...", "idCausaAlt": null }
  ]
}
```

**Decidir admisibilidad** — `POST /requerimientos/{altKey}/admisibilidad`

```json
{
  "accion": "ASIGNACION",
  "idProfesionalAsignadoAlt": "c3d4e5f6-...",
  "observacion": "Deriva a UPRJ"
}
```

```json
{
  "accion": "INADMISIBLE",
  "escaladoADefensora": true,
  "observacion": "No es competencia"
}
```

```json
{
  "accion": "RESPUESTA_INMEDIATA",
  "observacion": "Orientación entregada en el acto"
}
```

---

## 6. Códigos de error (sobre unificado, Núcleo 2.5)

El cuerpo de error usa el campo **`ruta`** (no `path`); `detalles` solo aparece en validación.

```json
{
  "status": 409,
  "error": "Conflict",
  "codigo": "REQ-409",
  "mensaje": "La operación entra en conflicto con el estado actual del requerimiento.",
  "ruta": "/requerimientos/1c2d3e4f-.../enviar",
  "timestamp": "2027-04-10T09:00:00Z"
}
```

| Código     | HTTP | Significado                                                     |
| ---------- | ---- | --------------------------------------------------------------- |
| `REQ-001`  | 400  | Validación de DTO/parámetros o cuerpo ilegible                  |
| `REQ-404`  | 404  | Inexistente **o fuera de alcance** territorial                  |
| `REQ-409`  | 409  | Transición de estado (o folio) en conflicto                     |
| `REQ-422`  | 422  | Regla de negocio (mínimos de ingreso, profesional/unidad, etc.) |
| `REQ-500`  | 500  | Error interno (sin filtrar detalle)                             |
| `AUTZ-003` | 401  | No autenticado (token ausente/mal firmado/expirado)             |
| `AUTZ-004` | 403  | Autenticado sin rol requerido (Coordinación en admisibilidad)   |

---

## 7. Eventos de dominio

- Exchange topic `smid.eventos`; clave de enrutamiento = tipo del evento (`requerimiento.*`).
- Transporte **conmutable** por `smid.eventos.transporte` (`log` por defecto, o `rabbitmq`).
- **Solo metadatos no sensibles (G7):** folio, estado, sede, unidad, complejidad, banderas. **Nunca**
  nombres, RUT ni relatos. La publicación es **tolerante a fallos** (no deshace la operación).

Tipos: `requerimiento.ingresado`, `requerimiento.clasificado`, `requerimiento.inadmisible`,
`requerimiento.respondido`, `requerimiento.asignado`.

---

## 8. Variables de entorno

Ver `.env.example`. Resumen:

| Variable                               | Por defecto                                          | Descripción                           |
| -------------------------------------- | ---------------------------------------------------- | ------------------------------------- |
| `SERVER_PORT`                          | `8089`                                               | Puerto del servicio                   |
| `DB_URL` / `DB_USER` / `DB_PASSWORD`   | —                                                    | Conexión MySQL (`db_requerimientos`)  |
| `JWT_ISSUER`                           | `smid-auth`                                          | `iss` esperado                        |
| `JWT_AUDIENCE`                         | `smid-servicios`                                     | Valor que `aud` debe contener         |
| `JWT_KID` / `JWT_SECRET`               | —                                                    | Clave de firma activa (HS256)         |
| `JWT_KID_PREVIO` / `JWT_SECRET_PREVIO` | (vacío)                                              | Clave previa (ventana de rotación)    |
| `EVENTOS_TRANSPORTE`                   | `log`                                                | `log` o `rabbitmq`                    |
| `EVENTOS_EXCHANGE`                     | `smid.eventos`                                       | Topic exchange                        |
| `FOLIO_INICIO_OFICIAL`                 | `2027-01-01`                                         | Corte serie BETA → OFICIAL            |
| `SEDE_CODIGO_DEFECTO`                  | `SED`                                                | Código de sede de respaldo            |
| `PERSONAS_URL`                         | `http://localhost:8088`                              | personas-service (6.2)                |
| `CATALOGO_URL`                         | `http://localhost:8087`                              | catalogo-service (6.7)                |
| `IDENTIDAD_URL`                        | `http://localhost:8081`                              | Identidad (6.1)                       |
| `VERIFICACION_PROFESIONAL`             | `permisiva`                                          | `permisiva` o `estricta`              |
| `ROLES_COORDINACION`                   | `COORDINADOR,COORDINACION,ADMIN_SEDE,ADMIN_NACIONAL` | Roles con facultad de Coordinación    |
| `RABBITMQ_*`                           | `localhost:5672` / `guest`                           | Solo si `EVENTOS_TRANSPORTE=rabbitmq` |

`application.yml` contiene **solo** referencias `${VARIABLE}` (DT-2): nunca secretos.

---

## 9. Discrepancias resueltas frente a la documentación

Documentadas en la cabecera de `V1__inicial.sql`:

1. `id_sede` / `id_unidad_destino` → `CHAR(36)` **alt_key** (la doc sugería `BIGINT`), coherente con
   el arranque sin migración y el cierre de IDOR.
2. Enumerados → `VARCHAR(N) + CHECK` (en vez de `ENUM` nativo), para evolucionar sin `ALTER` y
   mantener el dominio como fuente de verdad.
3. Marcas de tiempo → `DATETIME(6)` en **UTC** (la app fija `hibernate.jdbc.time_zone=UTC`).
4. Se añade `actualizado_en` al requerimiento (no estaba en el modelo de referencia).
5. `canal` es **NULLABLE** (flexibilidad de ingreso); pasa a obligatorio solo al transitar a
   `INGRESADO`.
6. El campo de ruta del sobre de error se llama `ruta` (no `path`).

---

## 10. Cómo construir, probar y correr

> **Compilación y dependencias.** Se resuelven con Maven (requiere acceso a los repositorios de
> artefactos). Este repositorio se entrega como **código fuente**; no incluye binarios compilados.

```bash
# Compilar (omite pruebas)
mvn -q clean package -DskipTests

# Pruebas unitarias de dominio (sin Docker): máquina de estados, folio (incl. concurrencia),
# admisibilidad, alcance e invariantes del agregado.
mvn -q test -Dtest='MaquinaEstadosTest,GeneradorFolioTest,EvaluadorAlcanceTest,RequerimientoTest,ServicioRequerimientosTest'

# Suite completa, incluida la prueba de integración con MySQL real.
#   IMPORTANTE: la prueba de integración usa Testcontainers y REQUIERE Docker en ejecución.
mvn -q verify

# Ejecutar en local (perfil 'local' con trazas verbosas)
mvn -q spring-boot:run -Dspring-boot.run.profiles=local
```

La prueba de integración (`RequerimientoIntegracionTest`) levanta MySQL 8 vía Testcontainers, ejecuta
Flyway y verifica: 401 sin token, 401 con token mal firmado, **403 sin rol de Coordinación** (camino
alcanzable), alta `201`, flujo de ingreso, admisibilidad con Coordinación, folio oficial vs beta y el
recorte territorial `404`. Las dependencias externas (Personas, Catálogo, Identidad) se sustituyen
por dobles en memoria; la persistencia y Flyway son reales.

---

## 11. Decisiones y costuras pendientes (documentadas)

- **Verificación profesional ↔ unidad:** Identidad aún no publica un endpoint estable de consulta de
  usuario por `alt_key`. La política `permisiva` (por defecto) tolera su indisponibilidad; la
  `estricta` la exige. En ambos modos, si la unidad se resuelve y **no** coincide, se rechaza
  (`REQ-422`). El adaptador es la costura: a futuro se reemplaza sin tocar el dominio.
- **Código de sede:** se resuelve por configuración (el `alt_key` viaja en el token, el código no).
  Costura lista para un adaptador REST contra Identidad.
- **Anexos:** solo metadatos; la `referenciaExterna` al binario queda nula hasta el servicio de
  Documentos (6.9).
- **Cruce con el padrón histórico:** fuera de alcance; el registro histórico permanece congelado en
  el legado y se accede por el puente `siger-service`.
