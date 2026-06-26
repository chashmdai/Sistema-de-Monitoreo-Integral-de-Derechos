# antecedentes-service · SMID 6.8 — Antecedentes y Hallazgos

Subsistema **Ficha de Antecedentes de la Unidad de Estudios y Gestión** + **Gestión de Hallazgos**
(origen funcional **RF028** de PR658). Recoge el *aprendizaje institucional* que un profesional
levanta a partir de un caso (6.4): buenas prácticas, nudos críticos y casos no abordados, con
clasificación, criterios de relevancia, derechos CDN vulnerados y un relato reservado cifrado.
Cuando una ficha lo amerita, **propone un hallazgo** que la Unidad de Estudios y Gestión consolida.

> El "antecedente" hijo de la FIR de Vulneraciones (6.5) es una entidad **distinta** pese al nombre
> compartido: aquí se trata de un repositorio analítico para el aprendizaje institucional, no de un
> registro evidenciario dentro de una ficha reservada.

- **artifactId:** `antecedentes-service` · **versión:** 1.0.0 · **paquete base:** `cl.smid.antecedentes`
- **BD:** `db_antecedentes` · **puerto:** 8094
- **Gateway:** `/api/antecedentes/**` → `StripPrefix=1` → el servicio recibe `/antecedentes/**`

---

## 1. Stack

Java 21 · Spring Boot 3.5.15 · Maven (sin wrapper) · MySQL 8 (InnoDB, `utf8mb4_0900_ai_ci`) ·
Flyway (`ddl-auto=validate`) · jjwt 0.12.5 · RabbitMQ · Testcontainers. Lombok solo en entidades JPA.

---

## 2. Arquitectura (hexagonal estricta)

```
api ───────────────► dominio ◄─────────────── infraestructura
(controladores,        (POJOs puros:            (JPA, seguridad JWT,
 DTOs, sobre error,     modelo, puertos,         eventos, cifrado,
 mapeo a respuesta)     servicios)               adaptadores de puertos)
                            ▲
                            │ wiring
                         config (CableadoDominio, seguridad, rabbit, propiedades)
```

- **Dominio POJO:** sin Spring/JPA/Lombok. Todo su wiring vive en `config.CableadoDominio`.
- **`@Transactional` solo en la frontera:** controladores (y listeners RabbitMQ, si los hubiera);
  nunca en el dominio.
- **Servicio hoja:** no realiza llamadas síncronas a otros servicios. Las referencias externas
  (`casoAlt`, `unidadAlt`, `sedeAlt`, `profesionalAlt`, `institucionAlt`) se guardan como `alt_key`
  opacos, validados solo en formato.

### Capas y paquetes

| Paquete | Contenido |
|---|---|
| `dominio.modelo` | Agregados (`FichaAntecedente`, `Hallazgo`), VOs, enums, `Referencia`, eventos |
| `dominio.puerto.entrada` | Casos de uso (`GestionFichas/Hallazgos/Referencias`) + comandos |
| `dominio.puerto.salida` | Puertos de persistencia, correlativo, eventos, reloj, ids |
| `dominio.servicio` | `MaquinaEstadosFicha`, `EvaluadorAlcance`, `GeneradorFolio`, orquestadores |
| `dominio.excepcion` | `CodigoError` + jerarquía de excepciones de negocio |
| `infraestructura.persistencia` | Entidades JPA, repos Spring Data, mapeadores, adaptadores, cifrado |
| `infraestructura.seguridad` | Validación JWT, filtro, `ProveedorContexto`, manejadores 401/403 |
| `infraestructura.eventos` | Publicadores log / RabbitMQ (conmutables) |
| `api` | Controladores, DTOs, `MapeadorRespuesta`, sobre de error |
| `config` | Cableado de dominio, seguridad, rabbit, `@ConfigurationProperties` |

---

## 3. Modelo de dominio

### FichaAntecedente (agregado raíz)

`altKey`, `folio` (serie `FA-{N}/{anio}`), `estado`, contexto territorial (`unidadAlt`, `sedeAlt`),
autoría (`profesionalAlt`, `jefaturaAlt?`), `procesoId`, `casoAlt?`, derechos CDN (1..54 sin
duplicados), `categoriaPrincipalId` + `categoriasSecundariasIds` (máx 2; principal ≠ secundarias),
`descripcion`, `relato` (cifrado AES-256-GCM en reposo), `calificacion`, `criterios`,
`percepcionHallazgo`, `hallazgoAlt?`, documentos (metadatos), historial, marcas temporales.

> Los identificadores de referencia (`procesoId`, `categoriaPrincipalId`,
> `categoriasSecundariasIds`, `instrumentoId`) **transportan el `alt_key` (UUID)** de la referencia.
> La PK interna BIGINT nunca se expone (los adaptadores traducen `alt_key ↔ id`).

### Hallazgo (agregado raíz)

`altKey`, `folio` (serie `HZ-{N}/{anio}`), `titulo`, `descripcion`, `estado`, `instrumentoId`,
`temporalidad`, `unidadesInvolucradas`, `institucionesExternas`, `origenFichaAlt?`, marcas.
Relación `FichaAntecedente.hallazgoAlt → Hallazgo` (N:1 opcional).

### Tablas de referencia locales (viven en 6.8, no en Catálogo 6.7)

`CategoriaDdn`, `ProcesoDdn`, `Instrumento` — cada una: `altKey`, `codigo` (único), `nombre`,
`vigente`. CRUD admin. **Sembradas en Flyway con un set PROVISIONAL** (reemplazar por el listado
oficial DDN). Los artículos CDN **no** son tabla: se validan en rango 1..54.

### Enumeraciones

| Enum | Valores |
|---|---|
| `EstadoFicha` | `BORRADOR`, `EN_REVISION`, `APROBADA`, `RECHAZADA` |
| `Calificacion` | `BUENA_PRACTICA`, `NUDO_CRITICO`, `CASO_NO_ABORDADO` |
| `Criterio` | `GRAVEDAD`, `COMPLEJIDAD_COORDINACION`, `MAGNITUD_POBLACION`, `REPETICION`, `INTERPRETACION` |
| `PercepcionHallazgo` | `NO_ES_HALLAZGO`, `ANTECEDENTE_DE_HALLAZGO`, `SE_PROPONE_HALLAZGO` |
| `EstadoHallazgo` | `PROPUESTO`, `ASOCIADO`, `RECHAZADO` |
| `Temporalidad` | `CORTO_PLAZO`, `LARGO_PLAZO` |
| `Alcance` | `UNIDAD`, `SEDE`, `NACIONAL` |

---

## 4. Máquina de estados de la ficha

```
            enviarRevision                 aprobar
 BORRADOR ───────────────► EN_REVISION ───────────────► APROBADA (terminal)
    ▲                          │   │
    │        devolver          │   │ rechazar
    └──────────────────────────┘   └───────────────────► RECHAZADA (terminal)
```

- Solo `BORRADOR` es editable y eliminable.
- `enviarRevision`: `BORRADOR → EN_REVISION` (autor o miembro de la unidad).
- `devolver` / `aprobar` / `rechazar`: desde `EN_REVISION` (exigen **rol revisor**).
- Transición inválida → `ANT-422`.

---

## 5. Alcance territorial (override propio de 6.8)

Las fichas son **territoriales** (a diferencia de Instituciones 6.10, que son nacionales):

- **Lectura/listado:** acotado por el `alcance` del claim — `UNIDAD` solo su `idUnidad`; `SEDE` su
  `idSede`; `NACIONAL` todas. Una ficha fuera del alcance responde **404** (no 403).
- **Edición/eliminación:** exige pertenecer a la **misma unidad** y que la ficha esté en `BORRADOR`.
  Si no es de su unidad → 404 (denegación territorial). Si no está en `BORRADOR` → `ANT-422`.
- **Revisión** (`devolver`/`aprobar`/`rechazar`): exige **rol revisor**
  (`smid.seguridad.roles-revision`). Sin rol → `AUTZ-004 / 403` (primero se verifica visibilidad: si
  no la ve, 404).
- **Hallazgos:** lectura **nacional** autenticada; creación/edición/cambio de estado por rol
  gestión/revisor.
- **Referencias:** lectura nacional autenticada; escritura por **rol admin**
  (`smid.seguridad.roles-admin`).

> El `enviarRevision` se reconcilia según la sección de la máquina de estados (autor/miembro de la
> unidad), no como acción de rol revisor.

---

## 6. Cifrado del relato (AES-256-GCM)

`CifradorCampos` (infraestructura): AES-256-GCM con clave de 32 bytes desde
`ANTECEDENTES_CIFRADO_CLAVE` (Base64), IV aleatorio de 12 bytes por valor y tag de 128 bits. Se
persiste `Base64(iv ‖ ciphertext ‖ tag)` en `relato_cifrado`. El dominio maneja **texto plano**; el
mapeador cifra al escribir y descifra al leer. El relato **nunca** aparece en eventos, logs ni el
sobre de error. Conmutable a `none` (passthrough, solo desarrollo) con `smid.cifrado.relato`.

---

## 7. Folio atómico

Tabla `correlativo_folio` (`serie` PK, `anio`, `ultimo_valor`). Reserva atómica con el modismo
MySQL `INSERT ... VALUES (..., LAST_INSERT_ID(1)) ON DUPLICATE KEY UPDATE ultimo_valor =
LAST_INSERT_ID(ultimo_valor + 1)` + `SELECT LAST_INSERT_ID()`, sobre la conexión del
`EntityManager`, dentro de la transacción del controlador (nunca `MAX()+1`). Series independientes
`FA-{N}/{anio}` y `HZ-{N}/{anio}` con **reinicio anual** (la serie incluye el año).

---

## 8. API

Tras `StripPrefix=1`, el servicio expone `/antecedentes/**`. Todos los endpoints requieren JWT.
DTOs de respuesta `@JsonInclude(NON_NULL)`; enums como String; timestamps `Instant` ISO-8601.

### Fichas (territoriales; 404 fuera de alcance)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/antecedentes/fichas` | Crear (BORRADOR; autor y territorio del claim) → 201 |
| GET | `/antecedentes/fichas/{alt}` | Detalle (descifra relato) |
| PUT | `/antecedentes/fichas/{alt}` | Editar (solo BORRADOR, misma unidad) |
| DELETE | `/antecedentes/fichas/{alt}` | Eliminar (solo BORRADOR) → 204 |
| POST | `/antecedentes/fichas/{alt}/enviar-revision` | `BORRADOR → EN_REVISION` |
| POST | `/antecedentes/fichas/{alt}/devolver` | revisor |
| POST | `/antecedentes/fichas/{alt}/aprobar` | revisor |
| POST | `/antecedentes/fichas/{alt}/rechazar` | revisor |
| GET | `/antecedentes/fichas` | Bandeja (filtros `estado`, `calificacion`, `casoAlt`, `procesoId`, `texto`, `pagina`, `tamano`) |
| POST | `/antecedentes/fichas/{alt}/documentos` | Agregar metadato de documento |

### Hallazgos (lectura nacional)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/antecedentes/hallazgos` | Listado (`estado`, `temporalidad`, `texto`, paginado) |
| GET | `/antecedentes/hallazgos/{alt}` | Detalle |
| POST | `/antecedentes/hallazgos` | Crear propuesta directa (rol gestión/revisor) → 201 |
| PUT | `/antecedentes/hallazgos/{alt}` | Editar propuesta (solo PROPUESTO) |
| POST | `/antecedentes/hallazgos/{alt}/estado` | Cambiar estado (`ASOCIADO`/`RECHAZADO`) |

### Referencia (lectura nacional autenticada; escritura admin)

CRUD en `/antecedentes/{categorias|procesos|instrumentos}`:

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/antecedentes/{recurso}` | Crear → 201 |
| GET | `/antecedentes/{recurso}/{alt}` | Obtener |
| PUT | `/antecedentes/{recurso}/{alt}` | Editar nombre |
| POST | `/antecedentes/{recurso}/{alt}/activacion` | Activar/desactivar |
| GET | `/antecedentes/{recurso}` | Listar (`texto`, `vigente`, `pagina`, `tamano`) |

### Ejemplos de payload

**Crear ficha** (`POST /antecedentes/fichas`):

```json
{
  "procesoId": "22222222-2222-4222-8222-000000000001",
  "casoAlt": null,
  "categoriaPrincipalId": "11111111-1111-4111-8111-000000000001",
  "categoriasSecundariasIds": ["11111111-1111-4111-8111-000000000002"],
  "derechosCdn": [1, 19, 24],
  "descripcion": "Caso de aprendizaje institucional",
  "relato": "Relato reservado (se cifra en reposo)",
  "calificacion": "BUENA_PRACTICA",
  "criterios": ["GRAVEDAD", "REPETICION"],
  "percepcionHallazgo": "SE_PROPONE_HALLAZGO",
  "jefaturaAlt": null,
  "hallazgoAlt": null,
  "propuestaHallazgo": {
    "titulo": "Brecha de coordinación intersectorial",
    "descripcion": "Se observa…",
    "instrumentoId": "33333333-3333-4333-8333-000000000001",
    "temporalidad": "CORTO_PLAZO",
    "unidadesInvolucradas": [],
    "institucionesExternas": []
  }
}
```

**Detalle de ficha** (respuesta):

```json
{
  "altKey": "…",
  "folio": "FA-1/2027",
  "estado": "BORRADOR",
  "unidadAlt": "…",
  "sedeAlt": "…",
  "profesionalAlt": "…",
  "procesoId": "22222222-2222-4222-8222-000000000001",
  "categoriaPrincipalId": "11111111-1111-4111-8111-000000000001",
  "categoriasSecundariasIds": ["…"],
  "derechosCdn": [1, 19, 24],
  "descripcion": "…",
  "relato": "Relato reservado (se cifra en reposo)",
  "calificacion": "BUENA_PRACTICA",
  "criterios": ["GRAVEDAD", "REPETICION"],
  "percepcionHallazgo": "SE_PROPONE_HALLAZGO",
  "hallazgoAlt": "…",
  "documentos": [],
  "historial": [{ "tipoEvento": "CREACION", "actorAlt": "…", "ocurridoEn": "2027-05-01T10:00:00Z" }],
  "creadoEn": "2027-05-01T10:00:00Z",
  "actualizadoEn": "2027-05-01T10:00:00Z"
}
```

**Sobre de paginación**:

```json
{ "contenido": [ … ], "pagina": 0, "tamano": 20, "totalElementos": 42, "totalPaginas": 3 }
```

**Sobre de error** (campo `ruta`, `detalles` solo en validación):

```json
{
  "codigo": "ANT-422",
  "mensaje": "Transicion invalida: …",
  "ruta": "/antecedentes/fichas/…/aprobar",
  "timestamp": "2027-05-01T10:00:00Z"
}
```

---

## 9. Eventos (metadata-only, conmutables log/RabbitMQ)

Exchange de tópicos `smid.eventos`, routing key = tipo. El `alt_key` viaja en el sobre, **no** en
los metadatos; nunca se incluyen relatos, nombres ni RUT de personas.

| Evento | Metadatos |
|---|---|
| `ficha.creada` | `unidadAlt`, `calificacion`, `casoAlt?` |
| `ficha.enviada_revision` | — |
| `ficha.devuelta` | — |
| `ficha.aprobada` | — |
| `ficha.rechazada` | — |
| `ficha.propuesta_hallazgo` | `hallazgoAlt` |
| `hallazgo.creado` | `temporalidad` |
| `hallazgo.estado` | `estado` |

---

## 10. Códigos de error

| Código | HTTP | Situación |
|---|---|---|
| `ANT-001` | 400 | Validación / cuerpo ilegible / parámetro inválido |
| `ANT-404` | 404 | Recurso inexistente o denegación territorial |
| `ANT-409` | 409 | Conflicto de unicidad (folio, código de referencia) |
| `ANT-422` | 422 | Regla de negocio (transición inválida, incoherencia de hallazgo, referencia no vigente) |
| `ANT-500` | 500 | Error interno |
| `AUTZ-003` | 401 | No autenticado |
| `AUTZ-004` | 403 | Autenticado sin rol (revisión/admin) |

---

## 11. JWT (contrato del clúster)

HS256 con `kidActivo/secretoActivo` + `kidPrevio/secretoPrevio` (opcional, rotación). `JWT_SECRET`
compartido **byte a byte** con el Gateway. Resolución de clave por `kid` mediante `Locator<Key>` (no
`Locator<SecretKey>`, por invariancia de genéricos en jjwt 0.12.5). `parseSignedClaims` valida firma
y expiración; `iss` y `aud` se validan a mano (la audiencia esperada debe estar **contenida** en el
claim `aud`). Claims: `sub`, `iss=smid-auth`, `aud=smid-servicios`, `roles[]`, `idSede`, `idUnidad`,
`alcance` (`UNIDAD|SEDE|NACIONAL`), `nombre`.

`ProveedorContexto` se respalda en **request attributes** (no `@RequestScope`) para evitar el
problema de timing entre el filtro de seguridad y el `DispatcherServlet`.

---

## 12. Variables de entorno

Ver `.env.example`. Mínimo para arrancar en producción: `DB_URL`, `DB_USER`, `DB_PASSWORD`,
`JWT_SECRET` y `ANTECEDENTES_CIFRADO_CLAVE` (si `CIFRADO_RELATO=aes-gcm`).

---

## 13. Ejecución

```bash
# Compilar, validar esquema y ejecutar pruebas (requiere Docker para Testcontainers):
mvn clean verify

# Arrancar en local (perfil de desarrollo: relato passthrough, secreto de dev):
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Empaquetar:
mvn clean package
java -jar target/antecedentes-service-1.0.0.jar
```

> Maven Central no es alcanzable desde el entorno de generación; `mvn package`/`verify` se ejecutan
> localmente. El esquema lo gobierna **Flyway** (`V1__inicial.sql`); Hibernate solo valida.

---

## 14. Decisiones de diseño

1. **`alt_key` para todo identificador público**; PK BIGINT interna nunca expuesta. En el esquema se
   usa `VARCHAR(36)` (no `CHAR(36)`) por compatibilidad con la validación de Hibernate
   (mapeo `String → VARCHAR`); el valor sigue siendo un UUID opaco.
2. **Referencias por PK escalar** (`proceso_id`, `categoria_principal_id`, `categoria_id`,
   `instrumento_id` BIGINT); los adaptadores traducen `alt_key ↔ id`. Sin asociaciones JPA
   bidireccionales.
3. **Enums `VARCHAR` + `CHECK`**, `DATETIME(6)` UTC, `TINYINT(1)` booleanos (`tinyInt1isBit=true`).
4. **Denegación territorial = 404**, nunca 403.
5. **`@Transactional` solo en la frontera** (controladores).
6. **Eventos metadata-only**, transporte conmutable; un fallo de publicación nunca deshace la
   operación de negocio.
7. **Folio atómico** vía UPSERT + `LAST_INSERT_ID`, jamás `MAX()+1`.
8. **Cifrado AES-256-GCM** del relato; el dominio no conoce el cifrado.
9. Colecciones hijas como `@ElementCollection`; las puramente de valor usan PK compuesta (semántica
   de conjunto, sin duplicados).

---

## 15. Pruebas

- **Unitarias de dominio** con dobles in-memory: máquina de estados, alcance territorial,
  generador de folio, y reglas del orquestador (`ServicioFichasTest`: creación/folio/eventos,
  coherencia de hallazgo, categorías, CDN, 404 territorial, edición/eliminación solo en BORRADOR,
  autorización de revisión).
- **Cifrado** (`CifradorAesGcmTest`): ida y vuelta, IV aleatorio y detección de manipulación.
- **Integración** (`AntecedentesIntegracionTest`) con Testcontainers MySQL + Flyway + `MockMvc` y
  `GeneradorTokens` (jjwt 0.12.5, setters tipados): ciclo de vida de la ficha, descifrado de extremo
  a extremo, 401/403/404 y bandeja acotada al alcance. Se deshabilita si no hay Docker.
