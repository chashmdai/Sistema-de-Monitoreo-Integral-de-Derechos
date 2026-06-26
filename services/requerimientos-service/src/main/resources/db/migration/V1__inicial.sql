-- ============================================================================
--  requerimientos-service (6.3) — Núcleo Fundacional SMID
--  Migración inicial. Flyway es el ÚNICO dueño del esquema (DT-5; ddl-auto=validate).
--
--  ARRANQUE SIN MIGRACIÓN (decisión consolidada del ecosistema): esta migración
--  crea SOLO el esquema. No hay semilla de negocio ni ETL del padrón histórico
--  SIGER. Las personas se referencian por alt_key (registradas en personas-service).
--
--  DISCREPANCIAS RESUELTAS frente al esquema del documento funcional (Núcleo 6.2):
--   1. id_sede / id_unidad_destino: el documento los muestra como BIGINT. Aquí son
--      CHAR(36) (alt_key UUID), porque el token SOLO transporta alt_key, nunca la
--      llave interna de Identidad. Coherente con personas-service.
--   2. Enumerados: el documento usa ENUM nativo de MySQL. Aquí se modelan como
--      VARCHAR(N) + CHECK, por portabilidad y estabilidad frente a ddl-auto=validate.
--   3. Marcas de tiempo: el documento usa TIMESTAMP. Aquí DATETIME(6) en UTC
--      (hibernate.jdbc.time_zone=UTC), por precisión y portabilidad.
--   4. Se añade actualizado_en a 'requerimiento' (existe el endpoint PUT).
--   5. 'canal' es NULLABLE (el documento lo marca NOT NULL): la flexibilidad de
--      ingreso permite guardar un BORRADOR parcial. La obligatoriedad de 'canal'
--      se exige en el dominio SOLO al pasar de BORRADOR a INGRESADO.
-- ============================================================================

SET NAMES utf8mb4;

-- ----------------------------------------------------------------------------
--  1) requerimiento — agregado raíz del ciclo USR.01 + USR.02
-- ----------------------------------------------------------------------------
CREATE TABLE requerimiento (
    id                        BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key                   CHAR(36)     NOT NULL,                 -- identificador público (UUID)
    folio                     VARCHAR(24)  NOT NULL,                 -- {CODIGO_SEDE}-{CORRELATIVO}/{ANIO} (ver 6.5)
    id_sede                   CHAR(36)     NOT NULL,                 -- alt_key de la sede (del token)
    id_unidad_destino         CHAR(36)     NULL,                     -- alt_key de la unidad destino (nulable hasta clasificar)
    estado                    VARCHAR(20)  NOT NULL DEFAULT 'BORRADOR',
    canal                     VARCHAR(20)  NULL,                     -- WEB | PRESENCIAL (exigido al pasar a INGRESADO)
    complejidad               VARCHAR(20)  NULL,                     -- BAJA | MEDIANA | ALTA | FAST_TRACK
    urgencia                  VARCHAR(20)  NULL,                     -- VERDE | AMARILLO | ROJO
    requiere_ficha_reservada  TINYINT(1)   NOT NULL DEFAULT 0,       -- costura hacia Vulneraciones (6.5)
    id_requirente_alt         CHAR(36)     NULL,                     -- alt_key del requirente en personas-service
    requirente_snapshot       JSON         NULL,                     -- nombre/RUT al momento del ingreso (resiliencia)
    resumen                   VARCHAR(2000) NULL,
    fecha_ingreso             DATETIME(6)  NULL,                      -- instante del envío (BORRADOR -> INGRESADO)
    es_beta                   TINYINT(1)   NOT NULL DEFAULT 0,       -- 1 = serie BETA (marcha blanca), aísla la oficial
    vigente                   TINYINT(1)   NOT NULL DEFAULT 1,       -- borrado lógico (Núcleo 2.6)
    creado_en                 DATETIME(6)  NOT NULL,
    actualizado_en            DATETIME(6)  NOT NULL,
    creado_por                CHAR(36)     NULL,                     -- alt_key del autor (claim sub)
    PRIMARY KEY (id),
    UNIQUE KEY uk_requerimiento_alt_key (alt_key),
    UNIQUE KEY uk_requerimiento_folio (folio),
    KEY idx_req_sede (id_sede),
    KEY idx_req_unidad (id_unidad_destino),
    KEY idx_req_estado (estado),
    KEY idx_req_vigente (vigente),
    CONSTRAINT chk_req_estado CHECK (estado IN
        ('BORRADOR','INGRESADO','EN_ADMISIBILIDAD','INADMISIBLE','RESPONDIDO','ASIGNADO')),
    CONSTRAINT chk_req_canal CHECK (canal IS NULL OR canal IN ('WEB','PRESENCIAL')),
    CONSTRAINT chk_req_complejidad CHECK (complejidad IS NULL OR complejidad IN
        ('BAJA','MEDIANA','ALTA','FAST_TRACK')),
    CONSTRAINT chk_req_urgencia CHECK (urgencia IS NULL OR urgencia IN
        ('VERDE','AMARILLO','ROJO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
--  2) correlativo_folio — contador atómico por sede / año / serie
--     La unicidad (id_sede, anio, serie) + el incremento bajo bloqueo pesimista
--     garantizan folios únicos bajo carga concurrente. La serie BETA es una fila
--     independiente: NO consume la numeración OFICIAL.
-- ----------------------------------------------------------------------------
CREATE TABLE correlativo_folio (
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    id_sede       CHAR(36)    NOT NULL,                  -- alt_key de la sede
    anio          INT         NOT NULL,
    serie         VARCHAR(10) NOT NULL,                  -- OFICIAL | BETA
    ultimo_valor  BIGINT      NOT NULL DEFAULT 0,        -- 0 = serie inmaculada (p. ej. 0/2027)
    PRIMARY KEY (id),
    UNIQUE KEY uk_correlativo (id_sede, anio, serie),
    CONSTRAINT chk_correlativo_serie CHECK (serie IN ('OFICIAL','BETA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
--  3) requerimiento_nna — NNA afectados por el requerimiento (línea)
-- ----------------------------------------------------------------------------
CREATE TABLE requerimiento_nna (
    id                BIGINT   NOT NULL AUTO_INCREMENT,
    id_requerimiento  BIGINT   NOT NULL,
    id_persona_alt    CHAR(36) NOT NULL,                 -- alt_key del NNA en personas-service
    persona_snapshot  JSON     NULL,                     -- nombre/RUT al momento (resiliencia)
    PRIMARY KEY (id),
    KEY idx_reqnna_req (id_requerimiento),
    CONSTRAINT fk_reqnna_req FOREIGN KEY (id_requerimiento)
        REFERENCES requerimiento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
--  4) requerimiento_nna_derecho — derechos vulnerados por cada NNA (línea)
-- ----------------------------------------------------------------------------
CREATE TABLE requerimiento_nna_derecho (
    id                    BIGINT   NOT NULL AUTO_INCREMENT,
    id_requerimiento_nna  BIGINT   NOT NULL,
    id_derecho_alt        CHAR(36) NOT NULL,             -- alt_key del derecho en catalogo-service
    id_causa_alt          CHAR(36) NULL,                 -- alt_key de la causa en catalogo-service
    PRIMARY KEY (id),
    KEY idx_rnd_reqnna (id_requerimiento_nna),
    CONSTRAINT fk_rnd_reqnna FOREIGN KEY (id_requerimiento_nna)
        REFERENCES requerimiento_nna (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
--  5) requerimiento_anexo — SOLO metadatos; el binario llega con Documentos (6.9)
-- ----------------------------------------------------------------------------
CREATE TABLE requerimiento_anexo (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    id_requerimiento    BIGINT       NOT NULL,
    nombre_archivo      VARCHAR(240) NOT NULL,
    tipo_mime           VARCHAR(120) NULL,
    tamano_bytes        BIGINT       NULL,
    referencia_externa  VARCHAR(240) NULL,               -- nulo hasta que exista Documentos (6.9)
    PRIMARY KEY (id),
    KEY idx_anexo_req (id_requerimiento),
    CONSTRAINT fk_anexo_req FOREIGN KEY (id_requerimiento)
        REFERENCES requerimiento (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
--  6) admisibilidad — decisión USR.02 (una de tres acciones disjuntas)
-- ----------------------------------------------------------------------------
CREATE TABLE admisibilidad (
    id                           BIGINT        NOT NULL AUTO_INCREMENT,
    id_requerimiento             BIGINT        NOT NULL,
    accion                       VARCHAR(20)   NOT NULL,   -- INADMISIBLE | RESPUESTA_INMEDIATA | ASIGNACION
    id_coordinador_alt           CHAR(36)      NOT NULL,   -- alt_key del coordinador (claim sub)
    escalado_a_defensora         TINYINT(1)    NOT NULL DEFAULT 0,
    id_profesional_asignado_alt  CHAR(36)      NULL,       -- solo si accion = ASIGNACION
    observacion                  VARCHAR(2000) NULL,
    decidido_en                  DATETIME(6)   NOT NULL,
    PRIMARY KEY (id),
    KEY idx_adm_req (id_requerimiento),
    CONSTRAINT fk_adm_req FOREIGN KEY (id_requerimiento)
        REFERENCES requerimiento (id),
    CONSTRAINT chk_adm_accion CHECK (accion IN
        ('INADMISIBLE','RESPUESTA_INMEDIATA','ASIGNACION'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
