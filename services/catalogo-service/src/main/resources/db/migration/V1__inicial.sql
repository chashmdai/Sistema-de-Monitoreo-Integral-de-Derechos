-- ============================================================================
-- SMID 6.7 — Catálogo de Derechos · V1: esquema inicial
-- ----------------------------------------------------------------------------
-- Modelo de árbol como LISTA DE ADYACENCIA (id_padre -> derecho.id). El recorrido
-- jerárquico se resuelve con CTE recursivas (WITH RECURSIVE) de MySQL 8.
--
-- Colación utf8mb4_0900_ai_ci (acento- e insensible a mayúsculas): habilita la
-- búsqueda donde "educacion" encuentra "educación" sin funciones adicionales.
--
-- Flyway es el ÚNICO dueño del esquema; Hibernate corre en modo 'validate'.
-- ============================================================================

CREATE TABLE derecho (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key         CHAR(36)     NOT NULL,                       -- identificador público opaco (UUID)
    id_padre        BIGINT       NULL,                           -- FK al padre; NULL en las raíces
    codigo          VARCHAR(40)  NOT NULL,                       -- estable e inmutable, único global
    nombre          VARCHAR(200) NOT NULL,
    descripcion     VARCHAR(600) NULL,
    nivel           SMALLINT     NOT NULL,                       -- 0 = raíz; derivado del padre
    orden           SMALLINT     NOT NULL DEFAULT 0,             -- orden de despliegue entre hermanos
    vigente         TINYINT(1)   NOT NULL DEFAULT 1,             -- borrado lógico (1 = vigente)
    vigente_desde   DATE         NOT NULL,
    vigente_hasta   DATE         NULL,                           -- fecha de baja lógica
    creado_en       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_derecho_alt_key UNIQUE (alt_key),
    CONSTRAINT uk_derecho_codigo  UNIQUE (codigo),
    CONSTRAINT fk_derecho_padre   FOREIGN KEY (id_padre) REFERENCES derecho (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Índices de apoyo a los accesos frecuentes.
CREATE INDEX idx_derecho_padre   ON derecho (id_padre);      -- hijos directos / recorrido del árbol
CREATE INDEX idx_derecho_vigente ON derecho (vigente);       -- filtros por vigencia
CREATE INDEX idx_derecho_nombre  ON derecho (nombre);        -- búsqueda por nombre

CREATE TABLE causa (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    alt_key         CHAR(36)     NOT NULL,
    id_derecho      BIGINT       NOT NULL,                       -- derecho dueño
    codigo          VARCHAR(40)  NOT NULL,                       -- único dentro del derecho
    nombre          VARCHAR(200) NOT NULL,
    vigente         TINYINT(1)   NOT NULL DEFAULT 1,
    creado_en       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_causa_alt_key       UNIQUE (alt_key),
    CONSTRAINT uk_causa_derecho_codigo UNIQUE (id_derecho, codigo),  -- unicidad por derecho
    CONSTRAINT fk_causa_derecho       FOREIGN KEY (id_derecho) REFERENCES derecho (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX idx_causa_derecho ON causa (id_derecho);
