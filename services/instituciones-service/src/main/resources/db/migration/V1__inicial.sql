-- ============================================================================
--  instituciones-service (SMID 6.10) — Esquema inicial
--  Defensoría de los Derechos de la Niñez
-- ----------------------------------------------------------------------------
--  Flyway es el ÚNICO dueño del esquema. Hibernate solo valida (ddl-auto=validate);
--  si las entidades y estas tablas divergen, el servicio NO arranca (falla rápido).
--
--  OVERRIDES ARQUITECTÓNICOS APLICADOS EN ESTE ESQUEMA
--  --------------------------------------------------------------------------
--   #1  Arranque limpio: sin migración/ETL del legado; costura SIGER futura.
--   #2  IDs públicos solo alt_key (VARCHAR(36) UNIQUE); la PK BIGINT interna
--       nunca cruza la frontera del servicio.
--   #3  Enumerados como VARCHAR(N) + CHECK (nunca ENUM nativo de MySQL).
--   #4  Marcas temporales DATETIME(6) en UTC (hibernate.jdbc.time_zone=UTC).
--   #5  Booleanos como TINYINT(1) (tinyInt1isBit=true en la URL JDBC).
--   #6  DESVIACIÓN DE ALCANCE: las instituciones son DATOS DE REFERENCIA
--       NACIONALES. NO hay columnas territoriales (id_sede/id_unidad) ni filtro
--       por sede/unidad: las lecturas son globales para cualquier usuario
--       autenticado. Las escrituras exigen rol administrador (se controla en la
--       capa de dominio, no en el esquema).
--   FK por PK interna escalar (sin relación JPA bidireccional):
--       institucion.tipo_id     -> tipo_institucion(id)
--       punto_focal.institucion_id -> institucion(id)
--
--  Motor InnoDB, charset utf8mb4, collation utf8mb4_0900_ai_ci (acentos y
--  mayúsculas insensibles: habilita unicidad "normalizada" de nombres).
-- ============================================================================

-- ----------------------------------------------------------------------------
-- TIPO DE INSTITUCIÓN (catálogo de clasificación, plano; sin máquina de estados)
-- ----------------------------------------------------------------------------
CREATE TABLE tipo_institucion (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)   NOT NULL,
    nombre          VARCHAR(160)  NOT NULL,
    ambito          VARCHAR(20)   NOT NULL,
    descripcion     VARCHAR(500)  NULL,
    vigente         TINYINT(1)    NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)   NOT NULL,
    actualizado_en  DATETIME(6)   NOT NULL,
    CONSTRAINT pk_tipo_institucion PRIMARY KEY (id),
    CONSTRAINT uk_tipo_institucion_alt_key UNIQUE (alt_key),
    CONSTRAINT uk_tipo_institucion_nombre  UNIQUE (nombre),
    CONSTRAINT ck_tipo_institucion_ambito  CHECK (ambito IN
        ('JUDICIAL','SALUD','EDUCACION','PROTECCION','POLICIAL','MUNICIPAL','OTRO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX idx_tipo_institucion_ambito  ON tipo_institucion (ambito);
CREATE INDEX idx_tipo_institucion_vigente ON tipo_institucion (vigente);

-- ----------------------------------------------------------------------------
-- INSTITUCIÓN (raíz; activa/inactiva por bandera, sin máquina de estados)
-- ----------------------------------------------------------------------------
CREATE TABLE institucion (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)   NOT NULL,
    codigo          VARCHAR(60)   NULL,
    nombre          VARCHAR(200)  NOT NULL,
    tipo_id         BIGINT        NOT NULL,
    rut             VARCHAR(12)   NULL,
    dv              CHAR(1)       NULL,
    region_codigo   VARCHAR(10)   NULL,
    comuna_codigo   VARCHAR(10)   NULL,
    direccion       VARCHAR(240)  NULL,
    telefono        VARCHAR(40)   NULL,
    email           VARCHAR(160)  NULL,
    sitio_web       VARCHAR(200)  NULL,
    activa          TINYINT(1)    NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)   NOT NULL,
    actualizado_en  DATETIME(6)   NOT NULL,
    CONSTRAINT pk_institucion PRIMARY KEY (id),
    CONSTRAINT uk_institucion_alt_key UNIQUE (alt_key),
    -- 'codigo' es único SOLO entre los no nulos (MySQL admite múltiples NULL).
    CONSTRAINT uk_institucion_codigo  UNIQUE (codigo),
    CONSTRAINT fk_institucion_tipo
        FOREIGN KEY (tipo_id) REFERENCES tipo_institucion (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX idx_institucion_tipo    ON institucion (tipo_id);
CREATE INDEX idx_institucion_region  ON institucion (region_codigo);
CREATE INDEX idx_institucion_activa  ON institucion (activa);
CREATE INDEX idx_institucion_nombre  ON institucion (nombre);
CREATE INDEX idx_institucion_rut     ON institucion (rut);

-- ----------------------------------------------------------------------------
-- PUNTO FOCAL (contacto de una institución; hija por PK interna escalar)
-- Invariante de negocio (controlada en dominio dentro de la transacción):
--   a lo sumo UN punto focal principal activo por institución.
-- ----------------------------------------------------------------------------
CREATE TABLE punto_focal (
    id              BIGINT        NOT NULL AUTO_INCREMENT,
    alt_key         VARCHAR(36)   NOT NULL,
    institucion_id  BIGINT        NOT NULL,
    nombre          VARCHAR(160)  NOT NULL,
    cargo           VARCHAR(120)  NULL,
    email           VARCHAR(160)  NULL,
    telefono        VARCHAR(40)   NULL,
    principal       TINYINT(1)    NOT NULL DEFAULT 0,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,
    creado_en       DATETIME(6)   NOT NULL,
    actualizado_en  DATETIME(6)   NOT NULL,
    CONSTRAINT pk_punto_focal PRIMARY KEY (id),
    CONSTRAINT uk_punto_focal_alt_key UNIQUE (alt_key),
    CONSTRAINT fk_punto_focal_institucion
        FOREIGN KEY (institucion_id) REFERENCES institucion (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX idx_punto_focal_institucion ON punto_focal (institucion_id);
CREATE INDEX idx_punto_focal_principal   ON punto_focal (institucion_id, principal, activo);
