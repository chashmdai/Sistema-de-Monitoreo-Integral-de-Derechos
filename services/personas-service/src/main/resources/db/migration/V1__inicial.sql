-- ============================================================================
--  personas-service (6.2) · Migración inicial · SOLO ESQUEMA
--  Defensoría de los Derechos de la Niñez — Núcleo Fundacional SMID
-- ----------------------------------------------------------------------------
--  DECISIÓN DE DISEÑO VIGENTE (revierte README §5.6 / personas-README "Migración"):
--  el servicio ARRANCA VACÍO. Esta migración crea únicamente el esquema; NO hay
--  semilla de personas, NO hay carga ETL del padrón histórico (~27.000 registros
--  del SIGER legado) ni artefactos de cadena de custodia. El registro de personas
--  nace vacío y se puebla con el uso. El histórico permanece congelado en SIGER y
--  se consulta —si se requiere— a través de `siger-service` (servicio aparte tras
--  el Gateway); personas-service nunca lee ni escribe el legado.
--
--  Convenciones transversales aplicadas (Núcleo 2.2 / 2.6):
--   - id BIGINT AUTO_INCREMENT (privado) + alt_key CHAR(36) (público, cierre IDOR).
--   - utf8mb4 con colación utf8mb4_0900_ai_ci (búsquedas insensibles a tildes/mayúsc.).
--   - ENUM modelados como VARCHAR + CHECK (estabilidad frente a ddl-auto=validate,
--     idéntico a auth/catálogo ya construidos).
--   - Marcas temporales DATETIME(6) en UTC (coherencia binaria con auth/catálogo;
--     el Núcleo §2.6 las nombra TIMESTAMP, auth/catálogo las materializan DATETIME(6)).
--   - Referencias territoriales como alt_key CHAR(36): personas-service NO posee
--     tablas sede/unidad (viven en Identidad 6.1, base separada) y el token sólo
--     transporta alt_key UUID (§2.4). Se añade id_unidad (no presente en §5.3) para
--     habilitar el filtro de alcance UNIDAD exigido por §2.3.
-- ============================================================================

-- ----------------------------------------------------------------------------
--  Tabla `persona` — registro maestro de sujetos de derechos y demás personas
--  de casos (NNA, adultos requirentes, personas jurídicas, testigos).
--  El personal interno (funcionarios) NO vive aquí: vive en Identidad (6.1).
-- ----------------------------------------------------------------------------
CREATE TABLE persona (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key           CHAR(36)     NOT NULL,                 -- identificador público opaco (UUID)
    tipo              VARCHAR(20)  NOT NULL,                 -- NNA | ADULTO | JURIDICA | TESTIGO
    rut               VARCHAR(12)  NULL,                     -- nulable (flexibilidad de ingreso); normalizado "cuerpo-DV"
    dv                CHAR(1)      NULL,                     -- dígito verificador (mayúscula), derivado del RUT
    nombres           VARCHAR(160) NULL,
    apellido_paterno  VARCHAR(120) NULL,
    apellido_materno  VARCHAR(120) NULL,
    razon_social      VARCHAR(200) NULL,                     -- solo tipo JURIDICA
    fecha_nacimiento  DATE         NULL,
    sexo              VARCHAR(20)  NULL,                     -- F | M | OTRO | NO_INFORMA
    nacionalidad      VARCHAR(60)  NULL,
    hash_dedup        VARCHAR(64)  NULL,                     -- clave de deduplicación difusa (SHA-256 hex del nombre normalizado + fecha)
    id_sede           CHAR(36)     NULL,                     -- alt_key de la sede de origen del registro (del token)
    id_unidad         CHAR(36)     NULL,                     -- alt_key de la unidad de origen del registro (del token)
    vigente           TINYINT(1)   NOT NULL DEFAULT 1,       -- borrado lógico (nunca físico)
    creado_en         DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    actualizado_en    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    creado_por        CHAR(36)     NULL,                     -- alt_key del usuario creador (claim `sub`)
    CONSTRAINT pk_persona PRIMARY KEY (id),
    CONSTRAINT uk_persona_alt_key UNIQUE (alt_key),
    -- En MySQL una clave UNIQUE admite múltiples NULL: muchos NNA sin RUT coexisten,
    -- pero dos RUT iguales se rechazan a nivel de motor (defensa en profundidad).
    CONSTRAINT uk_persona_rut UNIQUE (rut),
    CONSTRAINT ck_persona_tipo CHECK (tipo IN ('NNA','ADULTO','JURIDICA','TESTIGO')),
    CONSTRAINT ck_persona_sexo CHECK (sexo IS NULL OR sexo IN ('F','M','OTRO','NO_INFORMA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Índices de soporte: filtros territoriales (§2.3), deduplicación difusa y búsqueda.
CREATE INDEX idx_persona_dedup     ON persona (hash_dedup);
CREATE INDEX idx_persona_sede      ON persona (id_sede);
CREATE INDEX idx_persona_unidad    ON persona (id_unidad);
CREATE INDEX idx_persona_apellido  ON persona (apellido_paterno);   -- "blocking" por prefijo de apellido
CREATE INDEX idx_persona_fechanac  ON persona (fecha_nacimiento);   -- "blocking" por fecha de nacimiento
CREATE INDEX idx_persona_vigente   ON persona (vigente);

-- ----------------------------------------------------------------------------
--  Tabla `persona_contacto` — datos de contacto (teléfono, email, dirección).
--  Forma parte del agregado Persona; sin datos de relato ni antecedentes (eso es
--  Vulneraciones 6.5, fuera de este núcleo).
-- ----------------------------------------------------------------------------
CREATE TABLE persona_contacto (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    id_persona  BIGINT       NOT NULL,
    tipo        VARCHAR(20)  NOT NULL,                       -- TELEFONO | EMAIL | DIRECCION
    valor       VARCHAR(240) NOT NULL,
    CONSTRAINT pk_persona_contacto PRIMARY KEY (id),
    CONSTRAINT fk_contacto_persona FOREIGN KEY (id_persona) REFERENCES persona (id),
    CONSTRAINT ck_contacto_tipo CHECK (tipo IN ('TELEFONO','EMAIL','DIRECCION'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX idx_contacto_persona ON persona_contacto (id_persona);
