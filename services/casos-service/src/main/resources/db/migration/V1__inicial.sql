-- =====================================================================================
--  casos-service (SMID 6.4) — Esquema inicial
--  Defensoría de los Derechos de la Niñez · Núcleo Fundacional SMID
-- =====================================================================================
--
--  CONTEXTO Y SUPUESTOS DE DISEÑO
--  ------------------------------
--  Casos (6.4) NO tiene especificación formal en el Núcleo Fundacional (se declara fuera
--  de alcance, §1.3). Este esquema se DISEÑA de forma coherente con: (a) el evento
--  `requerimiento.asignado` que publica requerimientos-service (6.3), (b) los principios
--  transversales del capítulo 2, y (c) las costuras dejadas por requerimientos. Cada
--  supuesto relevante queda documentado aquí.
--
--  ARRANQUE LIMPIO — SIN MIGRACIÓN (decisión arquitectónica, override)
--  ------------------------------------------------------------------
--  La base arranca VACÍA. Flyway crea SOLO la estructura; no hay semilla de negocio ni
--  migración de expedientes históricos. El padrón/historial del sistema legado (SIGER)
--  permanece CONGELADO y se accederá, en el futuro, mediante el puente `siger-service`.
--  Los casos se materializan EXCLUSIVAMENTE desde el evento `requerimiento.asignado` en
--  adelante. Cualquier indicación de migrar casos/historial desde el legado se IGNORA.
--  Se deja una COSTURA reservada (puerto de cruce con el legado) que NO se implementa aquí.
--
--  DISCREPANCIAS RESUELTAS (alineadas con requerimientos-service)
--  -------------------------------------------------------------
--   1. Identificadores públicos: se expone solo `alt_key` (UUID). Las columnas de
--      identificadores se modelan como VARCHAR(36) (alojan el UUID canónico) para
--      compatibilidad estricta con la validación de esquema de Hibernate (String→VARCHAR).
--   2. Enumerados: se modelan como VARCHAR(N) + CHECK (no se usan ENUM nativos de MySQL).
--   3. Marcas de tiempo: DATETIME(6) en UTC (la aplicación fija hibernate.jdbc.time_zone=UTC).
--   4. Booleanos: TINYINT(1) (el conector MySQL los reporta como BIT, compatibles con boolean).
--   5. Sobre de error de la API: usa el campo `ruta` (no "path"). [Aplica en la capa API.]
--   6. Se añaden `creado_en` y `actualizado_en` para trazabilidad uniforme.
--
--  IDEMPOTENCIA
--  ------------
--  La unicidad de `id_requerimiento_origen_alt` (uk_caso_requerimiento_origen) garantiza un
--  único caso por requerimiento de origen. Es el sostén durable del consumidor de eventos
--  (entrega at-least-once): la reentrega es no-op y una carrera entre consumidores se
--  resuelve por reintento.
--
--  Motor/-charset: InnoDB, utf8mb4 (collation utf8mb4_0900_ai_ci).
-- =====================================================================================

-- -------------------------------------------------------------------------------------
--  Tabla: caso  (agregado raíz del expediente)
-- -------------------------------------------------------------------------------------
CREATE TABLE caso (
    id                              BIGINT        NOT NULL AUTO_INCREMENT,
    alt_key                         VARCHAR(36)   NOT NULL,
    numero_expediente               VARCHAR(32)   NOT NULL,
    codigo_sede                     VARCHAR(8)    NOT NULL,
    serie                           VARCHAR(8)    NOT NULL,
    correlativo                     BIGINT        NOT NULL,
    anio                            INT           NOT NULL,
    id_requerimiento_origen_alt     VARCHAR(36)   NOT NULL,
    folio_requerimiento             VARCHAR(24)   NULL,
    id_sede_alt                     VARCHAR(36)   NOT NULL,
    id_unidad_alt                   VARCHAR(36)   NULL,
    id_profesional_responsable_alt  VARCHAR(36)   NULL,
    estado                          VARCHAR(20)   NOT NULL,
    complejidad                     VARCHAR(12)   NULL,
    requiere_ficha_reservada        TINYINT(1)    NOT NULL DEFAULT 0,
    es_beta                         TINYINT(1)    NOT NULL DEFAULT 0,
    abierto_en                      DATETIME(6)   NOT NULL,
    cerrado_en                      DATETIME(6)   NULL,
    creado_en                       DATETIME(6)   NOT NULL,
    actualizado_en                  DATETIME(6)   NOT NULL,
    creado_por                      VARCHAR(36)   NULL,
    vigente                         TINYINT(1)    NOT NULL DEFAULT 1,
    CONSTRAINT pk_caso PRIMARY KEY (id),
    CONSTRAINT uk_caso_alt_key UNIQUE (alt_key),
    CONSTRAINT uk_caso_numero_expediente UNIQUE (numero_expediente),
    CONSTRAINT uk_caso_requerimiento_origen UNIQUE (id_requerimiento_origen_alt),
    CONSTRAINT ck_caso_estado CHECK (estado IN
        ('ABIERTO','EN_INVESTIGACION','EN_SEGUIMIENTO','SUSPENDIDO','CERRADO','ARCHIVADO')),
    CONSTRAINT ck_caso_complejidad CHECK (complejidad IS NULL OR complejidad IN
        ('BAJA','MEDIANA','ALTA','FAST_TRACK')),
    CONSTRAINT ck_caso_serie CHECK (serie IN ('OFICIAL','BETA'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_caso_sede        ON caso (id_sede_alt);
CREATE INDEX idx_caso_unidad      ON caso (id_unidad_alt);
CREATE INDEX idx_caso_estado      ON caso (estado);
CREATE INDEX idx_caso_responsable ON caso (id_profesional_responsable_alt);

-- -------------------------------------------------------------------------------------
--  Tabla: caso_transicion  (historial de la máquina de estados)
--  El asiento de apertura usa la pseudo-acción 'MATERIALIZACION' y estado_origen NULL.
-- -------------------------------------------------------------------------------------
CREATE TABLE caso_transicion (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)   NOT NULL,
    id_caso         BIGINT        NOT NULL,
    estado_origen   VARCHAR(20)   NULL,
    estado_destino  VARCHAR(20)   NOT NULL,
    accion          VARCHAR(32)   NOT NULL,
    observacion     VARCHAR(2000) NULL,
    actor_alt       VARCHAR(36)   NULL,
    ocurrido_en     DATETIME(6)   NOT NULL,
    CONSTRAINT pk_caso_transicion PRIMARY KEY (id),
    CONSTRAINT uk_transicion_alt_key UNIQUE (alt_key),
    CONSTRAINT fk_transicion_caso FOREIGN KEY (id_caso) REFERENCES caso (id),
    CONSTRAINT ck_transicion_estado_origen CHECK (estado_origen IS NULL OR estado_origen IN
        ('ABIERTO','EN_INVESTIGACION','EN_SEGUIMIENTO','SUSPENDIDO','CERRADO','ARCHIVADO')),
    CONSTRAINT ck_transicion_estado_destino CHECK (estado_destino IN
        ('ABIERTO','EN_INVESTIGACION','EN_SEGUIMIENTO','SUSPENDIDO','CERRADO','ARCHIVADO'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX idx_transicion_caso ON caso_transicion (id_caso);

-- -------------------------------------------------------------------------------------
--  Tabla: correlativo_expediente  (numeración atómica por sede/año/serie)
--  La serie BETA se mantiene AISLADA de la OFICIAL en filas distintas.
--  El adaptador usa INSERT ... ON DUPLICATE KEY UPDATE con LAST_INSERT_ID() para reservar.
-- -------------------------------------------------------------------------------------
CREATE TABLE correlativo_expediente (
    id_sede_alt   VARCHAR(36) NOT NULL,
    anio          INT         NOT NULL,
    serie         VARCHAR(8)  NOT NULL,
    ultimo        BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_correlativo_expediente PRIMARY KEY (id_sede_alt, anio, serie),
    CONSTRAINT ck_correlativo_serie CHECK (serie IN ('OFICIAL','BETA'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
