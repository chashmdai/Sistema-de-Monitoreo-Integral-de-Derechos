-- =============================================================================
-- SMID 6.8 - Antecedentes y Hallazgos
-- Migracion inicial. Convenciones del cluster:
--   * PK BIGINT interna nunca expuesta; identificador publico alt_key (UUID, 36).
--     Se usa VARCHAR(36) (no CHAR) por compatibilidad con la validacion de Hibernate
--     (mapeo String -> VARCHAR); el valor sigue siendo un UUID opaco.
--   * Enums como VARCHAR + CHECK (no ENUM nativo).
--   * Timestamps DATETIME(6) en UTC (la app fija hibernate.jdbc.time_zone=UTC).
--   * Booleanos TINYINT(1) (URL JDBC con tinyInt1isBit=true).
--   * FKs por PK escalar; sin asociaciones bidireccionales.
-- Motor InnoDB, utf8mb4_0900_ai_ci.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Correlativos de folio (generacion atomica; series FA/HZ independientes por anio)
-- -----------------------------------------------------------------------------
CREATE TABLE correlativo_folio (
    serie         VARCHAR(40) NOT NULL,
    anio          INT         NOT NULL,
    ultimo_valor  BIGINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (serie)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- Tablas de referencia locales (decision 5.3: viven en 6.8, no en Catalogo 6.7)
-- -----------------------------------------------------------------------------
CREATE TABLE categoria_ddn (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)  NOT NULL,
    codigo          VARCHAR(80)  NOT NULL,
    nombre          VARCHAR(200) NOT NULL,
    vigente         TINYINT(1)   NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)  NOT NULL,
    actualizado_en  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_categoria_alt_key (alt_key),
    UNIQUE KEY uk_categoria_codigo (codigo)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE proceso_ddn (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)  NOT NULL,
    codigo          VARCHAR(80)  NOT NULL,
    nombre          VARCHAR(200) NOT NULL,
    vigente         TINYINT(1)   NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)  NOT NULL,
    actualizado_en  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_proceso_alt_key (alt_key),
    UNIQUE KEY uk_proceso_codigo (codigo)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE instrumento (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)  NOT NULL,
    codigo          VARCHAR(80)  NOT NULL,
    nombre          VARCHAR(200) NOT NULL,
    vigente         TINYINT(1)   NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)  NOT NULL,
    actualizado_en  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_instrumento_alt_key (alt_key),
    UNIQUE KEY uk_instrumento_codigo (codigo)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- Agregado raiz: FichaAntecedente
-- -----------------------------------------------------------------------------
CREATE TABLE ficha_antecedente (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key                 VARCHAR(36)  NOT NULL,
    folio                   VARCHAR(40)  NOT NULL,
    estado                  VARCHAR(20)  NOT NULL,
    unidad_alt              VARCHAR(36)  NOT NULL,
    sede_alt                VARCHAR(36)  NOT NULL,
    profesional_alt         VARCHAR(36)  NOT NULL,
    jefatura_alt            VARCHAR(36)  NULL,
    proceso_id              BIGINT       NOT NULL,
    caso_alt                VARCHAR(36)  NULL,
    categoria_principal_id  BIGINT       NOT NULL,
    descripcion             VARCHAR(4000) NOT NULL,
    relato_cifrado          TEXT         NOT NULL,
    calificacion            VARCHAR(30)  NOT NULL,
    percepcion_hallazgo     VARCHAR(30)  NOT NULL,
    hallazgo_alt            VARCHAR(36)  NULL,
    creado_en               DATETIME(6)  NOT NULL,
    actualizado_en          DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_ficha_alt_key (alt_key),
    UNIQUE KEY uk_ficha_folio (folio),
    KEY idx_ficha_unidad (unidad_alt),
    KEY idx_ficha_sede (sede_alt),
    KEY idx_ficha_estado (estado),
    KEY idx_ficha_caso (caso_alt),
    CONSTRAINT fk_ficha_proceso FOREIGN KEY (proceso_id) REFERENCES proceso_ddn (id),
    CONSTRAINT fk_ficha_categoria_principal FOREIGN KEY (categoria_principal_id) REFERENCES categoria_ddn (id),
    CONSTRAINT chk_ficha_estado CHECK (estado IN ('BORRADOR', 'EN_REVISION', 'APROBADA', 'RECHAZADA')),
    CONSTRAINT chk_ficha_calificacion CHECK (calificacion IN ('BUENA_PRACTICA', 'NUDO_CRITICO', 'CASO_NO_ABORDADO')),
    CONSTRAINT chk_ficha_percepcion CHECK (percepcion_hallazgo IN ('NO_ES_HALLAZGO', 'ANTECEDENTE_DE_HALLAZGO', 'SE_PROPONE_HALLAZGO'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Colecciones hijas de la ficha (FK por PK escalar; PK compuesta = semantica de conjunto)
CREATE TABLE ficha_categoria_secundaria (
    ficha_id      BIGINT NOT NULL,
    categoria_id  BIGINT NOT NULL,
    PRIMARY KEY (ficha_id, categoria_id),
    CONSTRAINT fk_fcs_ficha FOREIGN KEY (ficha_id) REFERENCES ficha_antecedente (id) ON DELETE CASCADE,
    CONSTRAINT fk_fcs_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_ddn (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE ficha_derecho_cdn (
    ficha_id         BIGINT   NOT NULL,
    numero_articulo  SMALLINT NOT NULL,
    PRIMARY KEY (ficha_id, numero_articulo),
    CONSTRAINT fk_fdc_ficha FOREIGN KEY (ficha_id) REFERENCES ficha_antecedente (id) ON DELETE CASCADE,
    CONSTRAINT chk_fdc_rango CHECK (numero_articulo BETWEEN 1 AND 54)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE ficha_criterio (
    ficha_id  BIGINT      NOT NULL,
    criterio  VARCHAR(40) NOT NULL,
    PRIMARY KEY (ficha_id, criterio),
    CONSTRAINT fk_fc_ficha FOREIGN KEY (ficha_id) REFERENCES ficha_antecedente (id) ON DELETE CASCADE,
    CONSTRAINT chk_fc_criterio CHECK (criterio IN ('GRAVEDAD', 'COMPLEJIDAD_COORDINACION', 'MAGNITUD_POBLACION', 'REPETICION', 'INTERPRETACION'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE ficha_documento (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    ficha_id            BIGINT       NOT NULL,
    alt_key             VARCHAR(36)  NOT NULL,
    nombre              VARCHAR(255) NOT NULL,
    referencia_externa  VARCHAR(255) NULL,
    PRIMARY KEY (id),
    KEY idx_fdoc_ficha (ficha_id),
    CONSTRAINT fk_fdoc_ficha FOREIGN KEY (ficha_id) REFERENCES ficha_antecedente (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE ficha_historial (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    ficha_id     BIGINT        NOT NULL,
    tipo_evento  VARCHAR(40)   NOT NULL,
    actor_alt    VARCHAR(36)   NULL,
    ocurrido_en  DATETIME(6)   NOT NULL,
    observacion  VARCHAR(1000) NULL,
    PRIMARY KEY (id),
    KEY idx_fhist_ficha (ficha_id),
    CONSTRAINT fk_fhist_ficha FOREIGN KEY (ficha_id) REFERENCES ficha_antecedente (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------------------------------
-- Agregado raiz: Hallazgo
-- -----------------------------------------------------------------------------
CREATE TABLE hallazgo (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key           VARCHAR(36)  NOT NULL,
    folio             VARCHAR(40)  NOT NULL,
    titulo            VARCHAR(255) NOT NULL,
    descripcion       VARCHAR(4000) NOT NULL,
    estado            VARCHAR(20)  NOT NULL,
    instrumento_id    BIGINT       NULL,
    temporalidad      VARCHAR(20)  NOT NULL,
    origen_ficha_alt  VARCHAR(36)  NULL,
    creado_en         DATETIME(6)  NOT NULL,
    actualizado_en    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_hallazgo_alt_key (alt_key),
    UNIQUE KEY uk_hallazgo_folio (folio),
    KEY idx_hallazgo_estado (estado),
    CONSTRAINT fk_hallazgo_instrumento FOREIGN KEY (instrumento_id) REFERENCES instrumento (id),
    CONSTRAINT chk_hallazgo_estado CHECK (estado IN ('PROPUESTO', 'ASOCIADO', 'RECHAZADO')),
    CONSTRAINT chk_hallazgo_temporalidad CHECK (temporalidad IN ('CORTO_PLAZO', 'LARGO_PLAZO'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE hallazgo_unidad (
    hallazgo_id  BIGINT      NOT NULL,
    unidad_alt   VARCHAR(36) NOT NULL,
    PRIMARY KEY (hallazgo_id, unidad_alt),
    CONSTRAINT fk_hu_hallazgo FOREIGN KEY (hallazgo_id) REFERENCES hallazgo (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE hallazgo_institucion (
    hallazgo_id     BIGINT      NOT NULL,
    institucion_alt VARCHAR(36) NOT NULL,
    PRIMARY KEY (hallazgo_id, institucion_alt),
    CONSTRAINT fk_hi_hallazgo FOREIGN KEY (hallazgo_id) REFERENCES hallazgo (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================================================
-- Semilla PROVISIONAL de referencias.
-- PROVISIONAL: reemplazar por el listado oficial DDN. Los alt_key son fijos
-- para facilitar el desarrollo; en produccion se generan al crear via API.
-- =============================================================================
INSERT INTO categoria_ddn (alt_key, codigo, nombre, vigente, creado_en, actualizado_en) VALUES
  ('11111111-1111-4111-8111-000000000001', 'CAT-VIDA',        'Derecho a la vida y la integridad', 1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('11111111-1111-4111-8111-000000000002', 'CAT-EDUCACION',   'Derecho a la educacion',            1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('11111111-1111-4111-8111-000000000003', 'CAT-SALUD',       'Derecho a la salud',                1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('11111111-1111-4111-8111-000000000004', 'CAT-PROTECCION',  'Derecho a la proteccion',           1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('11111111-1111-4111-8111-000000000005', 'CAT-PARTICIPACION','Derecho a la participacion',       1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));

INSERT INTO proceso_ddn (alt_key, codigo, nombre, vigente, creado_en, actualizado_en) VALUES
  ('22222222-2222-4222-8222-000000000001', 'PROC-PROMOCION', 'Promocion de derechos',     1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('22222222-2222-4222-8222-000000000002', 'PROC-PROTECCION','Proteccion administrativa', 1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('22222222-2222-4222-8222-000000000003', 'PROC-ESTUDIOS',  'Estudios y gestion',        1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));

INSERT INTO instrumento (alt_key, codigo, nombre, vigente, creado_en, actualizado_en) VALUES
  ('33333333-3333-4333-8333-000000000001', 'INST-RECOMENDACION', 'Recomendacion institucional', 1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('33333333-3333-4333-8333-000000000002', 'INST-OFICIO',        'Oficio a organo del Estado',  1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6)),
  ('33333333-3333-4333-8333-000000000003', 'INST-INFORME',       'Informe tematico',            1, UTC_TIMESTAMP(6), UTC_TIMESTAMP(6));
