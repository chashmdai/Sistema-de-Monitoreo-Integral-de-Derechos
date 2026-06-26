-- ============================================================================
-- sgs-service (SMID legado IA) · Esquema inicial (V1)
-- ----------------------------------------------------------------------------
-- Generado desde la BD `db_sgs` existente (las 9 tablas del legado) con
-- mysqldump --no-data. Flyway pasa a ser dueño del esquema:
--   * BD vacía (equipo nuevo / docker)  -> Flyway crea las tablas con este V1.
--   * BD ya poblada (tu entorno actual)  -> baseline-on-migrate marca V1 como
--     aplicado SIN re-ejecutarlo, conservando tus datos.
-- FOREIGN_KEY_CHECKS=0 evita problemas de orden de creación (FKs cruzadas).
-- Para cambios futuros de esquema: agregar V2__*.sql, V3__*.sql, ...
-- ============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `sgs_accion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recomendacion_id` bigint NOT NULL,
  `orden` int NOT NULL,
  `descripcion` text,
  PRIMARY KEY (`id`),
  KEY `ix_accion_rec` (`recomendacion_id`),
  CONSTRAINT `fk_accion_rec` FOREIGN KEY (`recomendacion_id`) REFERENCES `sgs_recomendacion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_alerta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recomendacion_id` bigint NOT NULL,
  `tipo` varchar(30) NOT NULL,
  `fecha_limite` date NOT NULL,
  `fecha_generada` datetime(6) NOT NULL,
  `atendida` bit(1) NOT NULL DEFAULT b'0',
  `notificada_telegram` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `ix_alerta_rec` (`recomendacion_id`),
  KEY `ix_alerta_atendida` (`atendida`),
  CONSTRAINT `fk_alerta_rec` FOREIGN KEY (`recomendacion_id`) REFERENCES `sgs_recomendacion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_analisis_audit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `oficio_id` bigint DEFAULT NULL,
  `recomendacion_id` bigint DEFAULT NULL,
  `usuario_rut` varchar(50) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `tipo_analisis` varchar(20) NOT NULL,
  `modelo` varchar(80) DEFAULT NULL,
  `modelo_snapshot` varchar(120) DEFAULT NULL,
  `version_rubrica` varchar(80) DEFAULT NULL,
  `evaluacion_ia` varchar(60) DEFAULT NULL,
  `confianza_ia` double DEFAULT NULL,
  `tokens_prompt` int DEFAULT NULL,
  `tokens_completion` int DEFAULT NULL,
  `tokens_reasoning` int DEFAULT NULL,
  `costo_estimado` decimal(12,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_audit_oficio` (`oficio_id`),
  KEY `ix_audit_usuario` (`usuario_rut`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_audit_documento` (
  `audit_id` bigint NOT NULL,
  `nombre_archivo` varchar(255) DEFAULT NULL,
  `sha256` varchar(64) DEFAULT NULL,
  KEY `ix_audit_doc_audit` (`audit_id`),
  CONSTRAINT `fk_audit_doc_audit` FOREIGN KEY (`audit_id`) REFERENCES `sgs_analisis_audit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_catalogo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(30) NOT NULL,
  `codigo` varchar(80) NOT NULL,
  `etiqueta` varchar(255) NOT NULL,
  `activo` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_catalogo_tipo_codigo` (`tipo`,`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_job` (
  `job_id` varchar(36) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `payload_resultado` longtext,
  `error` text,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_oficio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nro_oficio` varchar(100) DEFAULT NULL,
  `region` varchar(120) DEFAULT NULL,
  `institucion` varchar(255) DEFAULT NULL,
  `residencia_centro` varchar(255) DEFAULT NULL,
  `nivel` varchar(100) DEFAULT NULL,
  `pdf_hash` varchar(64) DEFAULT NULL,
  `fecha_ingreso` datetime(6) DEFAULT NULL,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_oficio_nro` (`nro_oficio`),
  KEY `ix_oficio_region` (`region`),
  KEY `ix_oficio_institucion` (`institucion`),
  KEY `ix_oficio_fecha` (`fecha_ingreso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_recomendacion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `oficio_id` bigint NOT NULL,
  `correlativo` varchar(50) DEFAULT NULL,
  `dimension` varchar(150) DEFAULT NULL,
  `nudo_critico` text,
  `tipo_recomendacion` varchar(200) DEFAULT NULL,
  `verbo` varchar(200) DEFAULT NULL,
  `descripcion` text,
  `plazo` varchar(30) DEFAULT NULL,
  `plazo_raw` varchar(120) DEFAULT NULL,
  `gv` bit(1) NOT NULL DEFAULT b'0',
  `acoge` varchar(5) DEFAULT NULL,
  `estado` varchar(30) NOT NULL,
  `materia_id` bigint DEFAULT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `profesional_responsable` varchar(150) DEFAULT NULL,
  `responsable_seguimiento` varchar(150) DEFAULT NULL,
  `anulado` bit(1) NOT NULL DEFAULT b'0',
  `motivo_anulacion` text,
  `anulado_por` varchar(50) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_rec_oficio` (`oficio_id`),
  KEY `ix_rec_estado` (`estado`),
  KEY `ix_rec_gv` (`gv`),
  KEY `ix_rec_plazo` (`plazo`),
  KEY `ix_rec_materia` (`materia_id`),
  KEY `ix_rec_categoria` (`categoria_id`),
  CONSTRAINT `fk_rec_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `sgs_catalogo` (`id`),
  CONSTRAINT `fk_rec_materia` FOREIGN KEY (`materia_id`) REFERENCES `sgs_catalogo` (`id`),
  CONSTRAINT `fk_rec_oficio` FOREIGN KEY (`oficio_id`) REFERENCES `sgs_oficio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `sgs_seguimiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recomendacion_id` bigint NOT NULL,
  `fase` varchar(60) DEFAULT NULL,
  `fecha_seguimiento` date DEFAULT NULL,
  `tipo_seguimiento_id` bigint DEFAULT NULL,
  `tipo_respuesta_id` bigint DEFAULT NULL,
  `fecha_respuesta` date DEFAULT NULL,
  `otro_seguimiento_inst` varchar(500) DEFAULT NULL,
  `evaluacion_ia` varchar(60) DEFAULT NULL,
  `valoracion_rubrica` text,
  `confianza` double DEFAULT NULL,
  `razonamiento` text,
  `requiere_revision_humana` bit(1) NOT NULL DEFAULT b'0',
  `evaluacion_final` varchar(60) DEFAULT NULL,
  `evaluacion_final_autor` varchar(50) DEFAULT NULL,
  `evaluacion_final_fecha` datetime(6) DEFAULT NULL,
  `responsable_seguimiento` varchar(150) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_seg_rec` (`recomendacion_id`),
  KEY `ix_seg_fecha` (`fecha_seguimiento`),
  KEY `ix_seg_tipo_seg` (`tipo_seguimiento_id`),
  KEY `ix_seg_tipo_resp` (`tipo_respuesta_id`),
  CONSTRAINT `fk_seg_rec` FOREIGN KEY (`recomendacion_id`) REFERENCES `sgs_recomendacion` (`id`),
  CONSTRAINT `fk_seg_tipo_resp` FOREIGN KEY (`tipo_respuesta_id`) REFERENCES `sgs_catalogo` (`id`),
  CONSTRAINT `fk_seg_tipo_seg` FOREIGN KEY (`tipo_seguimiento_id`) REFERENCES `sgs_catalogo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
