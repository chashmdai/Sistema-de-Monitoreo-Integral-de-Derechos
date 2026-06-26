# ESNNA — Consolidación de Refactorización

**Servicio:** `cl.smid.esnna` · puerto 8086 · Spring Boot 3.5.x / Java 21
**Propósito del documento:** fijar el alcance cerrado del refactor antes de escribir código. Es el contrato de lo que se construye y de lo que queda afuera.

---

## 1. Cambio de paradigma

**Hoy:** el servicio recibe PDFs, los manda a OpenAI y guarda el JSON. Es un wrapper de ChatGPT automatizado: analiza y archiva.

**Después:** sistema de gestión de casos. El análisis por IA sigue siendo el motor de entrada, pero el caso pasa a tener ciclo de vida (se corrige, cambia de estado, se sigue en el tiempo), la decisión del semáforo deja de ser caja negra, el caso se vuelve expediente (documento fuente + informe + auditoría), y el tablero sirve para priorizar de verdad.

Lo que NO cambia: el pipeline MAP/REDUCE de dos fases, los prompts del protocolo PR-PDR-05, la separación de capas y el manejo de errores de LLM (que ya era sólido).

---

## 2. Decisiones cerradas

| # | Decisión | Implicancia |
|---|----------|-------------|
| 1 | Transferencia de datos NNA a OpenAI **cubierta legalmente** (API dedicada Defensoría + unidad de estudios) | No se altera el flujo de envío. Se documenta como nota, no como bloqueo. |
| 2 | Campo `nna` pasa a **nombre completo** | Se elimina la regla de iniciales (§6.6.2) del prompt de extracción y consolidación. |
| 3 | Cifrado en reposo de PII: **hardcodeado / deuda reconocida** | Fuera de alcance. Se arregla después. |
| 4 | Semáforo IA inmutable + semáforo final humano | Desdoble `semaforoIA` / `semaforoFinal` con autor y fecha del override. |
| 5 | Imputados como **objeto unitario** | Migración de las tres listas paralelas a entidad hija `Imputado`. |
| 6 | `reasoning_effort = high` **siempre** | Obliga a recalcular el presupuesto de tokens (ver hallazgo IA-4). |
| 7 | Auditoría **solo al guardar** | El registro de análisis se persiste en `/guardar`, no en `/procesar`. |
| 8 | Límite de **30 archivos** por caso | Tope duro en `/procesar`. |
| 9 | Autorización **por rol** | `/procesar`, `/exportar-excel`, informe y métricas tras rol; `/casos` lectura para cualquier autenticado. |
| 10 | Modelo de consolidación a **gpt-5.5** | Snapshot pineado. gpt-5.5 reduce alucinación en dominio legal; reemplaza el alias flotante `gpt-5`. |
| 11 | Semáforo computado en **backend** | La IA detecta criterios + evidencia; el backend aplica la regla copulativa. |
| 12 | Endpoint de **métricas de concordancia**: entra | IA vs humano, por criterio. |

---

## 3. Modelo de datos nuevo

Punto de partida de la implementación. De acá cuelga todo lo demás.

### 3.1 Enums (reemplazan Strings libres)

- **`Semaforo`** → `ROJO`, `AMARILLO`, `VERDE`
- **`EstadoGestion`** → `PENDIENTE_REVISION`, `EN_REVISION`, `EN_QUERELLA`, `DERIVADO`, `CERRADO`, `ANULADO`
- **`RespuestaSiNo`** → `SI`, `NO` (serialización JSON `"SÍ"`/`"NO"` vía `@JsonValue`; resuelve la fragilidad del regex con tilde)
- **`SexoNna`** → `M`, `F`, `X`

Transiciones válidas de `EstadoGestion` (a confirmar el detalle, propuesta base):

```
PENDIENTE_REVISION → EN_REVISION → { EN_QUERELLA | DERIVADO | CERRADO }
EN_QUERELLA        → { DERIVADO | CERRADO }
DERIVADO           → CERRADO
(cualquiera)       → ANULADO   (borrado lógico, requiere motivo + autor)
CERRADO            → EN_REVISION  (reapertura, opcional)
```

### 3.2 `EsnnaEntity` (caso) — cambios clave

- Tipos correctos: `fecha`/`fechaNacimiento` → `LocalDate`; `edad` → `Integer`; `semaforo`/`estadoGestion`/`sexoNna` → enums; SÍ/NO → `RespuestaSiNo`.
- **Desdoble del semáforo:** `semaforoIA` + `confianzaIA` + `justificacionIA` (inmutables, lo que computó el backend desde la evidencia de la IA) y `semaforoFinal` + `semaforoFinalAutor` + `semaforoFinalFecha` (override humano).
- `@Version` (optimistic locking, evita lost update en edición concurrente).
- `fechaIngreso` (`@CreationTimestamp`) + `fechaActualizacion` (`@UpdateTimestamp`).
- Borrado lógico: `anulado` (bool) + `motivoAnulacion` + `anuladoPor`.
- Índices en `semaforoFinal`, `region`, `estadoGestion`, `nroOficio`, `fechaIngreso`, `ruc`.
- Imputados dejan de ser tres `List<String>` → relación a `Imputado`.
- Identidad mantiene `equals/hashCode` solo sobre `id` (ya estaba bien resuelto).

### 3.3 `Imputado` (entidad hija — tabla `esnna_imputados`)

```
id, caso_id (FK), orden, nombre, rut, domicilio, sexo, es_funcionario_publico
```

Una fila por imputado. Elimina el bug de desalineación posicional entre nombre/rut/domicilio. `orden` preserva el orden de aparición. La Fase 1 y 2 emiten ahora una lista de objetos imputado, no tres arrays correlacionados.

### 3.4 Criterios del semáforo estructurados

La salida de Fase 2 deja de traer `semaforo` calculado. Trae la evidencia; el backend decide.

```
criterios: {
  c1_pluralidadNna:        { cumple: bool, evidencia: string },
  c2_interseccionalidad:   { cumple: bool, subtipo: [...], evidencia: string },
  c3_agenteCualificado:    { cumple: bool, evidencia: string },
  c4_pluralidadVictimarios:{ cumple: bool, evidencia: string },
  c5_causaBasal:           { cumple: bool, evidencia: string }
}
exclusionAplicable:   { aplica: bool, cual: string }   // §5.4 / §5.1 / §6.2.5
improcedenciaPmaNad:  { aplica: bool, detalle: string } // §5.2 párrafo final
```

Regla determinista en backend (no en la IA):
- exclusión o improcedencia aplicable → `VERDE`
- ≥3 criterios `cumple=true` → `ROJO`
- exactamente 2 → `AMARILLO`
- ≤1 → `VERDE`

`presuntaRedExplotacion` se deriva de `c4`. La justificación se arma enumerando los criterios verificados. El semáforo queda auditable y el override humano puede discutir un criterio puntual.

### 3.5 `EsnnaAnalisisAudit` (tabla `esnna_analisis_audit`) — se registra al guardar

```
id, caso_id (FK), usuario_rut (del JWT), timestamp,
modelo_extraccion + snapshot, modelo_consolidacion + snapshot,
version_protocolo ("PR-PDR-05 v1 / 26-12-2024"),
documentos: [ { nombre_archivo, sha256 } ],   // hash, no contenido
semaforo_ia, confianza_ia,
tokens_prompt, tokens_completion, tokens_reasoning, costo_estimado
```

Inmutable. Una entrada por guardado. Da trazabilidad legal: qué documentos, qué modelo, qué versión de protocolo, quién y cuánto costó.

---

## 4. Hallazgos resueltos (trazabilidad de la auditoría)

### 4.1 Bugs / correcciones (sin esto el programa falla de hecho)

| ID | Problema | Resolución |
|----|----------|------------|
| IA-4 | `max_completion_tokens=16000` compartido entre razonamiento (gpt-5.5 + `high`) y JSON → `finish_reason=length` en casos complejos | Subir presupuesto con headroom para reasoning; medir `reasoning_tokens` reales del `usage`. |
| IA-3 / RES-1 | 429/503 transitorios botan el caso sin reintento | Resilience4j: retry con backoff exponencial para 429/5xx; subclase `GptRateLimitException`. |
| CTRL-2 | `/guardar` reasigna `fechaIngreso`/`estadoGestion` y anula el `@PrePersist` (O2) | Quitar la doble asignación; fuente única en la entidad. |
| MOD-3 | Imputados desalineables (tres listas paralelas) | Entidad `Imputado` (§3.3). |
| XLS-1 / CTRL-8 | `XSSFWorkbook` arma todo en memoria → OOM al exportar con volumen | `SXSSFWorkbook` (streaming); limitar `autoSizeColumn`. |
| XLS-5 | Formula/CSV injection desde datos extraídos por IA | Sanear prefijos `= + - @` en celdas de texto. |
| CTRL-9 | Sin usuario del JWT → imposible auditar autoría | Leer `sub` del `SecurityContext`; prerequisito de la decisión #7. |
| ERR-1 | Handlers de 401/403 muertos (los intercepta el filtro de seguridad antes del advice) | `AuthenticationEntryPoint` + `AccessDeniedHandler` que serialicen `ApiError`. |

### 4.2 De wrapper a sistema (funcionalidad faltante real)

| Bloque | Qué entrega |
|--------|-------------|
| Gestión del caso | `GET /casos/{id}`, `PATCH /casos/{id}`, transición de estado, borrado lógico con motivo/autor. `estadoGestion` deja de ser enum muerto. Toda edición auditada. |
| Semáforo defendible | Backend computa el semáforo desde la evidencia de la IA (§3.4). |
| Expediente | PDF fuente en MinIO (objeto = hash, lo que da sentido al hash de auditoría) + informe PDF por caso para la carpeta. |
| Tablero útil | Filtros combinables (estado, fecha, delito, región, RUC, RUT). Resuelve la cola de AMARILLO sin maquinaria aparte y reemplaza las dos queries muertas del repositorio. |
| Higiene operacional | Dedup al guardar (oficio repetido no infla ROJO); borrador con TTL por hash del lote (no re-pagar gpt-5.5 `high` si no se alcanzó a guardar); registro de tokens/costo por análisis. |
| Métricas (decisión #12) | `GET /metricas/concordancia`: tasa de override IA-vs-humano, dirección del sesgo, desglose por criterio. |

### 4.3 Endurecimiento / dependencias

| ID | Resolución |
|----|------------|
| AUTZ-1/2 + SEC-3 | `requestMatchers` por rol; `JwtAuthenticationConverter` para mapear claims de rol del Auth Service; validar `iss`/`aud` (aislamiento entre servicios del cluster). |
| CTRL-1 / DTO-3 | DTOs de respuesta (`EsnnaResumenDTO` listado, `EsnnaDetalleDTO` detalle). No se serializa la entity. |
| CTRL-4 | Tope de 30 archivos en `/procesar` (decisión #8). |
| CTRL-5 / DTO-1 | `/procesar` devuelve resultado tipado con estado de calidad (`COMPLETA`/`PARCIAL`) y detalle de documentos omitidos. |
| CTRL-7 | Validar content-type / magic bytes antes de procesar. |
| DEP-1 | POI a 5.4.x (cierra CVE de `commons-compress`). |
| DEP-2 + SEC-5 + RES-3 | `actuator` + micrometer; `/actuator/health/**` permitido sin auth; métricas y `traceId` (ERR-2) reales. |
| DTO-2 / CTRL-3 | MapStruct para el mapeo de ~44 campos; el controller orquesta, no transcribe. |
| PRIV-2 | Mensajes de excepción solo con metadato (archivo, página, código OpenAI), nunca eco de contenido. Logging del servicio a `INFO`; nunca se loggea el texto del documento. |

---

## 5. Contrato de la API

| Método | Ruta | Estado | Acceso |
|--------|------|--------|--------|
| GET | `/api/esnna/casos` | modificado (filtros combinables, page cap) | autenticado |
| GET | `/api/esnna/casos/{id}` | **nuevo** (detalle) | autenticado |
| POST | `/api/esnna/procesar` | modificado (límite 30, resultado tipado, JWT) | rol |
| POST | `/api/esnna/guardar` | modificado (audit, semáforo IA inmutable, dedup, JWT) | rol |
| PATCH | `/api/esnna/casos/{id}` | **nuevo** (edición parcial) | rol |
| POST | `/api/esnna/casos/{id}/estado` | **nuevo** (transición validada) | rol |
| DELETE | `/api/esnna/casos/{id}` | **nuevo** (borrado lógico + motivo) | rol |
| GET | `/api/esnna/casos/{id}/informe` | **nuevo** (PDF del caso) | rol |
| GET | `/api/esnna/casos/{id}/documento` | **nuevo** (PDF fuente desde MinIO) | rol |
| GET | `/api/esnna/exportar-excel` | modificado (streaming, sanitizado) | rol |
| GET | `/api/esnna/metricas/concordancia` | **nuevo** | rol |

---

## 6. Flujo del motor (después)

```
/procesar
  ├─ valida ≤30 archivos, content-type
  ├─ extrae texto (PDFBox) por documento → omite ilegibles (reporta cuáles)
  ├─ FASE 1 (MAP, gpt-4o-mini): extracción unitaria → omite fallos IA (reporta)
  ├─ FASE 2 (REDUCE, gpt-5.5 high): consolidación + criterios C1-C5 con evidencia
  ├─ BACKEND: aplica regla copulativa → semaforoIA determinista
  └─ devuelve resultado tipado { consolidado, estadoCalidad, omitidos[], usage }

/guardar (humano revisa y confirma)
  ├─ persiste caso (semaforoIA inmutable + semaforoFinal del humano)
  ├─ persiste imputados (entidad hija)
  ├─ guarda PDFs fuente en MinIO (hash)
  ├─ escribe EsnnaAnalisisAudit (modelos, versión protocolo, hashes, usuario, tokens/costo)
  └─ chequea duplicado (ruc / nroOficio / cedulaNna) → advierte
```

---

## 7. Fuera de alcance (por decisión explícita)

- **Cifrado en reposo de PII** (PRIV-3) — deuda reconocida, se arregla después.
- **Credenciales/secretos hardcodeados** (SEC-1/SEC-2) — misma deuda.
- **OCR encadenado** para PDFs escaneados — integrado por otra vía, no en `PdfExtractionService`. Se mantiene el rechazo actual de scans.
- **Versionado / reanálisis** de casos existentes — descartado por sobreingeniería.

> Nota de coherencia: con el campo `nna` ahora en nombre completo (decisión #2) y sin cifrado (decisión #3), los controles que SÍ entran — logging seguro (PRIV-2), DTOs de respuesta (CTRL-1), autorización por rol (AUTZ-1), Excel restringido (XLS-2) — pasan a ser la única barrera sobre la PII de menores. Quedan dentro del alcance precisamente por eso.

---

## 8. Orden de implementación

1. **Modelo de datos** — enums + `EsnnaEntity` refactorizada + `Imputado` + `EsnnaAnalisisAudit`. (De acá cuelga todo.)
2. Config + Security (env/rol/converter, actuator, entry point).
3. Repository (filtros, proyecciones) + DTOs + MapStruct.
4. Service (motor con semáforo en backend, retry, MinIO, costo) + PDF + Excel (streaming) + informe.
5. Controller (endpoints nuevos + modificados) + exception (entry point, multipart).
6. `pom.xml` + `application.properties` (POI 5.4, actuator, modelo gpt-5.5).
