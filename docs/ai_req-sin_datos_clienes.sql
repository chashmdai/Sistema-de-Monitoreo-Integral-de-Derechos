-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.7.29-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura para tabla ai_defensoria_req_qa.archivo_adjunto
DROP TABLE IF EXISTS `archivo_adjunto`;
CREATE TABLE IF NOT EXISTS `archivo_adjunto` (
  `id_archivo_adjunto` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT NULL,
  `fecha_de_emision` datetime DEFAULT NULL,
  `glosa` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `mime_type` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre_norm` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `path` longtext COLLATE latin1_spanish_ci,
  `publicado` int(11) DEFAULT NULL,
  `referencia` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `size_bytes` bigint(20) NOT NULL,
  `temporal` int(11) NOT NULL,
  `url_externa` longtext COLLATE latin1_spanish_ci,
  `id_persona_creador` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_archivo_adjunto`),
  UNIQUE KEY `UK_lluonpvj8l47u9gja32qfat3o` (`alt_key`),
  KEY `FKqyn65yy8vl3wphgrvhojqrdb9` (`id_persona_creador`),
  CONSTRAINT `FKqyn65yy8vl3wphgrvhojqrdb9` FOREIGN KEY (`id_persona_creador`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.archivo_adjunto: ~0 rows (aproximadamente)
DELETE FROM `archivo_adjunto`;
/*!40000 ALTER TABLE `archivo_adjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `archivo_adjunto` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.atributo_sesion
DROP TABLE IF EXISTS `atributo_sesion`;
CREATE TABLE IF NOT EXISTS `atributo_sesion` (
  `id_atributo` int(11) NOT NULL DEFAULT '0',
  `nombre` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `valor_por_defecto` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `todos_los_usuarios` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_atributo`),
  UNIQUE KEY `id_atributo` (`nombre`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.atributo_sesion: 0 rows
DELETE FROM `atributo_sesion`;
/*!40000 ALTER TABLE `atributo_sesion` DISABLE KEYS */;
/*!40000 ALTER TABLE `atributo_sesion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.bienvenida
DROP TABLE IF EXISTS `bienvenida`;
CREATE TABLE IF NOT EXISTS `bienvenida` (
  `id_bienvenida` tinyint(3) NOT NULL DEFAULT '0',
  `tipo_reporte` tinyint(3) unsigned DEFAULT '0',
  `id_reporte` int(11) unsigned DEFAULT '0',
  `Comentario` varchar(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_bienvenida`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.bienvenida: 1 rows
DELETE FROM `bienvenida`;
/*!40000 ALTER TABLE `bienvenida` DISABLE KEYS */;
INSERT INTO `bienvenida` (`id_bienvenida`, `tipo_reporte`, `id_reporte`, `Comentario`) VALUES
	(4, 0, 2, '');
/*!40000 ALTER TABLE `bienvenida` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.calendar
DROP TABLE IF EXISTS `calendar`;
CREATE TABLE IF NOT EXISTS `calendar` (
  `id_calendar` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_categoria` int(10) unsigned DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `temp_key` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_calendar`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla ai_defensoria_req_qa.calendar: 0 rows
DELETE FROM `calendar`;
/*!40000 ALTER TABLE `calendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.calendar_evento
DROP TABLE IF EXISTS `calendar_evento`;
CREATE TABLE IF NOT EXISTS `calendar_evento` (
  `id_calendar_evento` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_calendar` int(10) unsigned NOT NULL DEFAULT '0',
  `subject` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `description` text COLLATE latin1_spanish_ci,
  `owner` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `due_date` varchar(60) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` date DEFAULT NULL,
  `priority` int(10) unsigned DEFAULT '4',
  `temp_key` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_calendar_evento`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.calendar_evento: 0 rows
DELETE FROM `calendar_evento`;
/*!40000 ALTER TABLE `calendar_evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar_evento` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.cargo
DROP TABLE IF EXISTS `cargo`;
CREATE TABLE IF NOT EXISTS `cargo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activo` int(11) NOT NULL,
  `cargo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.cargo: ~0 rows (aproximadamente)
DELETE FROM `cargo`;
/*!40000 ALTER TABLE `cargo` DISABLE KEYS */;
/*!40000 ALTER TABLE `cargo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.carpeta_docs
DROP TABLE IF EXISTS `carpeta_docs`;
CREATE TABLE IF NOT EXISTS `carpeta_docs` (
  `id_carpeta` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_categoria_padre` int(10) unsigned DEFAULT '0',
  `xml_data` text COLLATE latin1_spanish_ci,
  `temp_key` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `id_grupo_editor` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id_carpeta`),
  UNIQUE KEY `id_carpeta` (`id_carpeta`,`temp_key`),
  KEY `temp_key` (`temp_key`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.carpeta_docs: 1 rows
DELETE FROM `carpeta_docs`;
/*!40000 ALTER TABLE `carpeta_docs` DISABLE KEYS */;
INSERT INTO `carpeta_docs` (`id_carpeta`, `id_categoria_padre`, `xml_data`, `temp_key`, `id_grupo_editor`) VALUES
	(15, 222, '<carpeta><titulo>Videos tutoriales para el registro y derivación de requerimientos</titulo><desc></desc><nombre>videos_requerimientos</nombre></carpeta>', '34T3FAA6AJKHHOM', 0);
/*!40000 ALTER TABLE `carpeta_docs` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.carpeta_docs_recurso
DROP TABLE IF EXISTS `carpeta_docs_recurso`;
CREATE TABLE IF NOT EXISTS `carpeta_docs_recurso` (
  `id_recurso` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_carpeta` int(10) unsigned DEFAULT '0',
  `correlativo` int(5) unsigned DEFAULT '0',
  `id_tipo_recurso` int(5) unsigned NOT NULL DEFAULT '0',
  `titulo_recurso` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `desc_recurso` text COLLATE latin1_spanish_ci,
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_ultima_modificacion` datetime DEFAULT NULL,
  `nombre_archivo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `mime_type` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `temp_key` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `tamano_archivo` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id_recurso`),
  UNIQUE KEY `id_recurso` (`id_recurso`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.carpeta_docs_recurso: 5 rows
DELETE FROM `carpeta_docs_recurso`;
/*!40000 ALTER TABLE `carpeta_docs_recurso` DISABLE KEYS */;
INSERT INTO `carpeta_docs_recurso` (`id_recurso`, `id_carpeta`, `correlativo`, `id_tipo_recurso`, `titulo_recurso`, `desc_recurso`, `fecha_ingreso`, `fecha_ultima_modificacion`, `nombre_archivo`, `mime_type`, `temp_key`, `tamano_archivo`) VALUES
	(17, 15, 5, 1, 'Video 5 - Buscador de requerimientos', 'Explica la operación del buscador de requerimientos registrados.', '2021-02-09', '2021-02-09 17:07:21', 'Video05_-_Buscador_de_requerimientos.mp4', 'video/mp4', 'S8NCM5HD65J2XHM', 9601922);
INSERT INTO `carpeta_docs_recurso` (`id_recurso`, `id_carpeta`, `correlativo`, `id_tipo_recurso`, `titulo_recurso`, `desc_recurso`, `fecha_ingreso`, `fecha_ultima_modificacion`, `nombre_archivo`, `mime_type`, `temp_key`, `tamano_archivo`) VALUES
	(16, 15, 4, 1, 'Video 4 - Admisibilidad de requerimientos', 'Revisa el proceso de admisibilidad de los requerimientos enviados a las unidades.', '2021-02-09', '2021-02-09 17:06:10', 'Video04_-_Admisibilidad.mp4', 'video/mp4', 'AH7FZACQN8BO6HI', 19579993);
INSERT INTO `carpeta_docs_recurso` (`id_recurso`, `id_carpeta`, `correlativo`, `id_tipo_recurso`, `titulo_recurso`, `desc_recurso`, `fecha_ingreso`, `fecha_ultima_modificacion`, `nombre_archivo`, `mime_type`, `temp_key`, `tamano_archivo`) VALUES
	(15, 15, 3, 1, 'Video 3 - Respuesta inmediata al requerimiento desde recepción.', 'Explica la opción de respuesta inmediata al requerimiento, SIN enviarlo a una unidad.', '2021-02-09', '2021-02-09 16:28:03', 'Video03_-_Respuesta_inmediata_al_requerimiento_desde_recepcion.mp4', 'video/mp4', '42UYC8578MET2YY', 4178686);
INSERT INTO `carpeta_docs_recurso` (`id_recurso`, `id_carpeta`, `correlativo`, `id_tipo_recurso`, `titulo_recurso`, `desc_recurso`, `fecha_ingreso`, `fecha_ultima_modificacion`, `nombre_archivo`, `mime_type`, `temp_key`, `tamano_archivo`) VALUES
	(13, 15, 1, 1, 'Video 1 - Ingresando requerimientos', 'Revisión breve del entorno y registro de nuevos requerimientos.', '2021-02-09', '2021-02-09 16:08:31', 'Video01_-_Ingreso_nuevos_requerimientos.mp4', 'video/mp4', 'D9TNOSPPJB46XNE', 10477050);
INSERT INTO `carpeta_docs_recurso` (`id_recurso`, `id_carpeta`, `correlativo`, `id_tipo_recurso`, `titulo_recurso`, `desc_recurso`, `fecha_ingreso`, `fecha_ultima_modificacion`, `nombre_archivo`, `mime_type`, `temp_key`, `tamano_archivo`) VALUES
	(14, 15, 2, 1, 'Video 2 - Ingreso de nuevos requerimientos con NNAs registrados.', 'Explica cómo buscar primero al NNA, antes de crear el requerimiento.', '2021-02-09', '2021-02-09 16:10:09', 'Video02_-_Ingreso_nuevos_requerimientos_con_NNAs_registrados.mp4', 'video/mp4', 'SQYUS3XY89245CA', 4868867);
/*!40000 ALTER TABLE `carpeta_docs_recurso` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.carpeta_tipo_recurso
DROP TABLE IF EXISTS `carpeta_tipo_recurso`;
CREATE TABLE IF NOT EXISTS `carpeta_tipo_recurso` (
  `id_tipo_recurso` int(3) unsigned DEFAULT '0',
  `desc_tipo_recurso` varchar(50) COLLATE latin1_spanish_ci DEFAULT '0',
  `prioridad` int(5) unsigned DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.carpeta_tipo_recurso: 1 rows
DELETE FROM `carpeta_tipo_recurso`;
/*!40000 ALTER TABLE `carpeta_tipo_recurso` DISABLE KEYS */;
INSERT INTO `carpeta_tipo_recurso` (`id_tipo_recurso`, `desc_tipo_recurso`, `prioridad`) VALUES
	(1, 'Instructivo', 1);
/*!40000 ALTER TABLE `carpeta_tipo_recurso` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.categoria
DROP TABLE IF EXISTS `categoria`;
CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` int(11) NOT NULL DEFAULT '0',
  `descr` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `orden` int(11) DEFAULT NULL,
  `orden_temp` int(11) DEFAULT NULL,
  `id_padre` int(11) DEFAULT '0',
  `id_reporte_ref` int(11) DEFAULT NULL,
  `publico` int(11) DEFAULT NULL,
  `owner` varchar(20) COLLATE latin1_spanish_ci DEFAULT NULL,
  `invisible_con_login` int(1) unsigned DEFAULT '0',
  `id_imagen_asociada` int(5) unsigned DEFAULT '0',
  `id_plantilla` int(10) unsigned DEFAULT '0',
  `id_security_manager` int(5) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_categoria`),
  KEY `Orden` (`orden`),
  KEY `padre_descr` (`id_padre`,`descr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.categoria: 23 rows
DELETE FROM `categoria`;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(1, 'Sección Principal', 0, 0, 0, 0, 1, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(3, 'Inicio', 4, 3, 1, 0, 1, 'root', 1, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(222, 'Ayuda', 5, 4, 1, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(649, 'Requerimientos', 0, 0, 1, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(650, 'Ingreso y derivación', -1, 0, 649, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(654, 'Admin', 3, 2, 1, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(688, 'Requerimientos y casos admisibles', 1, 2, 649, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(693, 'Punto focal', 1, 1, 654, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(696, 'Usuarios', 0, 0, 654, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(698, 'Antecedentes y hallazgos', 1, 1, 1, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(699, 'Registro antecedentes', 0, 0, 698, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(704, 'Evaluación', 1, 1, 698, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(709, 'Tablas de requerimientos', 3, 2, 654, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(712, 'Hallazgos', 3, 2, 698, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(722, 'Notificaciones', 4, 3, 654, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(726, 'Reportes', 2, 5, 1, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(727, 'Requerimientos', 0, 0, 726, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(728, 'Descargas', 2, 1, 726, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(729, 'Casos', 1, 2, 726, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(741, 'Indicadores', 10, NULL, 726, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(743, 'Hallazgos', 11, NULL, 726, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(746, 'Interno ALK', 5, 4, 654, 0, 0, 'root', 0, 0, 0, 0);
INSERT INTO `categoria` (`id_categoria`, `descr`, `orden`, `orden_temp`, `id_padre`, `id_reporte_ref`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `id_plantilla`, `id_security_manager`) VALUES
	(773, 'Tablas generales', 2, 5, 654, 0, 0, 'root', 0, 0, 0, 0);
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.category_access
DROP TABLE IF EXISTS `category_access`;
CREATE TABLE IF NOT EXISTS `category_access` (
  `id_category` int(11) unsigned NOT NULL DEFAULT '0',
  `id_group` int(11) unsigned DEFAULT '0',
  `id_user` int(11) unsigned DEFAULT '0',
  `access` tinyint(3) unsigned NOT NULL DEFAULT '2'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.category_access: 0 rows
DELETE FROM `category_access`;
/*!40000 ALTER TABLE `category_access` DISABLE KEYS */;
/*!40000 ALTER TABLE `category_access` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.centro_resp_grupo
DROP TABLE IF EXISTS `centro_resp_grupo`;
CREATE TABLE IF NOT EXISTS `centro_resp_grupo` (
  `tipo` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `id_unidad_ref` int(11) NOT NULL,
  `id_unidad` int(11) NOT NULL,
  PRIMARY KEY (`id_unidad_ref`,`id_unidad`,`tipo`),
  KEY `FKkcaqperq4169wfwbncf2lny16` (`id_unidad`),
  CONSTRAINT `FK3o8qmj55xce03sy12rew4ldpy` FOREIGN KEY (`id_unidad_ref`) REFERENCES `unidad` (`id`),
  CONSTRAINT `FKkcaqperq4169wfwbncf2lny16` FOREIGN KEY (`id_unidad`) REFERENCES `unidad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.centro_resp_grupo: ~0 rows (aproximadamente)
DELETE FROM `centro_resp_grupo`;
/*!40000 ALTER TABLE `centro_resp_grupo` DISABLE KEYS */;
/*!40000 ALTER TABLE `centro_resp_grupo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.changelog
DROP TABLE IF EXISTS `changelog`;
CREATE TABLE IF NOT EXISTS `changelog` (
  `ID` decimal(20,0) NOT NULL,
  `APPLIED_AT` varchar(25) COLLATE latin1_spanish_ci NOT NULL,
  `DESCRIPTION` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.changelog: ~5 rows (aproximadamente)
DELETE FROM `changelog`;
/*!40000 ALTER TABLE `changelog` DISABLE KEYS */;
INSERT INTO `changelog` (`ID`, `APPLIED_AT`, `DESCRIPTION`) VALUES
	(20130506174139, '2015-03-23 15:15:44', 'create changelog');
INSERT INTO `changelog` (`ID`, `APPLIED_AT`, `DESCRIPTION`) VALUES
	(20130509202542, '2015-03-23 15:15:45', 'upgrade');
INSERT INTO `changelog` (`ID`, `APPLIED_AT`, `DESCRIPTION`) VALUES
	(20130619141122, '2015-03-23 15:15:45', 'update');
INSERT INTO `changelog` (`ID`, `APPLIED_AT`, `DESCRIPTION`) VALUES
	(20130624142036, '2015-03-23 15:15:46', 'alter notificacion user');
INSERT INTO `changelog` (`ID`, `APPLIED_AT`, `DESCRIPTION`) VALUES
	(20131003181529, '2015-03-23 15:15:46', 'drop cas');
/*!40000 ALTER TABLE `changelog` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.clasificacion_replet
DROP TABLE IF EXISTS `clasificacion_replet`;
CREATE TABLE IF NOT EXISTS `clasificacion_replet` (
  `id_clasificacion_replet` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_clasificacion_replet` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_clasificacion_replet`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.clasificacion_replet: 0 rows
DELETE FROM `clasificacion_replet`;
/*!40000 ALTER TABLE `clasificacion_replet` DISABLE KEYS */;
/*!40000 ALTER TABLE `clasificacion_replet` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.clientes_api_rest
DROP TABLE IF EXISTS `clientes_api_rest`;
CREATE TABLE IF NOT EXISTS `clientes_api_rest` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_key` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `cliente` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_clientes_api_rest_api_key` (`api_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.clientes_api_rest: ~0 rows (aproximadamente)
DELETE FROM `clientes_api_rest`;
/*!40000 ALTER TABLE `clientes_api_rest` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientes_api_rest` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.conexiones_db
DROP TABLE IF EXISTS `conexiones_db`;
CREATE TABLE IF NOT EXISTS `conexiones_db` (
  `id_base_datos` int(3) NOT NULL DEFAULT '0',
  `desc_base_datos` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_base_datos`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.conexiones_db: 0 rows
DELETE FROM `conexiones_db`;
/*!40000 ALTER TABLE `conexiones_db` DISABLE KEYS */;
/*!40000 ALTER TABLE `conexiones_db` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.configuracion
DROP TABLE IF EXISTS `configuracion`;
CREATE TABLE IF NOT EXISTS `configuracion` (
  `ambiente` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `valor` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`ambiente`,`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.configuracion: ~0 rows (aproximadamente)
DELETE FROM `configuracion`;
/*!40000 ALTER TABLE `configuracion` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.config_param
DROP TABLE IF EXISTS `config_param`;
CREATE TABLE IF NOT EXISTS `config_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo_valor` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `valor_default_string` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `valor_default_boolean` bit(1) DEFAULT NULL,
  `valor_default_integer` int(11) DEFAULT NULL,
  `valor_default_big_decimal` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_param_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.config_param: ~0 rows (aproximadamente)
DELETE FROM `config_param`;
/*!40000 ALTER TABLE `config_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_param` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.config_param_value
DROP TABLE IF EXISTS `config_param_value`;
CREATE TABLE IF NOT EXISTS `config_param_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_config` bigint(20) NOT NULL,
  `ambiente` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `tipo_valor` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `valor_string` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `valor_boolean` bit(1) DEFAULT NULL,
  `valor_integer` int(11) DEFAULT NULL,
  `valor_big_decimal` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_param_value_config_ambiente` (`id_config`,`ambiente`),
  CONSTRAINT `fk_config_param_value_config_param` FOREIGN KEY (`id_config`) REFERENCES `config_param` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.config_param_value: ~0 rows (aproximadamente)
DELETE FROM `config_param_value`;
/*!40000 ALTER TABLE `config_param_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_param_value` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.databasechangelog
DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `AUTHOR` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `FILENAME` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) COLLATE latin1_spanish_ci NOT NULL,
  `MD5SUM` varchar(35) COLLATE latin1_spanish_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `COMMENTS` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `TAG` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `LIQUIBASE` varchar(20) COLLATE latin1_spanish_ci DEFAULT NULL,
  `CONTEXTS` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `LABELS` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.databasechangelog: ~53 rows (aproximadamente)
DELETE FROM `databasechangelog`;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160411_alter_notificacion_tipo', 'arivera', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-06-23 19:55:23', 1, 'EXECUTED', '7:9c6d6693d5051c9525218908591b5bb7', 'addColumn', '', NULL, '3.4.2', NULL, NULL, NULL);
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160414_alter_replet_sql_rango', 'arivera', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-06-23 19:55:23', 2, 'EXECUTED', '7:a84572e88bd40e80f27d96c468bb9c66', 'addColumn (x2)', '', NULL, '3.4.2', NULL, NULL, NULL);
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160705_alter_replet_sql_columna', 'arivera', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-09-29 14:25:45', 3, 'EXECUTED', '7:d9c31cccf7de422c76d4fda92b5e311a', 'addColumn tableName=replet_sql_columna', '', NULL, '3.5.1', NULL, NULL, '5173545711');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160802_alter_replet_sql_columna', 'arivera', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-09-29 14:25:45', 4, 'EXECUTED', '7:f88420a61725b39624bd87697286ae69', 'addColumn tableName=replet_sql_columna', '', NULL, '3.5.1', NULL, NULL, '5173545711');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160704_alter_table_user_default_value_fecha_modificacion', 'cacuna', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-09-29 14:25:45', 5, 'EXECUTED', '7:fac12b9489513e60d95780719aad8d56', 'sql', '', NULL, '3.5.1', NULL, NULL, '5173545711');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160930_table_user_default_value_fecha_modificacion', 'abahamondes', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-10-26 02:53:07', 6, 'EXECUTED', '7:d1096b46e4f52f2c235db350f73caf56', 'sql', '', NULL, '3.5.1', NULL, NULL, '7428212917');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160930_largo_replet_param_sql', 'abahamondes', 'db-changelog-4.2.4-SNAPSHOT.xml', '2016-10-26 02:53:08', 7, 'EXECUTED', '7:833df37f0104833d801696a6c00e143c', 'sql', '', NULL, '3.5.1', NULL, NULL, '7428212917');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20161227_alter_replet_sql_columna', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-01-10 19:58:10', 8, 'EXECUTED', '7:af7c8f6c39d2f61e89e3e00a2b5b5209', 'addColumn tableName=replet_sql_columna', '', NULL, '3.5.1', NULL, NULL, '4056022042');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20161228_create_table_notificacion_attachment', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-05 10:09:57', 9, 'EXECUTED', '7:a2e68e9b161304fbaa6f46270f2995b2', 'createTable tableName=notificacion_attachment', '', NULL, '3.5.1', NULL, NULL, '9278317973');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20161228_alter_table_notificacion', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-05 10:09:58', 10, 'EXECUTED', '7:ff7e88a62e630ec1a5222ed0aa4deca1', 'addColumn tableName=notificacion', '', NULL, '3.5.1', NULL, NULL, '9278317973');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20160411_try_alter_notificacion_tipo', 'arivera', 'db-changelog-4.2.4-SNAPSHOT.xml', '2017-07-17 14:12:12', 11, 'EXECUTED', '7:ad2e0c6987337a8ed326fa6f433a0fa0', 'addColumn tableName=notificacion_tipo', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170712_alter_table_replet_sql', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:12', 12, 'EXECUTED', '7:35d86e00c7a154c804ae19bdd24e0d41', 'addColumn tableName=replet_sql', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170712_update_replet_sql_query1_query2', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:12', 13, 'EXECUTED', '7:48325403e6463f219fa7161e0dbbb3f2', 'sql', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170712_alter_table_replet_param', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:12', 14, 'EXECUTED', '7:33233e0220aca9687acb47378b3917c7', 'addColumn tableName=replet_param', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170712_update_replet_param_nulo_no_nulo', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:12', 15, 'EXECUTED', '7:18f035bd410d22ea67f9c25f8d462841', 'sql', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170713_alter_table_selector', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:13', 16, 'EXECUTED', '7:eac2c829c1e1f2d571a916d723fd8fd1', 'addColumn tableName=selector', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170713_alter_table_replet_sql_multi_query', 'frodriguez', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-07-17 14:12:13', 17, 'EXECUTED', '7:b53b912a865380ab7691d177a56d3610', 'addColumn tableName=replet_sql', '', NULL, '3.5.1', NULL, NULL, '0318184439');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170801_update_replet_sql_columna', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-08-17 09:20:52', 18, 'EXECUTED', '7:f527f0e9627c51674b2f8fa5f5b78d6d', 'sql', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170801_alter_replet_sql', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-08-17 09:20:52', 19, 'EXECUTED', '7:d87a4a69c2aef304816ec05125041592', 'addColumn tableName=replet_sql', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170801_update_replet_sql', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-08-17 09:20:52', 20, 'EXECUTED', '7:38944002c27787a5968c81f493bb3673', 'sql', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170801_alter_replet_param', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-08-17 09:20:52', 21, 'EXECUTED', '7:c2a213d9419a351ab2888f8deb4a44b8', 'addColumn tableName=replet_param', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170802_alter_table_url', 'arivera', 'db-changelog-4.3.3-SNAPSHOT.xml', '2017-08-17 09:20:52', 22, 'EXECUTED', '7:db9a8c4f6d12bc09458056ea44bec78b', 'sql', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170880_alter_notificacion', 'arivera', 'db-changelog-4.4.1.xml', '2017-08-17 09:20:53', 23, 'EXECUTED', '7:bf378d3dc32f4403617103566422f947', 'addColumn tableName=notificacion', '', NULL, '3.5.1', NULL, NULL, '2975974899');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170880_alter_plantilla', 'arivera', 'db-changelog-4.4.1.xml', '2017-08-17 09:24:47', 24, 'EXECUTED', '7:feb3cb96db91c079da6ccb5fed00269d', 'addColumn tableName=plantilla', '', NULL, '3.5.1', NULL, NULL, '2976209855');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170880_alter_notificacion_tipo', 'arivera', 'db-changelog-4.4.1.xml', '2017-08-17 09:24:47', 25, 'EXECUTED', '7:51bb2948a7876d2ea0a023fed952da2f', 'addColumn tableName=notificacion_tipo', '', NULL, '3.5.1', NULL, NULL, '2976209855');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170817_alter_plantilla', 'arivera', 'db-changelog-4.4.1.xml', '2017-08-21 15:20:29', 26, 'EXECUTED', '7:02f3795044933cc303251a2e5f595e76', 'sql', '', NULL, '3.5.1', NULL, NULL, '3343149778');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20170818_alter_replet_param', 'arivera', 'db-changelog-4.4.1.xml', '2017-08-21 15:20:29', 27, 'EXECUTED', '7:5b9e146aadf2a19c37f7da396ff32db3', 'addColumn tableName=replet_param', '', NULL, '3.5.1', NULL, NULL, '3343149778');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20171103_fix_bit_boolean_columns', 'arivera', 'db-changelog-4.4.1.xml', '2018-09-11 09:42:34', 28, 'EXECUTED', '7:a0e8a92248f3dc5bdd845d0862ebc7ef', 'sql', 'Corrige columnas de tipo BIT porque MySQL tiene un bug al usar este tipo de columnas como boolean.', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20180306_create_table_dir_config', 'arivera', 'db-changelog-4.4.1.xml', '2018-09-11 09:42:34', 29, 'EXECUTED', '7:8cf8ef2221e54a67edfdf81a575bc4aa', 'createTable tableName=directory_config', '', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20180509_alter_table_notificacion', 'arivera', 'db-changelog-4.4.3.xml', '2018-09-11 09:42:34', 30, 'EXECUTED', '7:83971cf92cfc77bc5b561269d1841cad', 'sql', '', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20180613_alter_table_notificacion', 'arivera', 'db-changelog-4.4.4.xml', '2018-09-11 09:42:34', 31, 'EXECUTED', '7:395f8fd7448d652bcb706a1ce86bbad3', 'addColumn tableName=notificacion', '', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20180613_update_table_notificacion', 'arivera', 'db-changelog-4.4.4.xml', '2018-09-11 09:42:34', 32, 'EXECUTED', '7:6f2857abf051ba8f7c31d887c3d4c034', 'sql', '', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20180904_alter_table_grupo', 'arivera', 'db-changelog-4.4.5.xml', '2018-09-11 09:42:34', 33, 'EXECUTED', '7:824d97a5c0561860a0b510d689244f2c', 'addColumn tableName=grupo', '', NULL, '3.5.1', NULL, NULL, '6669956517');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20181221_alter_table_notificacion_tipo', 'arivera', 'db-changelog-4.5.0.xml', '2019-05-22 10:23:33', 34, 'EXECUTED', '7:4ae98699cab3af59d29fef76816666ec', 'addColumn tableName=notificacion_tipo', '', NULL, '3.5.1', NULL, NULL, '8535013534');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190104_add_index_table_notificacion', 'arivera', 'db-changelog-4.5.0.xml', '2019-05-22 10:23:35', 35, 'EXECUTED', '7:eb43749f205782ec42654d1d63cc06fb', 'createIndex indexName=idx_notificacion_fecha_notificacion, tableName=notificacion; createIndex indexName=fki_notificacion_notificacion_tipo, tableName=notificacion', '', NULL, '3.5.1', NULL, NULL, '8535013534');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190114_notificacion_tipo_requiere_plantilla', 'abahamondes', 'db-changelog-4.5.0.xml', '2019-05-22 10:23:35', 36, 'EXECUTED', '7:12d5953620245dd2c4148bf910c46863', 'addColumn tableName=notificacion_tipo', '', NULL, '3.5.1', NULL, NULL, '8535013534');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190426_create_table_mensajes', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 37, 'EXECUTED', '7:2f9c2b2a204791baf2fcb9eaffc21b0a', 'createTable tableName=mensajes; addPrimaryKey constraintName=pk_mensajes, tableName=mensajes', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190429_create_table_config_param', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 38, 'EXECUTED', '7:427eac3a92acfeaf3e4e650adca5a904', 'createTable tableName=config_param', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190429_create_table_config_param_value', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 39, 'EXECUTED', '7:c9a0ba88d1dd6dd9ee821cf98ed1e9ec', 'createTable tableName=config_param_value; createIndex indexName=uk_config_param_value_config_ambiente, tableName=config_param_value; addForeignKeyConstraint baseTableName=config_param_value, constraintName=fk_config_param_value_config_param, refer...', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190502_alter_table_replet_param', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 40, 'EXECUTED', '7:9591f15f449450c6df410e7209c7f337', 'addColumn tableName=replet_param', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190503_create_table_tickets_acceso', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 41, 'EXECUTED', '7:96d612a9a5bc18ac94e3887aed0868b9', 'createTable tableName=tickets_acceso', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190503_create_table_clientes_api_rest', 'arivera', 'db-changelog-4.5.2.xml', '2019-06-13 16:06:22', 42, 'EXECUTED', '7:add9aa3f8f7514ea497bb3b4800f8d28', 'createTable tableName=clientes_api_rest; createIndex indexName=uk_clientes_api_rest_api_key, tableName=clientes_api_rest', '', NULL, '3.5.1', NULL, NULL, '0456367535');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190719_alter_table_notificacion', 'arivera', 'db-changelog-4.5.2.xml', '2019-07-31 10:02:48', 43, 'EXECUTED', '7:2faa3cc1dd933d2fd0b7fd0cc4346dc3', 'modifyDataType columnName=email_notificacion, tableName=notificacion; modifyDataType columnName=email_original, tableName=notificacion', '', NULL, '3.5.1', NULL, NULL, '4581768694');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190720_alter_table_user', 'arivera', 'db-changelog-4.5.2.xml', '2019-07-31 10:02:48', 44, 'EXECUTED', '7:41b23863b4b04751bd02717cbd8dd35b', 'dropNotNullConstraint columnName=password, tableName=user', '', NULL, '3.5.1', NULL, NULL, '4581768694');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190819_fix_default_fecha_table_replet', 'arivera', 'db-changelog-4.5.3.xml', '2019-09-24 17:34:22', 45, 'EXECUTED', '7:f93e499e6d1046d698474963e680bee6', 'sql', 'Corrige valor default mal asignado (0000-00-00) en columnas de fechas de la tabla replet.\n    Se tiene que hacer con sql porque liquibase no soporta multiples columnas.', NULL, '3.5.1', NULL, NULL, '9357344889');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190820_alter_table_replet', 'cacuna', 'db-changelog-4.5.3.xml', '2019-09-24 17:34:22', 46, 'EXECUTED', '7:a107e4e574a7dadaa0d1adfdfebd1803', 'addColumn tableName=replet', '', NULL, '3.5.1', NULL, NULL, '9357344889');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190909_alter_table_mensajes', 'arivera', 'db-changelog-4.5.3.xml', '2019-09-24 17:34:22', 47, 'EXECUTED', '7:fa7ae55eb85d71bbabc926e0da11cb2d', 'addColumn tableName=mensajes', '', NULL, '3.5.1', NULL, NULL, '9357344889');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20190909_update_table_mensajes', 'arivera', 'db-changelog-4.5.3.xml', '2019-09-24 17:34:22', 48, 'EXECUTED', '7:fb8b1627aa25e61cd8b221b47a9e5fd7', 'sql', '', NULL, '3.5.1', NULL, NULL, '9357344889');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20200610_agranda_nombre_grupo', 'abahamondes', 'db-changelog-4.5.6.xml', '2020-11-04 17:42:48', 49, 'EXECUTED', '7:b743a23c749d6b9162213d8058901f31', 'modifyDataType columnName=grupo, tableName=grupo', '', NULL, '3.5.1', NULL, NULL, '4522292196');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20201021_alter_table_user', 'arivera', 'db-changelog-4.5.6.xml', '2020-11-04 17:42:48', 50, 'EXECUTED', '7:1609028a31a7d84179ef1fede9e0a7aa', 'addColumn tableName=user', '', NULL, '3.5.1', NULL, NULL, '4522292196');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20201021_update_table_user', 'arivera', 'db-changelog-4.5.6.xml', '2020-11-04 17:42:48', 51, 'EXECUTED', '7:a3fcf1ee596d4539c6a269ba1cc0ae5d', 'sql', '', NULL, '3.5.1', NULL, NULL, '4522292196');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20201021_alter_table_user_num_intentos_login', 'arivera', 'db-changelog-4.5.6.xml', '2020-11-04 17:42:48', 52, 'EXECUTED', '7:f98a2da70d58ddff5053479bc2855024', 'addNotNullConstraint columnName=num_intentos_login, tableName=user', '', NULL, '3.5.1', NULL, NULL, '4522292196');
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('20201112_fix_user_num_intentos_login_default_constraint', 'arivera', 'db-changelog-4.5.6.xml', '2020-11-23 09:28:33', 53, 'EXECUTED', '7:7ca8b973565ff528475629aaae2e1f3c', 'addDefaultValue columnName=num_intentos_login, tableName=user', '', NULL, '3.5.1', NULL, NULL, '6134605699');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.databasechangeloglock
DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.databasechangeloglock: ~0 rows (aproximadamente)
DELETE FROM `databasechangeloglock`;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
	(1, b'0', NULL, NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.db_version
DROP TABLE IF EXISTS `db_version`;
CREATE TABLE IF NOT EXISTS `db_version` (
  `id_version` int(3) unsigned NOT NULL DEFAULT '0',
  `version_number` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id_version`),
  UNIQUE KEY `id_version` (`id_version`),
  KEY `id_version_2` (`id_version`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.db_version: 1 rows
DELETE FROM `db_version`;
/*!40000 ALTER TABLE `db_version` DISABLE KEYS */;
INSERT INTO `db_version` (`id_version`, `version_number`, `updated`) VALUES
	(2, 'Defensoría - Casos v1', '2021-02-14 13:00:00');
/*!40000 ALTER TABLE `db_version` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.delegacion
DROP TABLE IF EXISTS `delegacion`;
CREATE TABLE IF NOT EXISTS `delegacion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT NULL,
  `fecha_ult_modif` datetime DEFAULT NULL,
  `fin` date DEFAULT NULL,
  `inicio` date DEFAULT NULL,
  `id_delegacion_estado` int(11) DEFAULT NULL,
  `id_delegacion_tipo` int(11) DEFAULT NULL,
  `id_owner` bigint(20) DEFAULT NULL,
  `id_persona_ult_modif` bigint(20) DEFAULT NULL,
  `id_suplente` bigint(20) DEFAULT NULL,
  `id_usuario_conectado` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jrjij911gf3ab2r77w37qog11` (`alt_key`),
  KEY `FK7hugg2rrrvwdd9lmy0j2rsynb` (`id_delegacion_estado`),
  KEY `FKoa0fkeyehoboi5gqkakvqwmq4` (`id_delegacion_tipo`),
  KEY `FK39otpvwrs1aimoapk4oytv7yj` (`id_owner`),
  KEY `FK5ppkg6p6st9kacv9b037y7sxm` (`id_persona_ult_modif`),
  KEY `FKtrofupq23qxtf5yfhx4b4r5qg` (`id_suplente`),
  KEY `FK8gn83fjsqq6wufcw528uw3u27` (`id_usuario_conectado`),
  CONSTRAINT `FK39otpvwrs1aimoapk4oytv7yj` FOREIGN KEY (`id_owner`) REFERENCES `persona` (`id`),
  CONSTRAINT `FK5ppkg6p6st9kacv9b037y7sxm` FOREIGN KEY (`id_persona_ult_modif`) REFERENCES `persona` (`id`),
  CONSTRAINT `FK7hugg2rrrvwdd9lmy0j2rsynb` FOREIGN KEY (`id_delegacion_estado`) REFERENCES `delegacion_estado` (`id`),
  CONSTRAINT `FK8gn83fjsqq6wufcw528uw3u27` FOREIGN KEY (`id_usuario_conectado`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKoa0fkeyehoboi5gqkakvqwmq4` FOREIGN KEY (`id_delegacion_tipo`) REFERENCES `delegacion_tipo` (`id`),
  CONSTRAINT `FKtrofupq23qxtf5yfhx4b4r5qg` FOREIGN KEY (`id_suplente`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.delegacion: ~0 rows (aproximadamente)
DELETE FROM `delegacion`;
/*!40000 ALTER TABLE `delegacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `delegacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.delegacion_estado
DROP TABLE IF EXISTS `delegacion_estado`;
CREATE TABLE IF NOT EXISTS `delegacion_estado` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.delegacion_estado: ~0 rows (aproximadamente)
DELETE FROM `delegacion_estado`;
/*!40000 ALTER TABLE `delegacion_estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `delegacion_estado` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.delegacion_tipo
DROP TABLE IF EXISTS `delegacion_tipo`;
CREATE TABLE IF NOT EXISTS `delegacion_tipo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.delegacion_tipo: ~0 rows (aproximadamente)
DELETE FROM `delegacion_tipo`;
/*!40000 ALTER TABLE `delegacion_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `delegacion_tipo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.directory_config
DROP TABLE IF EXISTS `directory_config`;
CREATE TABLE IF NOT EXISTS `directory_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `name` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `description` varchar(2048) COLLATE latin1_spanish_ci DEFAULT NULL,
  `base_dir` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `allowed_user` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `allowed_groups` varchar(2048) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_directory_config_alt_key` (`alt_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.directory_config: ~0 rows (aproximadamente)
DELETE FROM `directory_config`;
/*!40000 ALTER TABLE `directory_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `directory_config` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.division
DROP TABLE IF EXISTS `division`;
CREATE TABLE IF NOT EXISTS `division` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activa` int(11) NOT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_jefe` bigint(20) DEFAULT NULL,
  `id_organizacion` int(11) DEFAULT NULL,
  `id_region_region` int(11) DEFAULT NULL,
  `id_servicio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5r9innay7nkrc9ep7jgclx24m` (`id_jefe`),
  KEY `FKkoaps4oitorr1cwnrv4ipxvwk` (`id_organizacion`),
  KEY `FKfwl8rg5a2mh8iehqhhku0v80` (`id_region_region`),
  KEY `FKbkmgdngqc356yrmj10qpfw3bj` (`id_servicio`),
  CONSTRAINT `FK5r9innay7nkrc9ep7jgclx24m` FOREIGN KEY (`id_jefe`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKbkmgdngqc356yrmj10qpfw3bj` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id`),
  CONSTRAINT `FKfwl8rg5a2mh8iehqhhku0v80` FOREIGN KEY (`id_region_region`) REFERENCES `region` (`id_region`),
  CONSTRAINT `FKkoaps4oitorr1cwnrv4ipxvwk` FOREIGN KEY (`id_organizacion`) REFERENCES `organizacion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.division: ~0 rows (aproximadamente)
DELETE FROM `division`;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
/*!40000 ALTER TABLE `division` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.documento
DROP TABLE IF EXISTS `documento`;
CREATE TABLE IF NOT EXISTS `documento` (
  `id_documento` int(11) unsigned NOT NULL DEFAULT '0',
  `file_name` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `file` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `despliega_info` tinyint(3) unsigned DEFAULT '0',
  PRIMARY KEY (`id_documento`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.documento: 0 rows
DELETE FROM `documento`;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.dummy
DROP TABLE IF EXISTS `dummy`;
CREATE TABLE IF NOT EXISTS `dummy` (
  `id` int(1) unsigned DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.dummy: 1 rows
DELETE FROM `dummy`;
/*!40000 ALTER TABLE `dummy` DISABLE KEYS */;
INSERT INTO `dummy` (`id`) VALUES
	(1);
/*!40000 ALTER TABLE `dummy` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.formularios
DROP TABLE IF EXISTS `formularios`;
CREATE TABLE IF NOT EXISTS `formularios` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `descripcion` text COLLATE latin1_spanish_ci,
  `xml_file` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `estado` int(10) unsigned DEFAULT '0',
  `preload` int(10) unsigned DEFAULT '0',
  `init_url` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `view_url` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.formularios: 0 rows
DELETE FROM `formularios`;
/*!40000 ALTER TABLE `formularios` DISABLE KEYS */;
/*!40000 ALTER TABLE `formularios` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.foro
DROP TABLE IF EXISTS `foro`;
CREATE TABLE IF NOT EXISTS `foro` (
  `id_foro` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `description` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `owner` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `temp_key` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_categoria` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_foro`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.foro: 0 rows
DELETE FROM `foro`;
/*!40000 ALTER TABLE `foro` DISABLE KEYS */;
/*!40000 ALTER TABLE `foro` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.foro_thread
DROP TABLE IF EXISTS `foro_thread`;
CREATE TABLE IF NOT EXISTS `foro_thread` (
  `id_foro_thread` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_foro` int(10) unsigned NOT NULL DEFAULT '0',
  `subject` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `owner` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `priority` int(10) unsigned DEFAULT '4',
  `temp_key` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `modified` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`id_foro_thread`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.foro_thread: 0 rows
DELETE FROM `foro_thread`;
/*!40000 ALTER TABLE `foro_thread` DISABLE KEYS */;
/*!40000 ALTER TABLE `foro_thread` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.foro_thread_message
DROP TABLE IF EXISTS `foro_thread_message`;
CREATE TABLE IF NOT EXISTS `foro_thread_message` (
  `id_foro_thread_message` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_foro_thread` int(10) unsigned NOT NULL DEFAULT '0',
  `owner` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `message` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_foro_thread_message`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.foro_thread_message: 0 rows
DELETE FROM `foro_thread_message`;
/*!40000 ALTER TABLE `foro_thread_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `foro_thread_message` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.forward
DROP TABLE IF EXISTS `forward`;
CREATE TABLE IF NOT EXISTS `forward` (
  `id_forward` int(11) unsigned NOT NULL DEFAULT '0',
  `id_replet` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_forward`),
  KEY `idx_forward_id_replet` (`id_replet`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.forward: 0 rows
DELETE FROM `forward`;
/*!40000 ALTER TABLE `forward` DISABLE KEYS */;
/*!40000 ALTER TABLE `forward` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.group_access
DROP TABLE IF EXISTS `group_access`;
CREATE TABLE IF NOT EXISTS `group_access` (
  `id_group` int(11) unsigned NOT NULL DEFAULT '0',
  `report` int(11) unsigned NOT NULL DEFAULT '0',
  `access` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id_group`,`report`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.group_access: 171 rows
DELETE FROM `group_access`;
/*!40000 ALTER TABLE `group_access` DISABLE KEYS */;
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 698, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 693, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 651, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 654, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 687, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 691, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 686, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(11, 650, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(11, 651, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(11, 649, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 650, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 649, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(188, 433, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(372, 433, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(355, 433, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 694, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 696, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 697, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 688, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 689, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 692, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 695, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(11, 687, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 649, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 650, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 687, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 650, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 688, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 689, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 649, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 699, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 708, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 704, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 700, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 701, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 703, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 705, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(13, 698, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(13, 704, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(13, 705, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(15, 698, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(15, 699, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(15, 700, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 707, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(15, 703, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 698, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 699, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 701, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 708, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 703, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 700, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 687, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 707, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 712, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 686, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 713, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 721, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 651, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 716, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 651, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 718, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 709, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 722, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 723, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 725, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 711, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 719, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(2, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(3, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(17, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(15, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(16, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(13, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(11, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(12, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 222, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 782, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 781, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 780, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 779, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 778, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 777, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 776, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 774, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 775, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 773, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 726, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 786, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 785, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 784, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 724, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 720, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 710, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 783, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 727, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 692, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 741, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 742, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 743, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 744, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 743, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 744, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 745, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 749, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 750, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 748, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 751, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 752, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 753, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 754, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 755, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 756, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 757, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(14, 758, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 729, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 759, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 764, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 765, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 766, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 760, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 762, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 763, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 761, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 771, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 767, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 768, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 769, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 770, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(9, 772, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(10, 772, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 726, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 743, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 745, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 749, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 750, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 748, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 751, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 752, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 753, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 754, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 755, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 756, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 757, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 758, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 744, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(20, 726, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 727, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 729, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 759, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 764, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 765, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 766, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 760, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 762, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 763, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 771, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 767, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 768, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 769, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 770, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 761, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 730, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 731, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 732, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 733, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 735, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 736, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 737, 3);
INSERT INTO `group_access` (`id_group`, `report`, `access`) VALUES
	(19, 740, 3);
/*!40000 ALTER TABLE `group_access` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.grupo
DROP TABLE IF EXISTS `grupo`;
CREATE TABLE IF NOT EXISTS `grupo` (
  `id_grupo` int(11) unsigned NOT NULL DEFAULT '0',
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `grupo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `privado` int(1) unsigned DEFAULT '0',
  `home` varchar(255) COLLATE latin1_spanish_ci DEFAULT '',
  `codigos_sistemas` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_grupo`),
  UNIQUE KEY `uk_grupo_codigo` (`codigo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.grupo: 14 rows
DELETE FROM `grupo`;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(2, NULL, 'Editor de Plantillas', 'Personas que pueden editar plantillas de estilos', 1, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(3, NULL, 'Maestros de datos', 'Puede modificar registros en las tablas de base de datos', 1, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(10, 'DEFENSORIA_ADMIN', 'Administrador general', 'Administrador general: mantenedores, usuarios, notificaciones, etc.', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(11, 'DEFENSORIA_REGISTRO', 'Registro de requerimientos', 'Registro y derivación de requerimientos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(12, 'DEFENSORIA_RESPONSABLE_CASO', 'Gestión requerimientos', 'Gestión de requerimientos y casos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(13, 'DEFENSORIA_EVALUADOR_ANTECEDENTE', 'Evaluador de antecedentes', 'Evaluador de antecedentes', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(14, 'DEFENSORIA_ADMIN_HALLAZGOS', 'Administrador gestión de hallazgos', 'Administrador gestión de hallazgos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(15, 'DEFENSORIA_INGRESO_ANTECEDENTE', 'Ingreso de antecedentes', 'Ingreso de antecedentes', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(16, 'DEFENSORIA_REVISION_ANTECEDENTE', 'Revisión de antecedentes', 'Revisión de antecedentes en la unidad de origen', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(17, 'DEFENSORIA_REVISION_HALLAZGOS', 'Revisión de hallazgos', 'Revisión de hallazgos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(9, 'DEFENSORIA_ADMISIBILIDAD', 'Admisibilidad de nuevos requerimientos', 'Admisibilidad de nuevos requerimientos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(18, 'DEFENSORIA_MULTI_SEDE', 'Usuario multi sede', 'Usuario multi sede', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(19, 'DEFENSORIA_CONSULTA_REPORTES_REQ_CASOS', 'Usuario consulta para reportes de casos y requerimientos', 'Usuario consulta para reportes de casos y requerimientos', 0, '', NULL);
INSERT INTO `grupo` (`id_grupo`, `codigo`, `grupo`, `descr`, `privado`, `home`, `codigos_sistemas`) VALUES
	(20, 'DEFENSORIA_CONSULTA_REPORTES_HALLAZGOS', 'Usuario consulta para reportes de hallazgos', 'Usuario consulta para reportes de hallazgos', 0, '', NULL);
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.id
DROP TABLE IF EXISTS `id`;
CREATE TABLE IF NOT EXISTS `id` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=787 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.id: 1 rows
DELETE FROM `id`;
/*!40000 ALTER TABLE `id` DISABLE KEYS */;
INSERT INTO `id` (`id`) VALUES
	(786);
/*!40000 ALTER TABLE `id` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.imagenes_sistema
DROP TABLE IF EXISTS `imagenes_sistema`;
CREATE TABLE IF NOT EXISTS `imagenes_sistema` (
  `id_imagen` int(5) unsigned NOT NULL DEFAULT '0',
  `nombre_imagen` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre_archivo` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  PRIMARY KEY (`id_imagen`),
  UNIQUE KEY `id_imagen` (`id_imagen`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.imagenes_sistema: 0 rows
DELETE FROM `imagenes_sistema`;
/*!40000 ALTER TABLE `imagenes_sistema` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagenes_sistema` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.item_lista
DROP TABLE IF EXISTS `item_lista`;
CREATE TABLE IF NOT EXISTS `item_lista` (
  `id_item` int(11) NOT NULL DEFAULT '0',
  `id_seccion` int(11) unsigned NOT NULL DEFAULT '0',
  `id_padre` int(10) unsigned DEFAULT NULL,
  `tipo_lista` int(11) unsigned NOT NULL DEFAULT '0',
  `orden` int(11) unsigned NOT NULL DEFAULT '0',
  `caption` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `image` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `nom_ref` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descr_ref` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo_despliegue` tinyint(3) unsigned DEFAULT '0',
  `tipo_ref` tinyint(3) unsigned DEFAULT '0',
  `id_ref` int(11) unsigned DEFAULT '0',
  PRIMARY KEY (`id_item`),
  UNIQUE KEY `IDX_seccion_tipo_orden` (`id_seccion`,`tipo_lista`,`orden`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.item_lista: 0 rows
DELETE FROM `item_lista`;
/*!40000 ALTER TABLE `item_lista` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_lista` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.log
DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `dtype` varchar(31) COLLATE latin1_spanish_ci NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha` datetime DEFAULT NULL,
  `http_header` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `ip` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `observaciones` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `username` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `responsable` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_evento` int(11) DEFAULT NULL,
  `id_tarea` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhcthio1ejuqx1ojs7p58a23oo` (`id_tarea`),
  CONSTRAINT `FKhcthio1ejuqx1ojs7p58a23oo` FOREIGN KEY (`id_tarea`) REFERENCES `tarea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.log: ~0 rows (aproximadamente)
DELETE FROM `log`;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.login_manager
DROP TABLE IF EXISTS `login_manager`;
CREATE TABLE IF NOT EXISTS `login_manager` (
  `id_login_manager` int(5) unsigned NOT NULL DEFAULT '0',
  `class_name` text COLLATE latin1_spanish_ci,
  `id_pool_conexiones` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_login_manager`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.login_manager: 2 rows
DELETE FROM `login_manager`;
/*!40000 ALTER TABLE `login_manager` DISABLE KEYS */;
INSERT INTO `login_manager` (`id_login_manager`, `class_name`, `id_pool_conexiones`) VALUES
	(0, 'ai.security.TAILoginManager', 'pool_control');
INSERT INTO `login_manager` (`id_login_manager`, `class_name`, `id_pool_conexiones`) VALUES
	(1, 'ai.security.TAILoginManagerMD5', 'pool_control');
/*!40000 ALTER TABLE `login_manager` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.log_evento
DROP TABLE IF EXISTS `log_evento`;
CREATE TABLE IF NOT EXISTS `log_evento` (
  `id` int(11) NOT NULL,
  `nombre` varchar(512) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.log_evento: ~0 rows (aproximadamente)
DELETE FROM `log_evento`;
/*!40000 ALTER TABLE `log_evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_evento` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.mensajes
DROP TABLE IF EXISTS `mensajes`;
CREATE TABLE IF NOT EXISTS `mensajes` (
  `ambiente` varchar(50) COLLATE latin1_spanish_ci NOT NULL,
  `codigo` varchar(512) COLLATE latin1_spanish_ci NOT NULL,
  `mensaje` text COLLATE latin1_spanish_ci,
  `tipo` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`ambiente`,`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.mensajes: ~0 rows (aproximadamente)
DELETE FROM `mensajes`;
/*!40000 ALTER TABLE `mensajes` DISABLE KEYS */;
/*!40000 ALTER TABLE `mensajes` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.modulo
DROP TABLE IF EXISTS `modulo`;
CREATE TABLE IF NOT EXISTS `modulo` (
  `id_modulo` int(10) unsigned NOT NULL DEFAULT '0',
  `url` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  PRIMARY KEY (`id_modulo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.modulo: 0 rows
DELETE FROM `modulo`;
/*!40000 ALTER TABLE `modulo` DISABLE KEYS */;
/*!40000 ALTER TABLE `modulo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia
DROP TABLE IF EXISTS `noticia`;
CREATE TABLE IF NOT EXISTS `noticia` (
  `id_noticia` int(11) NOT NULL AUTO_INCREMENT,
  `id_plantilla_ref` int(10) unsigned NOT NULL DEFAULT '0',
  `id_replet` int(11) unsigned NOT NULL DEFAULT '0',
  `estado` int(1) unsigned DEFAULT '0',
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `autor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_publicacion_ini` date DEFAULT '0000-00-00',
  `imagen_index` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `owner` varchar(20) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_publicacion_fin` date DEFAULT '0000-00-00',
  `fecha_creacion` date NOT NULL DEFAULT '0000-00-00',
  `fecha_modificacion` date NOT NULL DEFAULT '0000-00-00',
  `titulo` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `subtitulo` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `titulo_region1` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region1` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region2` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region2` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region3` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region3` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region4` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region4` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region5` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region5` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region6` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region6` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region7` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region7` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region8` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region8` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region9` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region9` text COLLATE latin1_spanish_ci NOT NULL,
  `titulo_region10` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `region10` text COLLATE latin1_spanish_ci NOT NULL,
  `imagen1` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen2` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen3` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen4` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen5` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen6` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen7` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen8` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen9` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `imagen10` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption1` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption2` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption3` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption4` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption5` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption6` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption7` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption8` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption9` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `caption10` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `file1` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `file_caption1` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_noticia`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia: 0 rows
DELETE FROM `noticia`;
/*!40000 ALTER TABLE `noticia` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_canal
DROP TABLE IF EXISTS `noticia_canal`;
CREATE TABLE IF NOT EXISTS `noticia_canal` (
  `id_noticia` int(10) unsigned NOT NULL DEFAULT '0',
  `id_tipo` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_noticia`,`id_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_canal: 0 rows
DELETE FROM `noticia_canal`;
/*!40000 ALTER TABLE `noticia_canal` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_canal` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_contenido_indice
DROP TABLE IF EXISTS `noticia_contenido_indice`;
CREATE TABLE IF NOT EXISTS `noticia_contenido_indice` (
  `id_indice` int(5) unsigned DEFAULT '0',
  `id_noticia` int(10) unsigned DEFAULT '0',
  `orden` int(2) unsigned DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_contenido_indice: 0 rows
DELETE FROM `noticia_contenido_indice`;
/*!40000 ALTER TABLE `noticia_contenido_indice` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_contenido_indice` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_group_access
DROP TABLE IF EXISTS `noticia_group_access`;
CREATE TABLE IF NOT EXISTS `noticia_group_access` (
  `id_grupo` int(11) NOT NULL DEFAULT '0',
  `id_tipo_noticia` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_grupo`,`id_tipo_noticia`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_group_access: 0 rows
DELETE FROM `noticia_group_access`;
/*!40000 ALTER TABLE `noticia_group_access` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_group_access` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_indice
DROP TABLE IF EXISTS `noticia_indice`;
CREATE TABLE IF NOT EXISTS `noticia_indice` (
  `id_indice` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `temp_key` double NOT NULL DEFAULT '0',
  `owner` varchar(20) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `nombre_indice` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `id_tipo_indice` int(3) unsigned NOT NULL DEFAULT '0',
  `titulo_indice` varchar(50) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `id_tipo_noticia` int(5) unsigned NOT NULL DEFAULT '0',
  `contenido_automatico` int(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_indice`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_indice: 0 rows
DELETE FROM `noticia_indice`;
/*!40000 ALTER TABLE `noticia_indice` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_indice` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_tipo
DROP TABLE IF EXISTS `noticia_tipo`;
CREATE TABLE IF NOT EXISTS `noticia_tipo` (
  `tipo_noticia` int(10) unsigned NOT NULL DEFAULT '0',
  `nombre` varchar(128) COLLATE latin1_spanish_ci DEFAULT '0',
  `titulo_indice` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`tipo_noticia`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_tipo: 0 rows
DELETE FROM `noticia_tipo`;
/*!40000 ALTER TABLE `noticia_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_tipo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.noticia_tipo_indice
DROP TABLE IF EXISTS `noticia_tipo_indice`;
CREATE TABLE IF NOT EXISTS `noticia_tipo_indice` (
  `id_tipo_indice` int(3) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_indice` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_tipo_indice`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Tipos de Indices de noticias';

-- Volcando datos para la tabla ai_defensoria_req_qa.noticia_tipo_indice: 0 rows
DELETE FROM `noticia_tipo_indice`;
/*!40000 ALTER TABLE `noticia_tipo_indice` DISABLE KEYS */;
/*!40000 ALTER TABLE `noticia_tipo_indice` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.notificacion_attachment
DROP TABLE IF EXISTS `notificacion_attachment`;
CREATE TABLE IF NOT EXISTS `notificacion_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_notificacion` int(11) NOT NULL,
  `name_` varchar(1024) COLLATE latin1_spanish_ci DEFAULT NULL,
  `original_filename` varchar(1024) COLLATE latin1_spanish_ci DEFAULT NULL,
  `content_type` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `size_` bigint(20) DEFAULT NULL,
  `data_type` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `url` varchar(2100) COLLATE latin1_spanish_ci DEFAULT NULL,
  `path` varchar(4096) COLLATE latin1_spanish_ci DEFAULT NULL,
  `date_` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notificacion_attachment_notificacion` (`id_notificacion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.notificacion_attachment: 0 rows
DELETE FROM `notificacion_attachment`;
/*!40000 ALTER TABLE `notificacion_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacion_attachment` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.notificacion_estado
DROP TABLE IF EXISTS `notificacion_estado`;
CREATE TABLE IF NOT EXISTS `notificacion_estado` (
  `id_estado_notificacion` int(1) NOT NULL DEFAULT '0',
  `descr_estado` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_estado_notificacion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.notificacion_estado: 5 rows
DELETE FROM `notificacion_estado`;
/*!40000 ALTER TABLE `notificacion_estado` DISABLE KEYS */;
INSERT INTO `notificacion_estado` (`id_estado_notificacion`, `descr_estado`) VALUES
	(0, 'Por Enviar');
INSERT INTO `notificacion_estado` (`id_estado_notificacion`, `descr_estado`) VALUES
	(1, 'Enviada');
INSERT INTO `notificacion_estado` (`id_estado_notificacion`, `descr_estado`) VALUES
	(2, 'Error en Envio');
INSERT INTO `notificacion_estado` (`id_estado_notificacion`, `descr_estado`) VALUES
	(3, 'No procesable');
INSERT INTO `notificacion_estado` (`id_estado_notificacion`, `descr_estado`) VALUES
	(4, 'No válida');
/*!40000 ALTER TABLE `notificacion_estado` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.organizacion
DROP TABLE IF EXISTS `organizacion`;
CREATE TABLE IF NOT EXISTS `organizacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activa` int(11) NOT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_servicio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7stj5for9ll8caoxaoygnl63g` (`id_servicio`),
  CONSTRAINT `FK7stj5for9ll8caoxaoygnl63g` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.organizacion: ~0 rows (aproximadamente)
DELETE FROM `organizacion`;
/*!40000 ALTER TABLE `organizacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `organizacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.paginas_dinamicas
DROP TABLE IF EXISTS `paginas_dinamicas`;
CREATE TABLE IF NOT EXISTS `paginas_dinamicas` (
  `id_pagina` int(11) unsigned NOT NULL DEFAULT '0',
  `text1` text COLLATE latin1_spanish_ci,
  `text2` text COLLATE latin1_spanish_ci,
  `image1` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `image2` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `tipo_despliegue` int(11) DEFAULT '1',
  PRIMARY KEY (`id_pagina`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.paginas_dinamicas: 0 rows
DELETE FROM `paginas_dinamicas`;
/*!40000 ALTER TABLE `paginas_dinamicas` DISABLE KEYS */;
/*!40000 ALTER TABLE `paginas_dinamicas` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.persona
DROP TABLE IF EXISTS `persona`;
CREATE TABLE IF NOT EXISTS `persona` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `activa` int(11) NOT NULL,
  `ape_mat` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `ape_pat` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `dv` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `email` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT NULL,
  `fecha_modificacion` datetime DEFAULT NULL,
  `login` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre_completo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombres` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `permite_aprobar_todos_revision` int(11) NOT NULL,
  `rut` int(11) DEFAULT NULL,
  `telefono` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_cargo` int(11) DEFAULT NULL,
  `id_division` int(11) DEFAULT NULL,
  `id_revisor_formulario` bigint(20) DEFAULT NULL,
  `id_servicio` int(11) DEFAULT NULL,
  `id_unidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jcuq0m682f2k8uj0vgat7qypo` (`alt_key`),
  KEY `FKdid9qtj5img58amw0suvcuyph` (`id_cargo`),
  KEY `FK5j9br37hkhlduhr6r2s00yxqo` (`id_division`),
  KEY `FK64llnt9xatisx9uejbtjuup6i` (`id_revisor_formulario`),
  KEY `FKh5mkyjrvxyrb5vvfrt7g6s2j8` (`id_servicio`),
  KEY `FK4i6p19g1s97ph5cy70p35cncq` (`id_unidad`),
  CONSTRAINT `FK4i6p19g1s97ph5cy70p35cncq` FOREIGN KEY (`id_unidad`) REFERENCES `unidad` (`id`),
  CONSTRAINT `FK5j9br37hkhlduhr6r2s00yxqo` FOREIGN KEY (`id_division`) REFERENCES `division` (`id`),
  CONSTRAINT `FK64llnt9xatisx9uejbtjuup6i` FOREIGN KEY (`id_revisor_formulario`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKdid9qtj5img58amw0suvcuyph` FOREIGN KEY (`id_cargo`) REFERENCES `cargo` (`id`),
  CONSTRAINT `FKh5mkyjrvxyrb5vvfrt7g6s2j8` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.persona: ~0 rows (aproximadamente)
DELETE FROM `persona`;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.plantilla_form
DROP TABLE IF EXISTS `plantilla_form`;
CREATE TABLE IF NOT EXISTS `plantilla_form` (
  `id_plantilla_form` int(11) NOT NULL DEFAULT '0',
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `archivo_plantilla_form` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` date DEFAULT '0000-00-00',
  `fecha_modificacion` date DEFAULT '0000-00-00',
  `owner` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo_plantilla` int(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_plantilla_form`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.plantilla_form: 0 rows
DELETE FROM `plantilla_form`;
/*!40000 ALTER TABLE `plantilla_form` DISABLE KEYS */;
/*!40000 ALTER TABLE `plantilla_form` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.plantilla_tipo
DROP TABLE IF EXISTS `plantilla_tipo`;
CREATE TABLE IF NOT EXISTS `plantilla_tipo` (
  `id_plantilla_tipo` int(3) unsigned DEFAULT NULL,
  `descripcion` varchar(60) COLLATE latin1_spanish_ci DEFAULT NULL,
  KEY `id_plantilla_tipo` (`id_plantilla_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.plantilla_tipo: 5 rows
DELETE FROM `plantilla_tipo`;
/*!40000 ALTER TABLE `plantilla_tipo` DISABLE KEYS */;
INSERT INTO `plantilla_tipo` (`id_plantilla_tipo`, `descripcion`) VALUES
	(1, 'PÃ¡gina WEB');
INSERT INTO `plantilla_tipo` (`id_plantilla_tipo`, `descripcion`) VALUES
	(2, 'Formulario');
INSERT INTO `plantilla_tipo` (`id_plantilla_tipo`, `descripcion`) VALUES
	(3, 'CategorÃ­a');
INSERT INTO `plantilla_tipo` (`id_plantilla_tipo`, `descripcion`) VALUES
	(4, 'Estilo CSS');
INSERT INTO `plantilla_tipo` (`id_plantilla_tipo`, `descripcion`) VALUES
	(5, 'Indice de PÃ¡ginas');
/*!40000 ALTER TABLE `plantilla_tipo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.plugin
DROP TABLE IF EXISTS `plugin`;
CREATE TABLE IF NOT EXISTS `plugin` (
  `id_plugin` int(11) unsigned NOT NULL DEFAULT '0',
  `file_name` char(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_plugin`),
  UNIQUE KEY `file_name` (`file_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.plugin: 0 rows
DELETE FROM `plugin`;
/*!40000 ALTER TABLE `plugin` DISABLE KEYS */;
/*!40000 ALTER TABLE `plugin` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.plugin_modulo
DROP TABLE IF EXISTS `plugin_modulo`;
CREATE TABLE IF NOT EXISTS `plugin_modulo` (
  `id_replet` int(10) unsigned NOT NULL DEFAULT '0',
  `id_modulo` int(10) unsigned NOT NULL DEFAULT '0',
  `id_plugin` int(10) unsigned NOT NULL DEFAULT '0',
  `publico` char(1) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.plugin_modulo: 0 rows
DELETE FROM `plugin_modulo`;
/*!40000 ALTER TABLE `plugin_modulo` DISABLE KEYS */;
/*!40000 ALTER TABLE `plugin_modulo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.plugin_parametro
DROP TABLE IF EXISTS `plugin_parametro`;
CREATE TABLE IF NOT EXISTS `plugin_parametro` (
  `id_replet` int(11) NOT NULL DEFAULT '0',
  `id_plugin` int(10) unsigned NOT NULL DEFAULT '0',
  `parametro` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `valor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.plugin_parametro: 0 rows
DELETE FROM `plugin_parametro`;
/*!40000 ALTER TABLE `plugin_parametro` DISABLE KEYS */;
/*!40000 ALTER TABLE `plugin_parametro` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.processor
DROP TABLE IF EXISTS `processor`;
CREATE TABLE IF NOT EXISTS `processor` (
  `id_processor` int(11) NOT NULL AUTO_INCREMENT,
  `class` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `parametros` text COLLATE latin1_spanish_ci,
  `tipo_processor` int(11) DEFAULT '0',
  PRIMARY KEY (`id_processor`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.processor: 5 rows
DELETE FROM `processor`;
/*!40000 ALTER TABLE `processor` DISABLE KEYS */;
INSERT INTO `processor` (`id_processor`, `class`, `descripcion`, `parametros`, `tipo_processor`) VALUES
	(1, 'ai.replet_processors.TProcessorPercent', 'Procesador de Procentajes', '<config>\r\n	<titulo>Procesador de Procentajes</titulo>\r\n	<descripcion>Procesador de Procentajes</descripcion>\r\n	<ayuda>Campos para calcular porcentaje (columna a dividir, con cual se divide, decimales )</ayuda>\r\n	<cant_parametros>3</cant_parametros>\r\n	<parametros>\r\n		<parametro>Columna a Dividir</parametro>\r\n		<parametro>Columna con que se Divide</parametro>\r\n		<parametro>Cantidad Decimales</parametro>\r\n	</parametros>\r\n</config>', 0);
INSERT INTO `processor` (`id_processor`, `class`, `descripcion`, `parametros`, `tipo_processor`) VALUES
	(2, 'ai.replet_processors.TProcessorPercentInverse', 'Procesador de Procentajes Inverso 2', '<config>\r\n	<titulo>Procesador de Procentajes Inverso</titulo>\r\n	<descripcion>Procesador de Procentajes Inverso</descripcion>\r\n	<ayuda>Campos para calcular porcentaje Inverso (columna a dividir, con cual se divide, decimales )</ayuda>\r\n	<cant_parametros>3</cant_parametros>\r\n	<parametros>\r\n		<parametro>Columna a Dividir</parametro>\r\n		<parametro>Columna con que se Divide</parametro>\r\n		<parametro>Cantidad Decimales</parametro>\r\n	</parametros>\r\n</config>', 0);
INSERT INTO `processor` (`id_processor`, `class`, `descripcion`, `parametros`, `tipo_processor`) VALUES
	(3, 'ai.replet_processors.TProcessorAverage', 'Procesador de Promedio', '<config>\r\n <titulo>Procesador de Promedio</titulo>\r\n <descripcion>Procesador de Promedio</descripcion>\r\n <ayuda>Campos para calcular promedio (columna sobre la cual calcular promedio)</ayuda>\r\n <cant_parametros>1</cant_parametros>\r\n <parametros>\r\n <parametro>Columna sobre la cual calcular el promedio</parametro>\r\n </parametros>\r\n</config>', 0);
INSERT INTO `processor` (`id_processor`, `class`, `descripcion`, `parametros`, `tipo_processor`) VALUES
	(4, 'ai.replet_processors.TProcessorColumnsExpression', 'Operación aritmética entre columnas', '\'<config>\r\n<titulo>Operación aritmética de columnas </titulo>\r\n<descripcion> Usar sintaxis ColA+ColB+ColC-ColD (con signos +, -, / y *) </descripcion>\r\n <ayuda>No dejar espacios.</ayuda>\r\n <cant_parametros>1</cant_parametros>\r\n<parametros>\r\n<parametro>La expresión a evaluar </parametro>\r\n</parametros>\r\n</config>\'', 0);
INSERT INTO `processor` (`id_processor`, `class`, `descripcion`, `parametros`, `tipo_processor`) VALUES
	(5, 'ai.replet_processors.TProcessorColumnPercent', 'Calcula el porcentaje entre dos columnas (A/B)*100', ' <config>\r\n <titulo>Calcula el porcentaje entre dos columnas (A/B)*100</titulo>\r\n <descripcion>Procesador de porcentaje entre dos columnas.</descripcion>\r\n <ayuda>(A/B)*100 </ayuda>\r\n <cant_parametros>3</cant_parametros>\r\n <parametros>\r\n  <parametro>Columna A</parametro>\r\n  <parametro>Columna B</parametro>\r\n  <parametro>Cantidad Decimales</parametro>\r\n </parametros>\r\n </config>', 0);
/*!40000 ALTER TABLE `processor` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.recurso_plantilla
DROP TABLE IF EXISTS `recurso_plantilla`;
CREATE TABLE IF NOT EXISTS `recurso_plantilla` (
  `id_recurso` int(11) NOT NULL DEFAULT '0',
  `id_plantilla_ref` int(11) NOT NULL DEFAULT '0',
  `parametros_include` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `texto1` text COLLATE latin1_spanish_ci,
  `texto2` text COLLATE latin1_spanish_ci,
  `texto3` text COLLATE latin1_spanish_ci,
  `titulo_region1` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto4` text COLLATE latin1_spanish_ci,
  `titulo_region2` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto5` text COLLATE latin1_spanish_ci,
  `titulo_region3` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto6` text COLLATE latin1_spanish_ci,
  `titulo_region4` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto7` text COLLATE latin1_spanish_ci,
  `titulo_region5` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto8` text COLLATE latin1_spanish_ci,
  `titulo_region6` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto9` text COLLATE latin1_spanish_ci,
  `titulo_region7` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto10` text COLLATE latin1_spanish_ci,
  `titulo_region8` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto11` text COLLATE latin1_spanish_ci,
  `titulo_region9` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto12` text COLLATE latin1_spanish_ci,
  `titulo_region10` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto13` text COLLATE latin1_spanish_ci,
  `imagen_1` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_2` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_3` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_4` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_5` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_6` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_7` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_8` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_9` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `imagen_10` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption1` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption2` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption3` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption4` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption5` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption6` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption7` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption8` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption9` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption10` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link1` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link2` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link3` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link4` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link5` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link6` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link7` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link8` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link9` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `link10` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `invisible_after_login` int(1) unsigned DEFAULT '0',
  `xml_data` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_recurso`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.recurso_plantilla: 0 rows
DELETE FROM `recurso_plantilla`;
/*!40000 ALTER TABLE `recurso_plantilla` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso_plantilla` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.region
DROP TABLE IF EXISTS `region`;
CREATE TABLE IF NOT EXISTS `region` (
  `id_region` int(11) NOT NULL,
  `nombre_region` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `orden_geografico` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_region`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.region: ~0 rows (aproximadamente)
DELETE FROM `region`;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
/*!40000 ALTER TABLE `region` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet
DROP TABLE IF EXISTS `replet`;
CREATE TABLE IF NOT EXISTS `replet` (
  `id_replet` int(11) unsigned NOT NULL DEFAULT '0',
  `id_tipo_reporte` tinyint(3) unsigned DEFAULT NULL,
  `id_categoria` int(10) unsigned NOT NULL DEFAULT '0',
  `orden` int(11) DEFAULT NULL,
  `orden_temp` int(11) DEFAULT NULL,
  `mime_type` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` date NOT NULL,
  `fecha_modificacion` date NOT NULL,
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `footer` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `title` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `title_style` varchar(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descr_style` varchar(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  `footer_style` varchar(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  `aparece_en_listado` tinyint(3) unsigned DEFAULT '1',
  `despliega_datos_usuario` int(11) DEFAULT '0',
  `publico` tinyint(3) unsigned DEFAULT NULL,
  `owner` varchar(20) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `invisible_con_login` int(1) unsigned DEFAULT '0',
  `id_imagen_asociada` int(5) unsigned DEFAULT '0',
  `autor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_publicacion` date DEFAULT NULL,
  `id_default_navigation_relator` int(10) unsigned NOT NULL DEFAULT '0',
  `id_param_visible_replet` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_replet`),
  UNIQUE KEY `IDX_REP_CAT` (`id_categoria`,`id_replet`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet: 81 rows
DELETE FROM `replet`;
/*!40000 ALTER TABLE `replet` DISABLE KEYS */;
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(239, 4, 222, 2, 1, 'url', '2015-10-06', '2020-03-31', '', '', 'Acerca del sistema', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(234, 4, 3, 0, 1, 'url', '2015-10-03', '2015-10-03', '', '', 'Conexión', 'page_title', 'text', 'page_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(235, 4, 3, 197, NULL, 'url', '2015-10-03', '2020-03-31', '', '', 'Acerca del sistema', 'page_title', 'text', 'page_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(651, 4, 650, 0, 0, 'url', '2020-03-31', '2020-12-16', '', '', 'Nuevos requerimientos<br>(Sin derivar)', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(689, 4, 688, 2, 1, 'url', '2020-11-23', '2021-01-04', '', '', 'Buscador de <br>requerimientos y casos', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(687, 4, 650, 3, 2, 'url', '2020-11-23', '2020-12-16', '', '', 'Buscador de<br>requerimientos', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(707, 4, 688, 0, 0, 'url', '2020-12-23', '2020-12-31', '', '', 'Mis requerimientos', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(708, 4, 688, 1, NULL, 'url', '2020-12-23', '2020-12-31', '', '', 'Mi Unidad/Sede', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(710, 4, 709, 225, NULL, 'url', '2020-12-29', '2020-12-29', '', '', 'Tipo de solicitud (Requerimiento)', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(711, 4, 709, 226, NULL, 'url', '2020-12-29', '2020-12-29', '', '', 'Vía de ingreso', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(713, 4, 712, -1, 0, 'url', '2020-12-29', '2020-12-31', '', '', 'Elaboración<br>(Registro)', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(716, 4, 712, 0, 1, 'url', '2020-12-29', '2020-12-31', '', '', 'Revisión', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(718, 4, 712, 2, NULL, 'url', '2020-12-29', '2020-12-31', '', '', 'Confirmación<br>Estrategia', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(719, 4, 709, 229, NULL, 'url', '2020-12-29', '2020-12-30', '', '', 'Formatos textos respuesta <br> rápida', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(720, 4, 709, 230, NULL, 'url', '2020-12-30', '2020-12-30', '', '', 'Tiempos de respuesta', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(721, 4, 712, 1, 2, 'url', '2020-12-30', '2020-12-30', '', '', 'Buscador', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(723, 4, 722, 231, NULL, 'url', '2021-01-05', '2021-01-05', '', '', 'Administración', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(724, 4, 709, 232, NULL, 'url', '2021-01-12', '2021-01-12', '', '', 'Actividad Pre-definida', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(725, 100, 722, 233, NULL, 'reporte', '2021-01-13', '2021-01-13', '', '', 'Notificaciones enviadas', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(703, 4, 699, 2, NULL, 'url', '2020-12-15', '2020-12-30', '', '', 'Buscador', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(701, 4, 699, 0, 1, 'url', '2020-12-15', '2020-12-15', '', '', 'Revisión', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(705, 4, 704, 0, 0, 'url', '2020-12-15', '2020-12-15', '', '', 'Evaluación', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(700, 4, 699, -1, 0, 'url', '2020-12-15', '2020-12-15', '', '', 'Ingreso', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(697, 4, 696, 218, NULL, 'url', '2020-11-23', '2020-12-02', '', '', 'Administración', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(695, 4, 688, 4, 3, 'url', '2020-11-23', '2020-12-31', '', '', 'Gestiones con<br>puntos focales', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(694, 4, 693, 216, NULL, 'url', '2020-11-23', '2020-12-02', '', '', 'Administración', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(686, 4, 650, 1, 1, 'url', '2020-11-23', '2020-12-16', '', '', 'Derivados y sin asignar', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(691, 4, 650, 2, 3, 'url', '2020-11-23', '2020-12-09', '', '', 'Revisión de la<br>Defensora', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(692, 4, 688, 3, 2, 'url', '2020-11-23', '2020-12-02', '', '', 'Solicitudes de cierre', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(730, 1, 727, 234, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por mes', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(731, 1, 727, 235, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por Tipo de solicitud', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(732, 1, 727, 236, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por Sede', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(733, 1, 727, 237, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por región', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(735, 1, 727, 238, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por Vía de ingreso', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(736, 1, 727, 239, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Por Tipo requirente', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(737, 1, 727, 240, NULL, 'reporte', '2021-02-04', '2021-07-28', '', '', 'Listado de requerimientos', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(738, 4, 222, 0, 0, 'url', '2021-02-09', '2021-02-09', '', '', 'Videos tutoriales', 'page_title', 'text', 'page_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(739, 4, 222, 1, 2, 'url', '2021-02-09', '2021-02-09', '', '', 'ADMIN Videos tutoriales', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(740, 100, 727, 241, NULL, 'reporte', '2021-03-14', '2021-07-28', '', '', 'Tiempos de tramitación', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 1, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(742, 4, 741, 242, NULL, 'url', '2021-03-24', '2021-03-24', '', '', 'Listado', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(744, 1, 743, 12, 0, 'reporte', '2021-04-05', '2021-04-23', 'Tiempos de gestión', '', 'Tiempos de gestión', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(745, 1, 743, 0, 1, 'reporte', '2021-04-20', '2021-04-21', 'Listado de antecedentes y hallazgos', '', 'Listado', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(747, 4, 746, 243, NULL, 'url', '2021-04-20', '2021-04-20', '', '', 'Selectores', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(748, 1, 743, 3, 4, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por tipo de problema genérico', '', 'Por tipo de problema', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(749, 1, 743, 1, 2, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por unidad', '', 'Por unidad', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(750, 1, 743, 2, 3, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por sede', '', 'Por sede', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(759, 1, 729, 0, 0, 'reporte', '2021-04-27', '2021-07-06', 'Listado', '', 'Listado', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(751, 1, 743, 4, 5, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por macroproceso', '', 'Por Macroproceso', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(752, 1, 743, 5, 6, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por proceso', '', 'Por proceso', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(753, 1, 743, 6, 7, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por región', '', 'Por región', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(754, 1, 743, 7, 8, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por comuna', '', 'Por comuna', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(755, 1, 743, 8, 9, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por área de derecho', '', 'Por área de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(756, 1, 743, 9, 10, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por categoria de derecho', '', 'Por categoria de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(757, 1, 743, 10, 11, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por sub-categoria de derecho', '', 'Por sub-categoria de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(758, 1, 743, 11, 12, 'reporte', '2021-04-22', '2021-04-23', 'Antecedentes por Sub-sub-categoria de derecho', '', 'Por Sub-sub-categoria de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(760, 1, 729, 4, 4, 'reporte', '2021-04-27', '2021-07-05', 'Casos por tipo de complejidad', '', 'Por tipo de complejidad', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(761, 1, 729, 7, 7, 'reporte', '2021-04-27', '2021-07-05', 'Casos por responsable', '', 'Por responsable', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(762, 1, 729, 5, 5, 'reporte', '2021-04-27', '2021-07-05', 'Casos por urgencia', '', 'Por urgencia', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(763, 1, 729, 6, 6, 'reporte', '2021-04-27', '2021-07-05', 'Casos por componente', '', 'Por componente', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(764, 1, 729, 1, 1, 'reporte', '2021-04-27', '2021-07-05', 'Casos por unidad', '', 'Por unidad', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(765, 1, 729, 2, 2, 'reporte', '2021-04-27', '2021-07-05', 'Casos por sede', '', 'Por sede', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(766, 1, 729, 3, 3, 'reporte', '2021-04-27', '2021-07-05', 'Casos por tipo de solicitud', '', 'Por tipo de solicitud', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(767, 1, 729, 9, 8, 'reporte', '2021-04-28', '2021-07-05', 'Casos por area de derecho', '', 'Por area de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(768, 1, 729, 10, 9, 'reporte', '2021-04-28', '2021-07-05', 'Casos por categoría de derecho', '', 'Por categoría de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(769, 1, 729, 11, 10, 'reporte', '2021-04-28', '2021-07-05', 'Casos por sub-categoría de derecho', '', 'Por sub-categoría de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(770, 1, 729, 12, 11, 'reporte', '2021-04-28', '2021-07-05', 'Casos por sub-sub-categoría de derecho', '', 'Por sub-sub-categoría de derecho', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(771, 1, 729, 8, 12, 'reporte', '2021-04-29', '2021-07-05', 'Casos por criticidad', '', 'Por criticidad', 'report_title', 'report_subtitle', 'report_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, 0);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(772, 4, 688, 244, NULL, 'url', '2021-07-01', '2021-07-01', '', '', 'Re-apertura', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(774, 4, 773, 1, 0, 'url', '2021-08-02', '2021-08-02', '', '', 'Región', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(775, 4, 773, 0, 1, 'url', '2021-08-02', '2021-08-02', '', '', 'País', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(776, 4, 773, 245, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Comuna', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(777, 4, 773, 246, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Pueblo Originario', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(778, 4, 773, 247, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Unidad', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(779, 4, 773, 248, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Sede', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(780, 4, 773, 249, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Área de derecho', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(781, 4, 773, 250, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Categoría de derecho', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(782, 4, 773, 251, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Sub Categoría de derecho', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(783, 4, 773, 252, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Sub Sub Categoría de derecho', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(784, 4, 709, 253, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Tipo motivo de término', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(785, 4, 709, 254, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Objetivos estratégicos', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
INSERT INTO `replet` (`id_replet`, `id_tipo_reporte`, `id_categoria`, `orden`, `orden_temp`, `mime_type`, `fecha_creacion`, `fecha_modificacion`, `descr`, `footer`, `title`, `title_style`, `descr_style`, `footer_style`, `aparece_en_listado`, `despliega_datos_usuario`, `publico`, `owner`, `invisible_con_login`, `id_imagen_asociada`, `autor`, `fecha_publicacion`, `id_default_navigation_relator`, `id_param_visible_replet`) VALUES
	(786, 4, 709, 255, NULL, 'url', '2021-08-02', '2021-08-02', '', '', 'Componente', 'page_title', 'text', 'page_footer', 1, 0, 0, 'root', 0, 0, NULL, NULL, 0, NULL);
/*!40000 ALTER TABLE `replet` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_auto_nav
DROP TABLE IF EXISTS `replet_auto_nav`;
CREATE TABLE IF NOT EXISTS `replet_auto_nav` (
  `id_auto_nav` int(11) NOT NULL AUTO_INCREMENT,
  `id_replet` int(11) DEFAULT '0',
  `id_col` int(11) NOT NULL DEFAULT '0',
  `id_named_relation` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_auto_nav`)
) ENGINE=MyISAM AUTO_INCREMENT=72 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_auto_nav: 6 rows
DELETE FROM `replet_auto_nav`;
/*!40000 ALTER TABLE `replet_auto_nav` DISABLE KEYS */;
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(67, 731, 3, 2);
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(68, 732, 3, 2);
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(69, 733, 3, 2);
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(70, 735, 3, 2);
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(71, 736, 3, 2);
INSERT INTO `replet_auto_nav` (`id_auto_nav`, `id_replet`, `id_col`, `id_named_relation`) VALUES
	(66, 730, 4, 2);
/*!40000 ALTER TABLE `replet_auto_nav` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_clasificacion
DROP TABLE IF EXISTS `replet_clasificacion`;
CREATE TABLE IF NOT EXISTS `replet_clasificacion` (
  `id_replet` int(11) unsigned DEFAULT '0',
  `id_clasificacion_replet` int(5) unsigned DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_clasificacion: 0 rows
DELETE FROM `replet_clasificacion`;
/*!40000 ALTER TABLE `replet_clasificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_clasificacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_clasificaciones_posibles
DROP TABLE IF EXISTS `replet_clasificaciones_posibles`;
CREATE TABLE IF NOT EXISTS `replet_clasificaciones_posibles` (
  `id_clasificacion_replet` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_clasificacion_replet` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_clasificacion_replet`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_clasificaciones_posibles: 0 rows
DELETE FROM `replet_clasificaciones_posibles`;
/*!40000 ALTER TABLE `replet_clasificaciones_posibles` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_clasificaciones_posibles` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_contenido
DROP TABLE IF EXISTS `replet_contenido`;
CREATE TABLE IF NOT EXISTS `replet_contenido` (
  `id_recurso` int(10) unsigned NOT NULL DEFAULT '0',
  `id_plantilla_ref` int(10) unsigned DEFAULT '0',
  `parametros_include` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `invisible_after_login` int(1) unsigned DEFAULT '0',
  `xml_data` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_recurso`),
  UNIQUE KEY `id_recurso` (`id_recurso`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_contenido: 0 rows
DELETE FROM `replet_contenido`;
/*!40000 ALTER TABLE `replet_contenido` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_contenido` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_grafico_tipo_grafico
DROP TABLE IF EXISTS `replet_grafico_tipo_grafico`;
CREATE TABLE IF NOT EXISTS `replet_grafico_tipo_grafico` (
  `id_tipo_grafico` int(5) NOT NULL DEFAULT '0',
  `desc_tipo_grafico` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_tipo_grafico`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_grafico_tipo_grafico: 46 rows
DELETE FROM `replet_grafico_tipo_grafico`;
/*!40000 ALTER TABLE `replet_grafico_tipo_grafico` DISABLE KEYS */;
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(0, 'Barra 3D');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(1, 'Torta Plano');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(2, 'Torta 3D');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(3, 'LÃ­neas');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(4, 'Rango Barra 3D');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(100, 'Area2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(101, 'Bar2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(102, 'Bubble_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(103, 'Column2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(104, 'Column3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(105, 'Doughnut2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(106, 'Doughnut3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(107, 'Line_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(108, 'MSArea_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(109, 'MSBar2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(110, 'MSBar3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(111, 'MSColumn2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(112, 'MSColumn3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(113, 'MSColumn3DLineDY_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(114, 'MSColumnLine3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(115, 'MSCombi2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(116, 'MSCombiDY2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(117, 'MSLine_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(118, 'MSStackedColumn2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(119, 'MSStackedColumn2DLineDY_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(120, 'Pie2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(121, 'Pie3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(122, 'SSGrid_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(123, 'Scatter_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(124, 'ScrollArea2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(125, 'ScrollColumn2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(126, 'ScrollCombi2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(127, 'ScrollCombiDY2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(128, 'ScrollLine2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(129, 'ScrollStackedColumn2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(130, 'StackedArea2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(131, 'StackedBar2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(132, 'StackedBar3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(133, 'StackedColumn2D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(134, 'StackedColumn3D_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(135, 'StackedColumn3DLineDY_FS');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(200, 'Líneas');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(201, 'Columnas');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(202, 'Barras');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(203, 'Torta');
INSERT INTO `replet_grafico_tipo_grafico` (`id_tipo_grafico`, `desc_tipo_grafico`) VALUES
	(204, 'Area');
/*!40000 ALTER TABLE `replet_grafico_tipo_grafico` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_indice
DROP TABLE IF EXISTS `replet_indice`;
CREATE TABLE IF NOT EXISTS `replet_indice` (
  `id_indice` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `temp_key` double NOT NULL DEFAULT '0',
  `owner` varchar(20) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `nombre_indice` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `id_tipo_indice` int(3) unsigned NOT NULL DEFAULT '0',
  `titulo_indice` varchar(50) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `id_categoria` int(5) unsigned NOT NULL DEFAULT '0',
  `contenido_automatico` int(1) unsigned NOT NULL DEFAULT '0',
  `id_plantilla` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id_indice`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_indice: 0 rows
DELETE FROM `replet_indice`;
/*!40000 ALTER TABLE `replet_indice` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_indice` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_indice_detalle
DROP TABLE IF EXISTS `replet_indice_detalle`;
CREATE TABLE IF NOT EXISTS `replet_indice_detalle` (
  `id_indice` int(5) unsigned DEFAULT '0',
  `id_replet` int(10) unsigned DEFAULT '0',
  `orden` int(2) unsigned DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_indice_detalle: 0 rows
DELETE FROM `replet_indice_detalle`;
/*!40000 ALTER TABLE `replet_indice_detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_indice_detalle` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_indice_tipo
DROP TABLE IF EXISTS `replet_indice_tipo`;
CREATE TABLE IF NOT EXISTS `replet_indice_tipo` (
  `id_tipo_indice` int(3) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_indice` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_tipo_indice`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_indice_tipo: 0 rows
DELETE FROM `replet_indice_tipo`;
/*!40000 ALTER TABLE `replet_indice_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_indice_tipo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_named_relation
DROP TABLE IF EXISTS `replet_named_relation`;
CREATE TABLE IF NOT EXISTS `replet_named_relation` (
  `id_named_relation` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `descr` varchar(250) COLLATE latin1_spanish_ci DEFAULT NULL,
  `class` varchar(250) COLLATE latin1_spanish_ci DEFAULT NULL,
  `param` text COLLATE latin1_spanish_ci,
  `caption` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `help` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_named_relation`),
  UNIQUE KEY `idx_replet_named_relation_name` (`nombre`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_named_relation: 2 rows
DELETE FROM `replet_named_relation`;
/*!40000 ALTER TABLE `replet_named_relation` DISABLE KEYS */;
INSERT INTO `replet_named_relation` (`id_named_relation`, `nombre`, `descr`, `class`, `param`, `caption`, `help`) VALUES
	(1, 'Listado requerimientos', '', 'ai.replet_relator.TGrupoReplets', '$R=737, $T=Listado requerimientos', 'Listado requerimientos', '');
INSERT INTO `replet_named_relation` (`id_named_relation`, `nombre`, `descr`, `class`, `param`, `caption`, `help`) VALUES
	(2, 'Opciones de navegación', '', 'ai.replet_relator.TSegunCategoria', '727', 'Ver', '');
/*!40000 ALTER TABLE `replet_named_relation` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_param
DROP TABLE IF EXISTS `replet_param`;
CREATE TABLE IF NOT EXISTS `replet_param` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_param` int(10) NOT NULL DEFAULT '0',
  `param_url` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `descr` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `opcional` int(5) unsigned DEFAULT '0',
  `valor_nulo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_no_nulo` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_nulo` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_tipo_presentacion` int(5) unsigned DEFAULT '0',
  `id_selector` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_descripcion` text COLLATE latin1_spanish_ci,
  `bd_descripcion` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `valor_test` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `asignar_si_no_se_recibe` int(10) unsigned NOT NULL DEFAULT '0',
  `id_param_padre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_no_nulo1` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_nulo1` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_no_nulo2` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sql_nulo2` varchar(2000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `texto_ayuda` text COLLATE latin1_spanish_ci,
  `mostrar_si_no_recibe` tinyint(4) NOT NULL DEFAULT '0',
  `id_param_visible` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_param`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_param: 347 rows
DELETE FROM `replet_param`;
/*!40000 ALTER TABLE `replet_param` DISABLE KEYS */;
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(725, 1, 'correo', 'E-mail contiene', 1, 1, NULL, '(email_notificacion like concat(\'%\'  ,  ?  ,  \'%\') )', '(1=1)', 4, '', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(725, 2, 'id_tipo_notificacion', 'Tipo Notificación', 2, 1, NULL, 'id_tipo_notificacion = ?', '(1=1)', 4, '', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 5, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 4, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 2, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 5, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 4, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 5, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 6, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'rtreq.id = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 7, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 6, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 7, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'rtreq.id = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 6, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'rtreq.id = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 7, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 6, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 7, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 6, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 7, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'r.id_tipo_requirente = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', 'PERSONA_NATURAL', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 8, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(733, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 6, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'rtreq.id = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 6, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'rtreq.id = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 3, 'id_sede', 'Sede', 2, 1, '-1', 's.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'u.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'rts.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 6, 'id_via_ingreso', 'Vía ingreso', 2, 1, '-1', 'rvi.id = ?', '(1=1)', 2, 'req_selector_via_ingreso_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 7, 'id_tipo_persona_requirente', 'Tipo Requirente', 1, 1, '-1', 'r.id_tipo_requirente = ?', '(1=1)', 2, 'req_selector_tipo_persona_requirente_TODOS', '', '', 'PERSONA_NATURAL', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(745, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 4, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 5, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 6, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 7, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 8, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 9, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 10, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 9, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 8, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 7, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 4, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 5, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 6, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(749, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(750, 10, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 10, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 9, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 7, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 8, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 5, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 6, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(748, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(751, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(752, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(754, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(755, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'acd.id_area=?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'acd.id_categoria_derecho=?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'acd.id_area=?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(756, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'acd.id_sub_sub_categoria_derecho=?', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'acd.id_area=?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'acd.id_categoria_derecho=?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(757, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'acd.id_sub_categoria_derecho=?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'acd.id_sub_categoria_derecho=?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'acd.id_categoria_derecho=?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'acd.id_area=?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 3, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(758, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(753, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 4, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 5, 'id_problema_generico', 'Problema genérico', 2, 1, '-1', 'problema_generico.id = ?', '1=1', 2, 'req_selector_tipo_problema_generico_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 6, 'id_macro_proceso', 'Macroproceso', 2, 1, '-1', 'macroproceso.id = ?', '1=1', 2, 'req_selector_macroproceso_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 7, 'id_proceso', 'Proceso', 2, 1, '-1', 'proceso.id=?', '1=1', 2, 'req_selector_proceso_tipo_TODOS', '', '', '-1', 0, 'id_macro_proceso', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 8, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_area=? AND ant.id = antecedente_categoria_derecho.id_antecedente)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 9, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente) 	', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 10, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 11, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM antecedente_categoria_derecho WHERE antecedente_categoria_derecho.id_sub_sub_categoria_derecho=? AND ant.id = antecedente_categoria_derecho.id_antecedente)  	', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(744, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(ant.fecha_creacion) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de creación del antecedente', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(740, 8, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(736, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'derecho.id_area= ?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'derecho.id_categoria_derecho= ?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'derecho.id_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'derecho.id_sub_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'derecho.id_categoria_derecho= ?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'derecho.id_area= ?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'derecho.id_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'derecho.id_sub_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'derecho.id_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'derecho.id_categoria_derecho= ?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'derecho.id_area= ?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'derecho.id_sub_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'derecho.id_area= ?', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'derecho.id_categoria_derecho= ?', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'derecho.id_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 12, 'id_sub_sub_categoria_derecho', 'Sub sub-categoria de derecho', 2, 1, '-1', 'derecho.id_sub_sub_categoria_derecho= ?', '1=1', 2, 'req_selector_sub_sub_categoria_derecho_TODOS', '', '', '-1', 0, 'id_sub_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 6, 'id_tipo_complejidad', 'Tipo de complejidad', 2, 1, '-1', 'complejidad.id=?', '1=1', 2, 'req_selector_tipo_complejidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 7, 'id_tipo_urgencia', 'Urgencia', 1, 1, '-1', 'urgencia_asignacion.id = ?', '1=1', 2, 'req_selector_tipo_urgencia_TODOS', '', '', '', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 8, 'id_componente', 'Componente', 2, 1, '-1', 'componente.id=?', '1=1', 2, 'req_selector_componente_derivacion_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 9, 'id_area_derecho', 'Area de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_area= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_area_derecho_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 10, 'id_categoria_derecho', 'Categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_categoria_derecho_TODOS', '', '', '', 0, 'id_area_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 11, 'id_sub_categoria_derecho', 'Sub categoria de derecho', 2, 1, '-1', 'EXISTS(SELECT 1 FROM categoria_derivacion_caso WHERE categoria_derivacion_caso.id_sub_categoria_derecho= ? AND caso.id = categoria_derivacion_caso.id_caso)', '1=1', 2, 'req_selector_sub_categoria_derecho_TODOS', '', '', '', 0, 'id_categoria_derecho', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 4, 'id_unidad', 'Unidad', 2, 1, '-1', 'unidad.id = ?', '(1=1)', 2, 'req_selector_unidad_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 5, 'id_tipo_solicitud', 'Tipo solicitud', 2, 1, '-1', 'tipo.id = ?', '(1=1)', 2, 'req_selector_tipo_solicitud_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 3, 'id_sede', 'Sede', 2, 1, '-1', 'sede.id = ?', '(1=1)', 2, 'req_selector_sede_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 2, 'mes', 'Mes', 2, 1, '-1', 'MONTH(requerimiento.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_mes_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(764, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(765, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(766, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(760, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(762, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(763, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(761, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(771, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(767, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(768, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(769, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(770, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(759, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(requerimiento.fecha_hora_solicitud) = ?', 'YEAR(requerimiento.fecha_hora_solicitud) = YEAR(CURDATE())', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 1, '', '', '', '', '', 'Año de creación del caso', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(730, 7, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 7, 'id_region', 'Región', 2, 1, '-1', 'reqreg.id = ?', '(1=1)', 2, 'req_selector_region_TODOS', '', '', '-1', 0, '', '', '', '', '', '', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(737, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(735, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(732, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
INSERT INTO `replet_param` (`id_reporte`, `id_param`, `param_url`, `descr`, `tipo`, `opcional`, `valor_nulo`, `sql_no_nulo`, `sql_nulo`, `id_tipo_presentacion`, `id_selector`, `sql_descripcion`, `bd_descripcion`, `valor_test`, `asignar_si_no_se_recibe`, `id_param_padre`, `sql_no_nulo1`, `sql_nulo1`, `sql_no_nulo2`, `sql_nulo2`, `texto_ayuda`, `mostrar_si_no_recibe`, `id_param_visible`) VALUES
	(731, 1, 'ano', 'Año', 2, 1, '-1', 'YEAR(r.fecha_hora_solicitud) = ?', '(1=1)', 2, 'req_selector_ano_requerimiento_TODOS', '', '', '-1', 0, '', '', '', '', '', 'Año de la solicitud', 0, NULL);
/*!40000 ALTER TABLE `replet_param` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_param_tipo_presentacion
DROP TABLE IF EXISTS `replet_param_tipo_presentacion`;
CREATE TABLE IF NOT EXISTS `replet_param_tipo_presentacion` (
  `id_tipo_presentacion` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_presentacion` varchar(50) COLLATE latin1_spanish_ci DEFAULT '0',
  PRIMARY KEY (`id_tipo_presentacion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_param_tipo_presentacion: 0 rows
DELETE FROM `replet_param_tipo_presentacion`;
/*!40000 ALTER TABLE `replet_param_tipo_presentacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_param_tipo_presentacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql
DROP TABLE IF EXISTS `replet_sql`;
CREATE TABLE IF NOT EXISTS `replet_sql` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_conexion` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '1',
  `query` text COLLATE latin1_spanish_ci,
  `maxrows` int(11) DEFAULT '0',
  `id_plantilla_ref` int(11) NOT NULL DEFAULT '0',
  `exporta_xls` tinyint(3) unsigned DEFAULT '1',
  `muestra_titulo` int(1) unsigned NOT NULL DEFAULT '1',
  `muestra_descripcion` int(1) unsigned NOT NULL DEFAULT '1',
  `muestra_parametros` int(1) unsigned NOT NULL DEFAULT '1',
  `muestra_parametros_en_plantilla` int(1) unsigned NOT NULL DEFAULT '1',
  `directo_a_excel` int(1) unsigned NOT NULL DEFAULT '0',
  `titulo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_replet_padre` int(10) unsigned DEFAULT '0',
  `descripcion` text COLLATE latin1_spanish_ci,
  `solo_exporta_csv` int(1) unsigned DEFAULT '0',
  `setear_por_id` int(1) unsigned DEFAULT '1',
  `filtros_antes_de_ejecutar` int(5) unsigned DEFAULT NULL,
  `clase` varchar(255) COLLATE latin1_spanish_ci DEFAULT 'ai.entities.replet.data_sources.TRepletDefaultDataSource',
  `trasponer` int(5) unsigned DEFAULT '0',
  `muestra_con_panel_parametros` int(5) unsigned DEFAULT '0',
  `id_presentacion_parametros` int(5) unsigned DEFAULT '1',
  `relacionar_con_hermanos` int(5) unsigned DEFAULT '0',
  `pdf_exporta` int(10) DEFAULT '0',
  `pdf_orientacion` int(10) DEFAULT '0',
  `pdf_num_pag` int(10) DEFAULT '0',
  `height_ini` int(10) DEFAULT '0',
  `query1` text COLLATE latin1_spanish_ci,
  `query2` text COLLATE latin1_spanish_ci,
  `multi_query` int(11) DEFAULT '0',
  `label_boton_filtrar` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql: 35 rows
DELETE FROM `replet_sql`;
/*!40000 ALTER TABLE `replet_sql` DISABLE KEYS */;
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(725, 'pool_control', 'select \r\n  id_estado_notificacion, id_notificacion, id_tipo_notificacion, fecha_notificacion, fecha_envio, \r\nmensaje, email_notificacion, email_original, parametros_reemplazo, \r\nlogin, error, login_envio\r\n\r\nfrom \r\n notificacion\r\nwhere\r\n  (1=1) and\r\n  $%correo%$ and\r\n  $%id_tipo_notificacion%$\r\norder by fecha_envio desc\r\nlimit 0, 500', 100, 0, 1, 1, 1, 1, 1, 0, 'Listado de notificaciones registradas y su estado', 0, 'NOTA: El informe está limitado a 500 registros, ordenados decrecientemente según fecha de envío.', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 0, 0, 0, 0, 0, 0, '', '', 1, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(730, 'data_sgp', 'SELECT concat(year(r.fecha_hora_solicitud), \'-\', lpad(month(r.fecha_hora_solicitud), 2, \'0\')) as periodo,\r\n year(r.fecha_hora_solicitud) as ano, month(r.fecha_hora_solicitud) as mes, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_tipo_persona_requirente%$\r\n    AND $%id_region%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por período', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(731, 'data_sgp', 'SELECT rts.id as id_tipo_solicitud, rts.nombre as tipo_solicitud, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_tipo_persona_requirente%$\r\n    AND $%id_region%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por tipo de solicitud', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(732, 'data_sgp', 'SELECT s.id as id_sede, s.nombre as sede, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_tipo_persona_requirente%$\r\n    AND $%id_region%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por sede', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(733, 'data_sgp', 'SELECT reqreg.id as id_region, reqreg.nombre as region, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_tipo_persona_requirente%$\r\n    AND $%id_via_ingreso%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por Región', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(735, 'data_sgp', 'SELECT rvi.id as id_via_ingreso, rvi.nombre as via_ingreso, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_tipo_persona_requirente%$\r\n    AND $%id_region%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por Vía de ingreso', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(736, 'data_sgp', 'SELECT rtreq.id as id_tipo_persona_requirente, rtreq.nombre as tipo_requirente, COUNT(*) as total\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       INNER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_region%$\r\nGROUP BY 1, 2\r\nORDER BY 1, 2', 100, 0, 0, 1, 1, 1, 1, 0, 'Requerimientos ingresados por Tipo requirente', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(737, 'data_sgp', 'SELECT r.id as id_requerimiento, r.folio\r\n , r.id_estado, r.fecha_hora_solicitud, r.id_sede, s.nombre AS sede, r.id_unidad, u.nombre AS unidad\r\n , r.id_via_ingreso, rvi.nombre AS via_ingreso\r\n , r.id_tipo_solicitud, rts.nombre AS tipo_solicitud, r.id_tipo_formulario, rtf.nombre AS tipo_formulario\r\n , r.id_tipo_requerimiento_promocion, rtrprom.nombre AS requerimiento_promocion\r\n , r.id_tipo_requirente, rtreq.nombre AS tipo_requirente\r\n , rreq.id_region, reqreg.nombre AS region, rreq.id_comuna, reqcom.nombre AS comuna\r\n , rreq.rut_format, rreq.nombre_completo, rreq.razon_social\r\n , rreqinst.nombre AS institucion_requirente\r\n , r.motivo_contacto\r\n , r.solicitud_realizada\r\n , r.escrito_antes ,\r\n (SELECT GROUP_CONCAT(DISTINCT IF(nna.extranjero = 1,\'Extranjero\',\'Chilena\')  SEPARATOR \'\\r\\n\')\r\n        FROM requerimiento_nna as nna\r\n                 INNER JOIN requerimiento  on nna.id_requerimiento = requerimiento.id\r\n        WHERE requerimiento.id = r.id) as nnas_nacionalidad,\r\n    (SELECT GROUP_CONCAT(DISTINCT pg.nombre  SEPARATOR \'\\r\\n\')\r\n     FROM requerimiento_nna as nna\r\n              INNER JOIN persona_genero pg on nna.id_genero = pg.id\r\n              INNER JOIN requerimiento on nna.id_requerimiento = requerimiento.id\r\n     WHERE requerimiento.id = r.id) as nnas_genero,\r\n    (SELECT GROUP_CONCAT(DISTINCT ptd.nombre  SEPARATOR \'\\r\\n\')\r\n     FROM requerimiento_nna as nna\r\n              INNER JOIN persona_tipo_discapacidad ptd on nna.id_discapacidad_tipo = ptd.id\r\n              INNER JOIN requerimiento  on nna.id_requerimiento = requerimiento.id\r\n     WHERE requerimiento.id = r.id) as nnas_tipos_discapacidades,\r\n    (SELECT GROUP_CONCAT(DISTINCT po.nombre  SEPARATOR \'\\r\\n\')\r\n     FROM requerimiento_nna as nna\r\n              INNER JOIN pueblo_originario po on nna.id_pueblo_originario = po.id\r\n              INNER JOIN requerimiento on nna.id_requerimiento = requerimiento.id\r\n     WHERE requerimiento.id = r.id) as nnas_pueblos_originarios\r\n\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       LEFT OUTER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_region%$\r\n    AND $%id_tipo_persona_requirente%$\r\nORDER BY r.folio', 20, 0, 1, 1, 1, 1, 1, 0, 'Listado de requerimientos', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 630, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(740, 'data_sgp', 'SELECT r.id as id_requerimiento, r.folio\r\n , r.id_estado\r\n \r\n , r.fecha_hora_solicitud, r.fecha_creacion\r\n , CAST(SEC_TO_TIME(TO_SECONDS(r.fecha_creacion) - TO_SECONDS(r.fecha_hora_solicitud)) AS CHAR) AS tiempo_ingreso_req\r\n , r.fecha_respuesta_inmediata\r\n , CAST(SEC_TO_TIME(TO_SECONDS(r.fecha_respuesta_inmediata) - TO_SECONDS(r.fecha_creacion)) AS CHAR) AS tiempo_respuesta_inmediata\r\n , r.fecha_envio_unidad\r\n , CAST(SEC_TO_TIME(TO_SECONDS(r.fecha_envio_unidad) - TO_SECONDS(r.fecha_creacion)) AS CHAR) AS tiempo_envio_unidad\r\n , r.fecha_no_admisibilidad \r\n , c.fecha_derivacion\r\n , CAST(SEC_TO_TIME(TO_SECONDS(c.fecha_derivacion) - TO_SECONDS(r.fecha_envio_unidad)) AS CHAR) AS tiempo_admisibilidad\r\n , CAST((TO_SECONDS(c.fecha_derivacion) - TO_SECONDS(r.fecha_envio_unidad)) AS CHAR) AS tiempo_admisibilidad_sec\r\n , c.fecha_solicitud_cierre\r\n , CAST(SEC_TO_TIME(TO_SECONDS(c.fecha_solicitud_cierre) - TO_SECONDS(r.fecha_envio_unidad)) AS CHAR) AS tiempo_cierre_caso\r\n , c.fecha_cierre\r\n , CAST(SEC_TO_TIME(TO_SECONDS(c.fecha_cierre) - TO_SECONDS(c.fecha_solicitud_cierre)) AS CHAR) AS tiempo_revision_cierre\r\n\r\n, r.id_sede, s.nombre AS sede, r.id_unidad, u.nombre AS unidad\r\n , r.id_via_ingreso, rvi.nombre AS via_ingreso\r\n , r.id_tipo_solicitud, rts.nombre AS tipo_solicitud, r.id_tipo_formulario, rtf.nombre AS tipo_formulario\r\n , r.id_tipo_requerimiento_promocion, rtrprom.nombre AS requerimiento_promocion\r\n , r.id_tipo_requirente, rtreq.nombre AS tipo_requirente\r\n , rreq.id_region, reqreg.nombre AS region, rreq.id_comuna, reqcom.nombre AS comuna\r\n , rreq.rut_format, rreq.nombre_completo, rreq.razon_social\r\n , rreqinst.nombre AS institucion_requirente\r\n, r.motivo_contacto, r.solicitud_realizada, r.escrito_antes\r\n-- , r.*\r\nFROM requerimiento r\r\n       LEFT OUTER JOIN req_tipo_formulario rtf ON (rtf.id = r.id_tipo_formulario)\r\n       LEFT OUTER JOIN req_tipo_solicitud rts ON (rts.id = r.id_tipo_solicitud)\r\n       LEFT OUTER JOIN req_via_ingreso rvi ON (rvi.id = r.id_via_ingreso)\r\n       LEFT OUTER JOIN req_tipo_requirente rtreq ON (rtreq.id = r.id_tipo_requirente)\r\n       LEFT OUTER JOIN req_tipo_requerimiento_promocion rtrprom ON (rtrprom.id = r.id_tipo_requerimiento_promocion)\r\n       LEFT OUTER JOIN unidad u ON (u.id = r.id_unidad)\r\n       LEFT OUTER JOIN sede s ON (s.id = r.id_sede)\r\n       LEFT OUTER JOIN requerimiento_requirente rreq ON (rreq.id_requerimiento = r.id)\r\n       LEFT OUTER JOIN region reqreg ON (reqreg.id = rreq.id_region)\r\n       LEFT OUTER JOIN comuna reqcom ON (reqcom.id = rreq.id_comuna)\r\n       LEFT OUTER JOIN institucion rreqinst ON (rreqinst.id = rreq.id_institucion)\r\n       LEFT OUTER JOIN caso c ON (c.id_requerimiento = r.id)\r\n       \r\nWHERE\r\n    ((r.tipo = \'CASO\') OR (  r.tipo = \'REQUERIMIENTO\' AND (not exists (select 1 from Caso c where c.id_requerimiento = r.id)) ))\r\n    AND $%ano%$\r\n    AND $%mes%$\r\n    AND $%id_sede%$\r\n    AND $%id_unidad%$\r\n    AND $%id_tipo_solicitud%$\r\n    AND $%id_via_ingreso%$\r\n    AND $%id_region%$\r\n    AND $%id_tipo_persona_requirente%$\r\nORDER BY r.folio desc', 20, 0, 1, 1, 1, 1, 1, 0, 'Tiempos de tramitación de requerimientos', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(744, 'data_sgp', 'SELECT ant.folio as folio,\r\n       estado.nombre as estado,\r\n       ant.fecha_creacion as antecedente_fecha_creacion,\r\n       ant.fecha_envio_revision as antecedente_fecha_revision_jefatura,\r\n       ant.fecha_aprobacion as antecedente_fecha_aprobacion,\r\n       CAST(SEC_TO_TIME(TO_SECONDS(ant.fecha_aprobacion) - TO_SECONDS(ant.fecha_envio_revision)) AS CHAR) as  tiempo_revision_jefatura,\r\n       evaluacion.fecha_asignacion_evaluador AS antecedente_fecha_asignacion_evaluador,\r\n       evaluacion.fecha_termino_evaluacion AS antecedente_fecha_termino_evaluacion,\r\n       CAST(SEC_TO_TIME(TO_SECONDS(evaluacion.fecha_termino_evaluacion) - TO_SECONDS(evaluacion.fecha_asignacion_evaluador)) AS CHAR) as  tiempo_evaluacion,\r\n       hallazgo.fecha_creacion AS hallazgo_fecha_creacion,\r\n       hallazgo.fecha_envio_revision AS hallazgo_fecha_envio_revision,\r\n       CAST(SEC_TO_TIME(TO_SECONDS(hallazgo.fecha_envio_revision) - TO_SECONDS(hallazgo.fecha_creacion)) AS CHAR) as  tiempo_elaboracion_hallazgo,\r\n       hallazgo.fecha_aprobacion AS hallazgo_fecha_aprobacion,\r\n       CAST(SEC_TO_TIME(TO_SECONDS(hallazgo.fecha_aprobacion) - TO_SECONDS(hallazgo.fecha_envio_revision)) AS CHAR) as  tiempo_revision_hallazgo\r\nFROM antecedente ant\r\n					INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n                    INNER JOIN antecedente_categoria_derecho acd on ant.id = acd.id_antecedente\r\n                    LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n                    LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n                    LEFT JOIN sede on ant.id_sede = sede.id\r\n                    LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n                    LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n                    LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n                    LEFT JOIN region on ant.id_region = region.id\r\n                    LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n                    LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n                    LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$                    \r\n', 100, 0, 0, 1, 1, 1, 1, 0, 'Tiempos de gestión', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(745, 'data_sgp', 'SELECT ant.folio as folio,\r\n       estado.nombre as estado,\r\n       ant.fecha_creacion as antecedente_fecha_creacion,\r\n       ant.fecha_envio_revision as antecedente_fecha_revision_jefatura,\r\n       ant.fecha_aprobacion as antecedente_fecha_aprobacion,\r\n       ant.fecha_rechazo as antecedente_fecha_rechazo,\r\n       ant.fecha_devolucion as antecedente_fecha_devolucion,\r\n       ant.fecha_devolucion_evaluacion as antecedente_fecha_devolucion_evaluacion,\r\n       ant.fecha_generacion_hallazgo as antecedente_fecha_generacion_hallazgo,\r\n       autor.nombre_completo as autor,\r\n       unidad.nombre as unidad,\r\n       sede.nombre as sede,\r\n       macroproceso.nombre as macroproceso,\r\n       proceso.nombre as proceso,\r\n       ant.fecha_ingreso as antecedente_fecha_ingreso,\r\n       problema_generico.nombre as problema_generico,\r\n       problema_generico.id as problema_generico_id,\r\n       (SELECT DISTINCT GROUP_CONCAT(ad.nombre SEPARATOR \'\\r\\n\') FROM antecedente_categoria_derecho categoria INNER JOIN area_derecho ad ON categoria.id_area = ad.id WHERE categoria.id_antecedente = ant.id GROUP BY categoria.id_antecedente) as areas_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(cd.nombre SEPARATOR \'\\r\\n\') FROM antecedente_categoria_derecho categoria INNER JOIN categoria_derecho cd ON categoria.id_categoria_derecho = cd.id WHERE categoria.id_antecedente = ant.id GROUP BY categoria.id_antecedente) as categorias_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(sc.nombre SEPARATOR \'\\r\\n\') FROM antecedente_categoria_derecho categoria INNER JOIN sub_categoria_derecho sc ON categoria.id_sub_categoria_derecho = sc.id WHERE categoria.id_antecedente = ant.id GROUP BY categoria.id_antecedente) as sub_categorias_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(ssc.nombre SEPARATOR \'\\r\\n\') FROM antecedente_categoria_derecho categoria INNER JOIN sub_sub_categoria_derecho ssc ON categoria.id_sub_sub_categoria_derecho = ssc.id WHERE categoria.id_antecedente = ant.id GROUP BY categoria.id_antecedente) as sub_sub_categorias_de_derecho,\r\n       ant.cantidad_nnas_afectados as antecedente_cantidad_nnas_afectados,\r\n       ant.descripcion_nnas_afectados as antecedente_descripcion_nnas_afectados,\r\n       ant.cantidad_nnas_potencialmente_afectados as antecedente_cantidad_nnas_potencialmente_afectados,\r\n       ant.descripcion_nnas_potencialmente_afectados as antecedente_descripcion_nnas_potencialmente_afectados,\r\n       ant.descripcion_problema_antecedente as antecedente_descripcion_problema_antecedente,\r\n       (CASE WHEN ant.pertenencia_nnasagrupos_vulnerables IS NOT NULL AND ant.pertenencia_nnasagrupos_vulnerables = 1 THEN \'Si\' ELSE \'No\' END) as antecedente_pertenencia_nnas_a_grupos_vulnerables,\r\n       (SELECT DISTINCT GROUP_CONCAT(etiqueta.nombre SEPARATOR \'\\r\\n\') FROM antecedente_etiquetas ant_etiqueta INNER JOIN Etiqueta etiqueta ON ant_etiqueta.id_etiqueta = etiqueta.id WHERE ant_etiqueta.id_antecedente = ant.id GROUP BY ant_etiqueta.id_antecedente) as etiquetas,\r\n       region.nombre as region,\r\n       comuna.nombre as comuna,\r\n       (CASE WHEN ant.rural IS NOT NULL AND ant.rural = 1 THEN \'Rural\' ELSE \'Urbano\' END) as antecedente_rural,\r\n       (SELECT DISTINCT GROUP_CONCAT(institucion.nombre SEPARATOR \'\\r\\n\') FROM antecedente_institucion_involucrada INNER JOIN institucion ON antecedente_institucion_involucrada.id_institucion = institucion.id WHERE antecedente_institucion_involucrada.id_antecedente = ant.id GROUP BY antecedente_institucion_involucrada.id_antecedente) as instituciones_involucradas,\r\n       (SELECT DISTINCT GROUP_CONCAT(CONCAT(i.nombre,\' - \',ip.nombre) SEPARATOR \'\\r\\n\') FROM antecedente_institucion_programa INNER JOIN institucion_programa ip on antecedente_institucion_programa.id_institucion_programa = ip.id INNER JOIN institucion i on ip.id_institucion = i.id WHERE antecedente_institucion_programa.id_antecedente = ant.id GROUP BY antecedente_institucion_programa.id_antecedente) as instituciones_programas,\r\n       ant.causas_problema_identificado as antecedente_causas_problema_identificado,\r\n       ant.efectos_problema_identificado as antecedente_efectos_problema_identificado,\r\n       (SELECT DISTINCT GROUP_CONCAT(producto.nombre SEPARATOR \'\\r\\n\') FROM antecenente_producto INNER JOIN producto on antecenente_producto.id_producto = producto.id WHERE antecenente_producto.id_antecedente = ant.id GROUP BY antecenente_producto.id_antecedente) as productos,\r\n       ant.otro_instrumento_accion_sugerido as antecedente_otro_instrumento_accion_sugerido,\r\n       (SELECT p.nombre_completo FROM profesional p WHERE p.id = evaluacion.id_evaluador) as evaluador,\r\n       evaluacion.fecha_asignacion_evaluador as evaluacion_fecha_asignacion_evaluador,\r\n       evaluacion.fecha_termino_evaluacion as evaluacion_fecha_termino_evaluacion,\r\n       ant.evaluacion_total_puntaje_ficha  as evaluacion_total_puntaje_ficha,\r\n       hallazgo.fecha_creacion as hallazgo_fecha_creacion,\r\n       (SELECT p.nombre_completo FROM profesional p WHERE p.id = hallazgo.id_revisor) as revisor_hallazgo,\r\n       hallazgo.descripcion as hallazgo_descripcion,\r\n       hallazgo.fecha_envio_revision as hallazgo_fecha_envio_revision,\r\n       hallazgo.fecha_aprobacion as hallazgo_fecha_aprobacion,\r\n       hallazgo.fecha_rechazo as hallazgo_fecha_rechazo,\r\n       hallazgo.folio as hallazgo_folio,\r\n       (SELECT unidad.nombre from unidad where unidad.id = hallazgo.id_unidad_responsable) as unidad_responsable,\r\n       hallazgo.fecha_ejecucion as hallazgo_fecha_ejecucion\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$', 100, 0, 1, 1, 1, 1, 1, 0, 'Listado de antecedentes y hallazgos', 0, 'Listado de antecedentes y hallazgo', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 300, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(748, 'data_sgp', 'SELECT problema_generico.id as id_problema,\r\n       CASE WHEN problema_generico.nombre IS NULL THEN \'Sin asignar\' ELSE problema_generico.nombre END as problema_generico,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$         \r\nGROUP BY problema_generico.id,problema_generico.nombre;', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por tipo de problema genérico', 0, 'Antecedentes por tipo de problema genérico', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(749, 'data_sgp', 'SELECT unidad.id as id_undidad,\r\n       unidad.nombre as unidad,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$         \r\nGROUP BY unidad.id,unidad.nombre;', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por unidad', 0, 'Antecedentes por unidad', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(750, 'data_sgp', 'SELECT sede.id as id_sede,\r\n       sede.nombre as sede,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\nGROUP BY sede.id,sede.nombre;', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por sede', 0, 'Antecedentes por sede', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(751, 'data_sgp', 'SELECT macroproceso.id as id_macro_proceso,\r\n       CASE WHEN macroproceso.nombre IS NULL THEN \'Sin asignar\' ELSE macroproceso.nombre END as macro_proceso,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$         \r\nGROUP BY macroproceso.id,macroproceso.nombre;', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por macroproceso', 0, 'Antecedentes por macroproceso', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(752, 'data_sgp', 'SELECT proceso.id as id_proceso,\r\n       CASE WHEN proceso.nombre IS NULL THEN \'Sin asignar\' ELSE proceso.nombre END as proceso,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\nGROUP BY proceso.id,proceso.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por proceso', 0, 'Antecedentes por proceso', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(753, 'data_sgp', 'SELECT region.id as id_region,\r\n       CASE WHEN region.nombre IS NULL THEN \'Sin asignar\' ELSE region.nombre END as region,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\nGROUP BY region.id,region.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por región', 0, 'Antecedentes por región', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(754, 'data_sgp', 'SELECT comuna.id as id_comuna,\r\n       CASE WHEN comuna.nombre IS NULL THEN \'Sin asignar\' ELSE comuna.nombre END as comuna,\r\n       COUNT(ant.id) as total_antecedentes\r\nFROM antecedente ant\r\n         INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n         LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n         LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n         LEFT JOIN sede on ant.id_sede = sede.id\r\n         LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n         LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n         LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n         LEFT JOIN region on ant.id_region = region.id\r\n         LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n         LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n         LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\nWHERE 1=1 \r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_unidad%$\r\nAND $%id_sede%$\r\nAND $%id_problema_generico%$\r\nAND $%id_macro_proceso%$\r\nAND $%id_proceso%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\nGROUP BY comuna.id,comuna.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por comuna', 0, 'Antecedentes por comuna', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(755, 'data_sgp', 'SELECT area_derecho.id,\r\n       area_derecho.nombre,\r\n       COUNT(total_antecedente.id_antecedente) AS total_antecedentes\r\nFROM area_derecho\r\nLEFT JOIN (\r\n           SELECT DISTINCT ant.id as id_antecedente,\r\n                           acd.id_area\r\n           FROM antecedente ant\r\n                    INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n                    INNER JOIN antecedente_categoria_derecho acd on ant.id = acd.id_antecedente\r\n                    LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n                    LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n                    LEFT JOIN sede on ant.id_sede = sede.id\r\n                    LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n                    LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n                    LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n                    LEFT JOIN region on ant.id_region = region.id\r\n                    LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n                    LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n                    LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\n			WHERE 1=1 \r\n			AND $%ano%$\r\n			AND $%mes%$\r\n			AND $%id_unidad%$\r\n			AND $%id_sede%$\r\n			AND $%id_problema_generico%$\r\n			AND $%id_macro_proceso%$\r\n			AND $%id_proceso%$\r\n			AND $%id_area_derecho%$\r\n    ) total_antecedente on total_antecedente.id_area = area_derecho.id\r\nGROUP BY area_derecho.id, area_derecho.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por área de derecho', 0, 'Antecedentes por área de derecho', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(756, 'data_sgp', 'SELECT categoria_derecho.id,\r\n       categoria_derecho.nombre,\r\n       COUNT(total_antecedente.id_antecedente) AS total_antecedentes\r\nFROM categoria_derecho\r\nLEFT JOIN (\r\n           SELECT DISTINCT ant.id as id_antecedente,\r\n                           acd.id_categoria_derecho\r\n           FROM antecedente ant\r\n                    INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n                    INNER JOIN antecedente_categoria_derecho acd on ant.id = acd.id_antecedente\r\n                    LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n                    LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n                    LEFT JOIN sede on ant.id_sede = sede.id\r\n                    LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n                    LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n                    LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n                    LEFT JOIN region on ant.id_region = region.id\r\n                    LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n                    LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n                    LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\n  			WHERE 1=1 \r\n			AND $%ano%$\r\n			AND $%mes%$\r\n			AND $%id_unidad%$\r\n			AND $%id_sede%$\r\n			AND $%id_problema_generico%$\r\n			AND $%id_macro_proceso%$\r\n			AND $%id_proceso%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n    ) total_antecedente on total_antecedente.id_categoria_derecho = categoria_derecho.id\r\nGROUP BY categoria_derecho.id, categoria_derecho.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por categoria de derecho', 0, 'Antecedentes por categoria de derecho', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(757, 'data_sgp', 'SELECT sub_categoria_derecho.id,\r\n       sub_categoria_derecho.nombre,\r\n       COUNT(total_antecedente.id_antecedente) AS total_antecedentes\r\nFROM sub_categoria_derecho\r\nLEFT JOIN (\r\n           SELECT DISTINCT ant.id as id_antecedente,\r\n                           acd.id_sub_categoria_derecho\r\n           FROM antecedente ant\r\n                    INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n                    INNER JOIN antecedente_categoria_derecho acd on ant.id = acd.id_antecedente\r\n                    LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n                    LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n                    LEFT JOIN sede on ant.id_sede = sede.id\r\n                    LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n                    LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n                    LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n                    LEFT JOIN region on ant.id_region = region.id\r\n                    LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n                    LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n                    LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\n  			WHERE 1=1 \r\n			AND $%ano%$\r\n			AND $%mes%$\r\n			AND $%id_unidad%$\r\n			AND $%id_sede%$\r\n			AND $%id_problema_generico%$\r\n			AND $%id_macro_proceso%$\r\n			AND $%id_proceso%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n			AND $%id_sub_categoria_derecho%$\r\n    ) total_antecedente on total_antecedente.id_sub_categoria_derecho = sub_categoria_derecho.id\r\nGROUP BY sub_categoria_derecho.id, sub_categoria_derecho.nombre\r\nHAVING COUNT(total_antecedente.id_antecedente) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por sub-categoria de derecho', 0, 'Antecedentes por sub-categoria de derecho', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(758, 'data_sgp', 'SELECT sub_sub_categoria_derecho.id,\r\n       sub_sub_categoria_derecho.nombre,\r\n       COUNT(total_antecedente.id_antecedente) AS total_antecedentes\r\nFROM sub_sub_categoria_derecho\r\nLEFT JOIN (\r\n           SELECT DISTINCT ant.id as id_antecedente,\r\n                           acd.id_sub_sub_categoria_derecho\r\n           FROM antecedente ant\r\n                    INNER JOIN antecedente_estado estado on ant.id_estado = estado.id\r\n                    INNER JOIN antecedente_categoria_derecho acd on ant.id = acd.id_antecedente\r\n                    LEFT JOIN profesional autor on autor.id = ant.id_autor\r\n                    LEFT JOIN unidad on ant.id_unidad = unidad.id\r\n                    LEFT JOIN sede on ant.id_sede = sede.id\r\n                    LEFT JOIN macroproceso_tipo macroproceso on ant.id_macroproceso = macroproceso.id\r\n                    LEFT JOIN proceso_tipo proceso on ant.id_proceso = proceso.id\r\n                    LEFT JOIN problema_generico_tipo problema_generico on ant.id_problema_generico = problema_generico.id\r\n                    LEFT JOIN region on ant.id_region = region.id\r\n                    LEFT JOIN comuna on ant.id_comuna = comuna.id\r\n                    LEFT JOIN evaluacion_antecedente evaluacion on ant.id = evaluacion.id_antecedente\r\n                    LEFT JOIN hallazgo on ant.id = hallazgo.id_antecedente\r\n    ) total_antecedente on total_antecedente.id_sub_sub_categoria_derecho = sub_sub_categoria_derecho.id\r\nGROUP BY sub_sub_categoria_derecho.id, sub_sub_categoria_derecho.nombre\r\nHAVING COUNT(total_antecedente.id_antecedente) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Antecedentes por Sub-sub-categoria de derecho', 0, 'Antecedentes por Sub-sub-categoria de derecho', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(759, 'data_sgp', 'SELECT YEAR(caso.fecha_creacion) as ano,\r\n       requerimiento.folio AS folio_requerimiento,\r\n       requerimiento.fecha_creacion AS requerimiento_fecha_creacion,\r\n       TO_SECONDS(requerimiento.fecha_creacion) AS requerimiento_fecha_creacion_sec,\r\n       requerimiento.fecha_hora_solicitud AS fecha_hora_solicitud,\r\n       TO_SECONDS(requerimiento.fecha_hora_solicitud) AS fecha_hora_solicitud_sec,\r\n       tipo.nombre AS requerimiento_tipo_solicitud,\r\n       unidad.nombre AS unidad,\r\n       sede.nombre AS sede,\r\n       requerimiento.fecha_envio_unidad AS fecha_envio_unidad,\r\n       TO_SECONDS(requerimiento.fecha_envio_unidad) AS fecha_envio_unidad_sec,\r\n       caso.fecha_derivacion AS fecha_asignacion,\r\n       TO_SECONDS(caso.fecha_derivacion) AS fecha_asignacion_sec,\r\n       caso.fecha_creacion AS fecha_creacion,\r\n       TO_SECONDS(caso.fecha_creacion) as fecha_creacion_sec,\r\n       caso.folio AS folio,\r\n       estado.nombre AS estado,\r\n       complejidad.nombre AS complejidad,\r\n       urgencia_asignacion.nombre AS urgencia_asignacion,\r\n       responsable.nombre_completo AS responsable,\r\n       (SELECT DISTINCT GROUP_CONCAT(ad.nombre SEPARATOR \'\\r\\n\') FROM categoria_derivacion_caso categoria INNER JOIN area_derecho ad ON categoria.id_area = ad.id WHERE categoria.id_caso = caso.id GROUP BY categoria.id_caso) AS areas_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(cd.nombre SEPARATOR \'\\r\\n\') FROM categoria_derivacion_caso categoria INNER JOIN categoria_derecho cd ON categoria.id_categoria_derecho = cd.id WHERE categoria.id_caso = caso.id GROUP BY categoria.id_caso) AS categorias_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(sb.nombre SEPARATOR \'\\r\\n\') FROM categoria_derivacion_caso categoria INNER JOIN sub_categoria_derecho sb ON categoria.id_sub_categoria_derecho = sb.id WHERE categoria.id_caso = caso.id GROUP BY categoria.id_caso) AS sub_categorias_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(ssb.nombre SEPARATOR \'\\r\\n\') FROM categoria_derivacion_caso categoria INNER JOIN sub_sub_categoria_derecho ssb ON categoria.id_sub_sub_categoria_derecho = ssb.id WHERE categoria.id_caso = caso.id GROUP BY categoria.id_caso) AS sub_sub_categorias_de_derecho,\r\n       (SELECT DISTINCT GROUP_CONCAT(objetivo.nombre SEPARATOR \'\\r\\n\') FROM objetivo_estrategico_caso obj_caso INNER JOIN caso_objetivo_estrategico objetivo ON objetivo.id = obj_caso.id_objetivo_estrategico WHERE obj_caso.id_caso = caso.id GROUP BY obj_caso.id_caso) AS objetivos_estrategicos,\r\n       componente.nombre AS componente,\r\n       (SELECT tarea.fecha_termino FROM caso_tarea JOIN tarea ON tarea.id = caso_tarea.id_tarea WHERE tarea.id_estado = \'FINALIZADA\' AND caso_tarea.id_caso = caso.id ORDER BY tarea.fecha_termino LIMIT 1) AS tarea_fecha_primera_gestion,\r\n       TO_SECONDS((SELECT tarea.fecha_termino FROM caso_tarea JOIN tarea ON tarea.id = caso_tarea.id_tarea WHERE tarea.id_estado = \'FINALIZADA\' AND caso_tarea.id_caso = caso.id ORDER BY tarea.fecha_termino LIMIT 1)) as tarea_fecha_primera_gestion_sec,\r\n       (SELECT bitacora.fecha FROM caso_bitacora bitacora WHERE bitacora.id_caso = caso.id ORDER BY bitacora.fecha LIMIT 1) AS bitacora_fecha_primera_gestion,\r\n       TO_SECONDS((SELECT bitacora.fecha FROM caso_bitacora bitacora WHERE bitacora.id_caso = caso.id ORDER BY bitacora.fecha LIMIT 1)) as bitacora_fecha_primera_gestion_sec,\r\n       caso.fecha_solicitud_cierre as fecha_solicitud_cierre,\r\n       TO_SECONDS(caso.fecha_solicitud_cierre) as fecha_solicitud_cierre_sec,\r\n       caso.fecha_cierre as fecha_cierre,\r\n       TO_SECONDS(caso.fecha_cierre) as fecha_cierre_sec,\r\n       (SELECT criticidad.nombre\r\n        FROM caso_ficha_interna_reservada as ficha_interna\r\n                 INNER JOIN caso_complejo_datos ccd on ficha_interna.id_caso_complejo_datos = ccd.id\r\n                 INNER JOIN analisis_criticidad_tipo criticidad on ficha_interna.id_analisis_criticidad_tipo = criticidad.id\r\n        WHERE ficha_interna.vigente=true AND ccd.id = caso.id\r\n        LIMIT 1) as criticidad,\r\n       (SELECT contexto.nombre\r\n        FROM caso_ficha_interna_reservada as ficha_interna\r\n                 INNER JOIN caso_complejo_datos ccd on ficha_interna.id_caso_complejo_datos = ccd.id\r\n                 INNER JOIN contexto_vulneracion contexto on ficha_interna.id_contexto_vulneracion = contexto.id\r\n        WHERE ficha_interna.vigente=true AND ccd.id = caso.id\r\n        LIMIT 1) as contexto_de_vulneracion,\r\n       (SELECT DISTINCT GROUP_CONCAT(tipo_vulneracion.nombre SEPARATOR \'\\r\\n\')\r\n        FROM caso_ficha_interna_reservada as ficha_interna\r\n                 INNER JOIN caso_complejo_datos ccd on ficha_interna.id_caso_complejo_datos = ccd.id\r\n                 INNER JOIN ficha_interna_tipo_vulneracion fitv on ficha_interna.id = fitv.id_ficha\r\n                 INNER JOIN tipo_vulneracion tipo_vulneracion on fitv.id_tipo_vulneracion = tipo_vulneracion.id\r\n        WHERE ficha_interna.vigente=true AND ccd.id = caso.id GROUP BY ficha_interna.id\r\n        LIMIT 1) as tipo_de_vulneracion,\r\n       (SELECT figura_vulneracion.nombre\r\n        FROM caso_ficha_interna_reservada as ficha_interna\r\n                 INNER JOIN caso_complejo_datos ccd on ficha_interna.id_caso_complejo_datos = ccd.id\r\n                 INNER JOIN figura_vulneracion figura_vulneracion on ficha_interna.id_figura_vulneracion = figura_vulneracion.id\r\n        WHERE ficha_interna.vigente=true AND ccd.id = caso.id\r\n        LIMIT 1) as figura_de_vulneracion,\r\n       (SELECT GROUP_CONCAT(DISTINCT IF(nna.extranjero = 1,\'Extranjero\',\'Chilena\')  SEPARATOR \'\\r\\n\')\r\n        FROM requerimiento_nna as nna\r\n                 INNER JOIN requerimiento r on nna.id_requerimiento = r.id\r\n        WHERE r.id = requerimiento.id) as nnas_nacionalidad,\r\n        (SELECT GROUP_CONCAT(DISTINCT pg.nombre  SEPARATOR \'\\r\\n\')\r\n        FROM requerimiento_nna as nna\r\n                 INNER JOIN persona_genero pg on nna.id_genero = pg.id\r\n                 INNER JOIN requerimiento r on nna.id_requerimiento = r.id\r\n        WHERE r.id = requerimiento.id) as nnas_genero,\r\n       (SELECT GROUP_CONCAT(DISTINCT ptd.nombre  SEPARATOR \'\\r\\n\')\r\n        FROM requerimiento_nna as nna\r\n                 INNER JOIN persona_tipo_discapacidad ptd on nna.id_discapacidad_tipo = ptd.id\r\n                 INNER JOIN requerimiento r on nna.id_requerimiento = r.id\r\n        WHERE r.id = requerimiento.id) as nnas_tipos_discapacidades,\r\n       (SELECT GROUP_CONCAT(DISTINCT po.nombre  SEPARATOR \'\\r\\n\')\r\n        FROM requerimiento_nna as nna\r\n                 INNER JOIN pueblo_originario po on nna.id_pueblo_originario = po.id\r\n                 INNER JOIN requerimiento r on nna.id_requerimiento = r.id\r\n        WHERE r.id = requerimiento.id) as nnas_pueblos_originarios\r\nFROM caso\r\n         LEFT JOIN caso_estado estado ON caso.id_estado = estado.id\r\n         LEFT JOIN requerimiento ON caso.id_requerimiento_caso = requerimiento.id\r\n         LEFT JOIN req_tipo_solicitud tipo ON requerimiento.id_tipo_solicitud = tipo.id\r\n         LEFT JOIN unidad ON requerimiento.id_unidad = unidad.id\r\n         LEFT JOIN sede ON requerimiento.id_sede = sede.id\r\n         LEFT JOIN caso_tipo_complejidad complejidad ON caso.id_complejidad_tipo = complejidad.id\r\n         LEFT JOIN urgencia_asignacion_tipo urgencia_asignacion ON caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         LEFT JOIN profesional responsable ON caso.id_responsable = responsable.id\r\n         LEFT JOIN caso_componente_derivacion componente ON caso.id_componente_derivacion = componente.id\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$', 100, 0, 1, 1, 1, 1, 1, 0, 'Listado de casos', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(760, 'data_sgp', 'SELECT complejidad.id,\r\n       complejidad.nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n         \r\nGROUP BY complejidad.id, complejidad.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por tipo de complejidad', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(761, 'data_sgp', 'SELECT responsable.id,\r\n       CASE WHEN responsable.nombre_completo IS NULL THEN \'Sin asignar\' ELSE responsable.nombre_completo END as nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n         \r\nGROUP BY responsable.id, responsable.nombre_completo', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por responsable', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(762, 'data_sgp', 'SELECT urgencia_asignacion.id,\r\n       CASE WHEN urgencia_asignacion.nombre IS NULL THEN \'Sin asignar\' ELSE urgencia_asignacion.nombre END as nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n         \r\nGROUP BY urgencia_asignacion.id, urgencia_asignacion.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por urgencia', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(763, 'data_sgp', 'SELECT componente.id,\r\n       CASE WHEN componente.nombre IS NULL THEN \'Sin asignar\' ELSE componente.nombre END as nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n         \r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$         \r\n         \r\nGROUP BY componente.id, componente.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por componente', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(764, 'data_sgp', 'SELECT unidad.id,\r\n       unidad.nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n\r\nGROUP BY unidad.id, unidad.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por unidad', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(765, 'data_sgp', 'SELECT sede.id,\r\n       sede.nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n\r\nGROUP BY sede.id, sede.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por sede', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(766, 'data_sgp', 'SELECT tipo.id,\r\n       tipo.nombre,\r\n       COUNT(caso.id) as total_casos\r\nFROM caso\r\n         left join caso_estado estado on caso.id_estado = estado.id\r\n         left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n         left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n         left join unidad on requerimiento.id_unidad = unidad.id\r\n         left join sede on requerimiento.id_sede = sede.id\r\n         left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n         left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n         left join profesional responsable on caso.id_responsable = responsable.id\r\n         left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\nWHERE 1=1\r\nAND $%ano%$\r\nAND $%mes%$\r\nAND $%id_sede%$\r\nAND $%id_unidad%$\r\nAND $%id_tipo_solicitud%$\r\nAND $%id_tipo_complejidad%$\r\nAND $%id_tipo_urgencia%$\r\nAND $%id_componente%$\r\nAND $%id_area_derecho%$\r\nAND $%id_categoria_derecho%$\r\nAND $%id_sub_categoria_derecho%$\r\nAND $%id_sub_sub_categoria_derecho%$\r\n\r\nGROUP BY tipo.id, tipo.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por tipo de solicitud', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(767, 'data_sgp', 'SELECT area_derecho.id,\r\n       area_derecho.nombre,\r\n       COUNT(total_casos.id_caso) AS total_casos\r\nFROM area_derecho\r\n         LEFT JOIN (\r\n    SELECT DISTINCT caso.id as id_caso,\r\n                    derecho.id_area\r\n    FROM caso\r\n             inner join  categoria_derivacion_caso derecho on caso.id = derecho.id_caso\r\n             left join caso_estado estado on caso.id_estado = estado.id\r\n             left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n             left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n             left join unidad on requerimiento.id_unidad = unidad.id\r\n             left join sede on requerimiento.id_sede = sede.id\r\n             left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n             left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n             left join profesional responsable on caso.id_responsable = responsable.id\r\n             left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n     WHERE 1=1\r\n			AND $%ano%$\r\n           	AND $%mes%$\r\n			AND $%id_sede%$\r\n			AND $%id_unidad%$\r\n			AND $%id_tipo_solicitud%$\r\n			AND $%id_tipo_complejidad%$\r\n			AND $%id_tipo_urgencia%$\r\n			AND $%id_componente%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n			AND $%id_sub_categoria_derecho%$\r\n			AND $%id_sub_sub_categoria_derecho%$      \r\n) total_casos on total_casos.id_area = area_derecho.id\r\nGROUP BY area_derecho.id, area_derecho.nombre\r\nHAVING COUNT(total_casos.id_caso) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por area de derecho', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(768, 'data_sgp', 'SELECT categoria_derecho.id,\r\n       categoria_derecho.nombre,\r\n       COUNT(total_casos.id_caso) AS total_casos\r\nFROM categoria_derecho\r\n         LEFT JOIN (\r\n    SELECT DISTINCT caso.id as id_caso,\r\n                    derecho.id_categoria_derecho\r\n    FROM caso\r\n             inner join  categoria_derivacion_caso derecho on caso.id = derecho.id_caso\r\n             left join caso_estado estado on caso.id_estado = estado.id\r\n             left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n             left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n             left join unidad on requerimiento.id_unidad = unidad.id\r\n             left join sede on requerimiento.id_sede = sede.id\r\n             left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n             left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n             left join profesional responsable on caso.id_responsable = responsable.id\r\n             left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n      WHERE 1=1\r\n			AND $%ano%$\r\n           	AND $%mes%$\r\n			AND $%id_sede%$\r\n			AND $%id_unidad%$\r\n			AND $%id_tipo_solicitud%$\r\n			AND $%id_tipo_complejidad%$\r\n			AND $%id_tipo_urgencia%$\r\n			AND $%id_componente%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n			AND $%id_sub_categoria_derecho%$\r\n			AND $%id_sub_sub_categoria_derecho%$ \r\n) total_casos on total_casos.id_categoria_derecho = categoria_derecho.id\r\nGROUP BY categoria_derecho.id, categoria_derecho.nombre\r\nHAVING COUNT(total_casos.id_caso) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por categoría de derecho', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(769, 'data_sgp', 'SELECT sub_categoria_derecho.id,\r\n       sub_categoria_derecho.nombre,\r\n       COUNT(total_casos.id_caso) AS total_casos\r\nFROM sub_categoria_derecho\r\n         LEFT JOIN (\r\n    SELECT DISTINCT caso.id as id_caso,\r\n                    derecho.id_sub_categoria_derecho\r\n    FROM caso\r\n             inner join  categoria_derivacion_caso derecho on caso.id = derecho.id_caso\r\n             left join caso_estado estado on caso.id_estado = estado.id\r\n             left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n             left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n             left join unidad on requerimiento.id_unidad = unidad.id\r\n             left join sede on requerimiento.id_sede = sede.id\r\n             left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n             left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n             left join profesional responsable on caso.id_responsable = responsable.id\r\n             left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n     WHERE 1=1\r\n			AND $%ano%$\r\n           	AND $%mes%$\r\n			AND $%id_sede%$\r\n			AND $%id_unidad%$\r\n			AND $%id_tipo_solicitud%$\r\n			AND $%id_tipo_complejidad%$\r\n			AND $%id_tipo_urgencia%$\r\n			AND $%id_componente%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n			AND $%id_sub_categoria_derecho%$\r\n			AND $%id_sub_sub_categoria_derecho%$            \r\n) total_casos on total_casos.id_sub_categoria_derecho = sub_categoria_derecho.id\r\nGROUP BY sub_categoria_derecho.id, sub_categoria_derecho.nombre\r\nHAVING COUNT(total_casos.id_caso) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por sub-categoría de derecho', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(770, 'data_sgp', 'SELECT sub_sub_categoria_derecho.id,\r\n       sub_sub_categoria_derecho.nombre,\r\n       COUNT(total_casos.id_caso) AS total_casos\r\nFROM sub_sub_categoria_derecho\r\n         LEFT JOIN (\r\n    SELECT DISTINCT caso.id as id_caso,\r\n                    derecho.id_sub_sub_categoria_derecho\r\n    FROM caso\r\n             inner join  categoria_derivacion_caso derecho on caso.id = derecho.id_caso\r\n             left join caso_estado estado on caso.id_estado = estado.id\r\n             left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n             left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n             left join unidad on requerimiento.id_unidad = unidad.id\r\n             left join sede on requerimiento.id_sede = sede.id\r\n             left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n             left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n             left join profesional responsable on caso.id_responsable = responsable.id\r\n             left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n     WHERE 1=1\r\n			AND $%ano%$\r\n           	AND $%mes%$\r\n			AND $%id_sede%$\r\n			AND $%id_unidad%$\r\n			AND $%id_tipo_solicitud%$\r\n			AND $%id_tipo_complejidad%$\r\n			AND $%id_tipo_urgencia%$\r\n			AND $%id_componente%$\r\n			AND $%id_area_derecho%$\r\n			AND $%id_categoria_derecho%$\r\n			AND $%id_sub_categoria_derecho%$\r\n			AND $%id_sub_sub_categoria_derecho%$            \r\n) total_casos on total_casos.id_sub_sub_categoria_derecho = sub_sub_categoria_derecho.id\r\nGROUP BY sub_sub_categoria_derecho.id, sub_sub_categoria_derecho.nombre\r\nHAVING COUNT(total_casos.id_caso) > 0', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por sub-sub-categoría de derecho', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
INSERT INTO `replet_sql` (`id_reporte`, `id_conexion`, `query`, `maxrows`, `id_plantilla_ref`, `exporta_xls`, `muestra_titulo`, `muestra_descripcion`, `muestra_parametros`, `muestra_parametros_en_plantilla`, `directo_a_excel`, `titulo`, `id_replet_padre`, `descripcion`, `solo_exporta_csv`, `setear_por_id`, `filtros_antes_de_ejecutar`, `clase`, `trasponer`, `muestra_con_panel_parametros`, `id_presentacion_parametros`, `relacionar_con_hermanos`, `pdf_exporta`, `pdf_orientacion`, `pdf_num_pag`, `height_ini`, `query1`, `query2`, `multi_query`, `label_boton_filtrar`) VALUES
	(771, 'data_sgp', 'select criticidad.id,\r\n       criticidad.nombre,\r\n       count(total_criticidad.id_caso) total_casos\r\nfrom analisis_criticidad_tipo criticidad\r\nleft join (SELECT criticidad.id,\r\n                  criticidad.nombre,\r\n                  ccd.id as id_caso\r\n           FROM caso_ficha_interna_reservada as ficha_interna\r\n                    INNER JOIN analisis_criticidad_tipo criticidad on ficha_interna.id_analisis_criticidad_tipo = criticidad.id\r\n                    INNER JOIN caso_complejo_datos ccd on ficha_interna.id_caso_complejo_datos = ccd.id\r\n                    INNER JOIN caso caso on ccd.id = caso.id\r\n                    left join caso_estado estado on caso.id_estado = estado.id\r\n                    left join requerimiento on caso.id_requerimiento_caso = requerimiento.id\r\n                    left join req_tipo_solicitud tipo on requerimiento.id_tipo_solicitud = tipo.id\r\n                    left join unidad on requerimiento.id_unidad = unidad.id\r\n                    left join sede on requerimiento.id_sede = sede.id\r\n                    left join caso_tipo_complejidad complejidad on caso.id_complejidad_tipo = complejidad.id\r\n                    left join urgencia_asignacion_tipo urgencia_asignacion on caso.id_urgencia_asignacion = urgencia_asignacion.id\r\n                    left join profesional responsable on caso.id_responsable = responsable.id\r\n                    left join caso_componente_derivacion componente on caso.id_componente_derivacion = componente.id\r\n           WHERE ficha_interna.vigente=true \r\n				AND $%ano%$\r\n           		AND $%mes%$\r\n				AND $%id_sede%$\r\n				AND $%id_unidad%$\r\n				AND $%id_tipo_solicitud%$\r\n				AND $%id_tipo_complejidad%$\r\n				AND $%id_tipo_urgencia%$\r\n				AND $%id_componente%$\r\n				AND $%id_area_derecho%$\r\n				AND $%id_categoria_derecho%$\r\n				AND $%id_sub_categoria_derecho%$\r\n				AND $%id_sub_sub_categoria_derecho%$\r\n          ) total_criticidad on total_criticidad.id = criticidad.id\r\nGROUP BY criticidad.id, criticidad.nombre', 100, 0, 1, 1, 1, 1, 1, 0, 'Casos por criticidad', 0, '', 0, 1, 0, 'ai.entities.replet.data_sources.TRepletDefaultDataSource', 0, 0, 1, 0, 0, 0, 0, 0, '', '', 0, 'Filtrar');
/*!40000 ALTER TABLE `replet_sql` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_columna
DROP TABLE IF EXISTS `replet_sql_columna`;
CREATE TABLE IF NOT EXISTS `replet_sql_columna` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_columna` int(11) NOT NULL DEFAULT '0',
  `nombre_columna` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `caption` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `valor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `formato_despliegue` int(11) DEFAULT '1',
  `cantidad_decimales` int(11) DEFAULT '0',
  `tipo_total` int(11) DEFAULT '0',
  `header_style` varchar(30) COLLATE latin1_spanish_ci DEFAULT 'report_header',
  `cell_style1` varchar(30) COLLATE latin1_spanish_ci DEFAULT 'report_cell1',
  `cell_style2` varchar(30) COLLATE latin1_spanish_ci DEFAULT 'report_cell2',
  `id_reporte_ref` int(11) DEFAULT NULL,
  `ordenable` int(1) DEFAULT '0',
  `caption_agrupacion` varchar(255) COLLATE latin1_spanish_ci DEFAULT '',
  `cantidad_agrupar` int(3) unsigned DEFAULT '0',
  `no_wrap` int(1) unsigned NOT NULL DEFAULT '0',
  `reporte_ref_target` varchar(60) COLLATE latin1_spanish_ci DEFAULT NULL,
  `invisible` int(1) unsigned DEFAULT '0',
  `auto_nav` int(1) unsigned DEFAULT '0',
  `agrupar_despliegue` int(5) unsigned DEFAULT '0',
  `trasposicion_caption_grupo` int(5) unsigned DEFAULT '0',
  `trasposicion_tipo_columna` int(5) unsigned DEFAULT '0',
  `tooltip` text COLLATE latin1_spanish_ci NOT NULL,
  `width_imagen` varchar(255) COLLATE latin1_spanish_ci DEFAULT '',
  `height_imagen` varchar(255) COLLATE latin1_spanish_ci DEFAULT '',
  `path_imagen` varchar(255) COLLATE latin1_spanish_ci DEFAULT '',
  `width_ini` int(10) NOT NULL DEFAULT '0',
  `link_dinamico` int(10) NOT NULL DEFAULT '0',
  `formatear_miles` int(1) NOT NULL DEFAULT '1',
  `max_length` int(10) unsigned NOT NULL DEFAULT '0',
  `add_to_url_reporte_ref` int(11) NOT NULL DEFAULT '0',
  `escape_html` tinyint(4) NOT NULL DEFAULT '1',
  `abrir_reporte_ref_modal` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_reporte`,`id_columna`),
  KEY `id_reporte_ref` (`id_reporte_ref`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_columna: 282 rows
DELETE FROM `replet_sql_columna`;
/*!40000 ALTER TABLE `replet_sql_columna` DISABLE KEYS */;
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 11, 'error', 'Error', 1, 'error', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 12, 'login_envio', 'Login Envio', 1, 'login_envio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 10, 'login', 'Login', 1, 'login', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 8, 'email_original', 'Email Original', 1, 'email_original', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 9, 'parametros_reemplazo', 'Parametros Reemplazo', 1, 'parametros_reemplazo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 7, 'email_notificacion', 'Email Notificacion', 1, 'email_notificacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 5, 'fecha_envio', 'Fecha Envio', 1, 'fecha_envio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 6, 'mensaje', 'Mensaje', 1, 'mensaje', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 3, 'id_tipo_notificacion', 'Id_tipo_notificacion', 1, 'id_tipo_notificacion', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 4, 'fecha_notificacion', 'Fecha Notificacion', 1, 'fecha_notificacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 2, 'id_notificacion', 'Id_notificacion', 1, 'id_notificacion', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(725, 1, 'id_estado_notificacion', 'Id_estado_notificacion', 1, 'id_estado_notificacion', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(730, 2, 'ano', 'Ano', 1, 'ano', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(730, 3, 'mes', 'Mes', 1, 'mes', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(730, 4, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(731, 2, 'tipo_solicitud', 'Tipo Solicitud', 1, 'tipo_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(731, 1, 'id_tipo_solicitud', 'Id_tipo_solicitud', 1, 'id_tipo_solicitud', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(731, 3, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(732, 3, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(732, 2, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(732, 1, 'id_sede', 'Id_sede', 1, 'id_sede', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(733, 2, 'region', 'Region', 1, 'region', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(733, 3, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(735, 3, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(735, 2, 'via_ingreso', 'Via Ingreso', 1, 'via_ingreso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(735, 1, 'id_via_ingreso', 'Id_via_ingreso', 1, 'id_via_ingreso', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(736, 2, 'tipo_requirente', 'Tipo Requirente', 1, 'tipo_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(730, 1, 'periodo', 'Período', 1, 'periodo', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 30, 'nnas_nacionalidad', 'Nnas Nacionalidad', 1, 'nnas_nacionalidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 29, 'escrito_antes', 'Escrito Antes', 1, 'escrito_antes', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 28, 'solicitud_realizada', 'Solicitud Realizada', 1, 'solicitud_realizada', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 27, 'motivo_contacto', 'Motivo Contacto', 1, 'motivo_contacto', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 26, 'institucion_requirente', 'Institucion Requirente', 1, 'institucion_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 25, 'razon_social', 'Razon Social', 1, 'razon_social', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 24, 'nombre_completo', 'Nombre Completo', 1, 'nombre_completo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 23, 'rut_format', 'Rut Format', 1, 'rut_format', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 22, 'comuna', 'Comuna', 1, 'comuna', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 21, 'id_comuna', 'Id_comuna', 1, 'id_comuna', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 20, 'region', 'Region', 1, 'region', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 19, 'id_region', 'Id_region', 1, 'id_region', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 18, 'tipo_requirente', 'Tipo Requirente', 1, 'tipo_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 17, 'id_tipo_requirente', 'Id_tipo_requirente', 1, 'id_tipo_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 16, 'requerimiento_promocion', 'Requerimiento Promocion', 1, 'requerimiento_promocion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 15, 'id_tipo_requerimiento_promocion', 'Id_tipo_requerimiento_promocion', 1, 'id_tipo_requerimiento_promocion', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 14, 'tipo_formulario', 'Tipo Formulario', 1, 'tipo_formulario', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 13, 'id_tipo_formulario', 'Id_tipo_formulario', 1, 'id_tipo_formulario', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 12, 'tipo_solicitud', 'Tipo Solicitud', 1, 'tipo_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 11, 'id_tipo_solicitud', 'Id_tipo_solicitud', 1, 'id_tipo_solicitud', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 10, 'via_ingreso', 'Via Ingreso', 1, 'via_ingreso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 9, 'id_via_ingreso', 'Id_via_ingreso', 1, 'id_via_ingreso', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 8, 'unidad', 'Unidad', 1, 'unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(736, 3, 'total', 'Total', 1, 'total', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 5, 'id_sede', 'Id_sede', 1, 'id_sede', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 34, 'region', 'Region', 1, 'region', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 35, 'id_comuna', 'Id_comuna', 1, 'id_comuna', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 33, 'id_region', 'Id_region', 1, 'id_region', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 32, 'tipo_requirente', 'Tipo Requirente', 1, 'tipo_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 31, 'id_tipo_requirente', 'Id_tipo_requirente', 1, 'id_tipo_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 30, 'requerimiento_promocion', 'Requerimiento Promocion', 1, 'requerimiento_promocion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 29, 'id_tipo_requerimiento_promocion', 'Id_tipo_requerimiento_promocion', 1, 'id_tipo_requerimiento_promocion', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 28, 'tipo_formulario', 'Tipo Formulario', 1, 'tipo_formulario', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 27, 'id_tipo_formulario', 'Id_tipo_formulario', 1, 'id_tipo_formulario', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 26, 'tipo_solicitud', 'Tipo Solicitud', 1, 'tipo_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 25, 'id_tipo_solicitud', 'Id_tipo_solicitud', 1, 'id_tipo_solicitud', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 24, 'via_ingreso', 'Via Ingreso', 1, 'via_ingreso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 23, 'id_via_ingreso', 'Id_via_ingreso', 1, 'id_via_ingreso', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 22, 'unidad', 'Unidad', 1, 'unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 21, 'id_unidad', 'Id_unidad', 1, 'id_unidad', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 20, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 19, 'id_sede', 'Id_sede', 1, 'id_sede', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 18, 'fecha_cierre', 'Fecha Cierre', 1, 'fecha_cierre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 17, 'fecha_solicitud_cierre', 'Fecha Sol. Cierre', 1, 'fecha_solicitud_cierre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 16, 'tiempo_revision_cierre', 'Tiempo Revisión Sol. Cierre', 1, 'tiempo_revision_cierre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 15, 'tiempo_cierre_caso', 'Tiempo Cierre Caso', 1, 'tiempo_cierre_caso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 14, 'tiempo_admisibilidad', 'Tiempo Admisibilidad', 1, 'tiempo_admisibilidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 13, 'tiempo_admisibilidad_sec', 'Tiempo Admisibilidad Sec', 1, 'tiempo_admisibilidad_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 12, 'fecha_derivacion', 'Fecha Derivación a Profesional', 1, 'fecha_derivacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 11, 'fecha_no_admisibilidad', 'Fecha No Admisible', 1, 'fecha_no_admisibilidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 10, 'tiempo_envio_unidad', 'Tiempo Envío a Unidad', 1, 'tiempo_envio_unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 9, 'fecha_envio_unidad', 'Fecha Envío a Unidad', 1, 'fecha_envio_unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 8, 'tiempo_respuesta_inmediata', 'Tiempo Resp. Inmediata', 1, 'tiempo_respuesta_inmediata', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 7, 'fecha_respuesta_inmediata', 'Fecha Respuesta Inmediata', 1, 'fecha_respuesta_inmediata', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 6, 'tiempo_ingreso_req', 'Tiempo ingreso requerimiento', 1, 'tiempo_ingreso_req', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 5, 'fecha_creacion', 'Fecha ingreso (creación)', 1, 'fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 4, 'fecha_hora_solicitud', 'Fecha Hora Solicitud', 1, 'fecha_hora_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 2, 'folio', 'Folio', 1, 'folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 3, 'id_estado', 'Id_estado', 1, 'id_estado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 1, 'id_requerimiento', 'Id_requerimiento', 1, 'id_requerimiento', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(736, 1, 'id_tipo_persona_requirente', 'id_tipo_persona_requirente', 1, 'id_tipo_persona_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(733, 1, 'id_region', 'Id_region', 1, 'id_region', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 31, 'nnas_genero', 'Nnas Genero', 1, 'nnas_genero', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 2, 'estado', 'Estado', 1, 'estado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 3, 'antecedente_fecha_creacion', 'Antecedente Fecha Creacion', 1, 'antecedente_fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 4, 'antecedente_fecha_revision_jefatura', 'Antecedente Fecha Envio Revision Jefatura', 1, 'antecedente_fecha_revision_jefatura', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 5, 'antecedente_fecha_aprobacion', 'Antecedente Fecha Aprobacion', 1, 'antecedente_fecha_aprobacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 6, 'tiempo_revision_jefatura', 'Tiempo Revision Jefatura', 1, 'tiempo_revision_jefatura', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 7, 'antecedente_fecha_asignacion_evaluador', 'Antecedente Fecha Asignacion Evaluador', 1, 'antecedente_fecha_asignacion_evaluador', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 8, 'antecedente_fecha_termino_evaluacion', 'Antecedente Fecha Termino Evaluacion', 1, 'antecedente_fecha_termino_evaluacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 9, 'tiempo_evaluacion', 'Tiempo Evaluacion', 1, 'tiempo_evaluacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 10, 'hallazgo_fecha_creacion', 'Hallazgo Fecha Creacion', 1, 'hallazgo_fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 11, 'hallazgo_fecha_envio_revision', 'Hallazgo Fecha Envio Revision', 1, 'hallazgo_fecha_envio_revision', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 12, 'tiempo_elaboracion_hallazgo', 'Tiempo Elaboracion Hallazgo', 1, 'tiempo_elaboracion_hallazgo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 13, 'hallazgo_fecha_aprobacion', 'Hallazgo Fecha Aprobacion', 1, 'hallazgo_fecha_aprobacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 14, 'tiempo_revision_hallazgo', 'Tiempo Revision Hallazgo', 1, 'tiempo_revision_hallazgo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(748, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(748, 2, 'problema_generico', 'Problema Generico', 1, 'problema_generico', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 48, 'hallazgo_folio', 'Hallazgo Folio', 1, 'hallazgo_folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 49, 'unidad_responsable', 'Unidad Responsable', 1, 'unidad_responsable', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 50, 'hallazgo_fecha_ejecucion', 'Hallazgo Fecha Ejecucion', 1, 'hallazgo_fecha_ejecucion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 86, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(748, 1, 'id_problema', 'Id_problema', 1, 'id_problema', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 45, 'hallazgo_fecha_envio_revision', 'Hallazgo Fecha Envio Revision', 1, 'hallazgo_fecha_envio_revision', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 46, 'hallazgo_fecha_aprobacion', 'Hallazgo Fecha Aprobacion', 1, 'hallazgo_fecha_aprobacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 47, 'hallazgo_fecha_rechazo', 'Hallazgo Fecha Rechazo', 1, 'hallazgo_fecha_rechazo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 44, 'hallazgo_descripcion', 'Hallazgo Descripcion', 1, 'hallazgo_descripcion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 43, 'revisor_hallazgo', 'Revisor Hallazgo', 1, 'revisor_hallazgo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 42, 'hallazgo_fecha_creacion', 'Hallazgo Fecha Creacion', 1, 'hallazgo_fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 41, 'evaluacion_total_puntaje_ficha', 'Evaluacion Total Puntaje Ficha', 1, 'evaluacion_total_puntaje_ficha', 2, 2, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 42, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 36, 'productos', 'Productos', 1, 'productos', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 37, 'antecedente_otro_instrumento_accion_sugerido', 'Antecedente Otro Instrumento Accion Sugerido', 1, 'antecedente_otro_instrumento_accion_sugerido', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 38, 'evaluador', 'Evaluador', 1, 'evaluador', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 39, 'evaluacion_fecha_asignacion_evaluador', 'Evaluacion Fecha Asignacion Evaluador', 1, 'evaluacion_fecha_asignacion_evaluador', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 40, 'evaluacion_fecha_termino_evaluacion', 'Evaluacion Fecha Termino Evaluacion', 1, 'evaluacion_fecha_termino_evaluacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 35, 'antecedente_efectos_problema_identificado', 'Antecedente Efectos Problema Identificado', 1, 'antecedente_efectos_problema_identificado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 33, 'instituciones_programas', 'Instituciones Programas', 1, 'instituciones_programas', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 34, 'antecedente_causas_problema_identificado', 'Antecedente Causas Problema Identificado', 1, 'antecedente_causas_problema_identificado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 30, 'comuna', 'Comuna', 1, 'comuna', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 31, 'antecedente_rural', 'Antecedente Rural', 1, 'antecedente_rural', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 32, 'instituciones_involucradas', 'Instituciones Involucradas', 1, 'instituciones_involucradas', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 29, 'region', 'Region', 1, 'region', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 28, 'etiquetas', 'Etiquetas', 1, 'etiquetas', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 27, 'antecedente_pertenencia_nnas_a_grupos_vulnerables', 'Antecedente Pertenencia Nnas A Grupos Vulnerables', 1, 'antecedente_pertenencia_nnas_a_grupos_vulnerables', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 12, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 13, 'macroproceso', 'Macroproceso', 1, 'macroproceso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 14, 'proceso', 'Proceso', 1, 'proceso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 15, 'antecedente_fecha_ingreso', 'Antecedente Fecha Ingreso', 1, 'antecedente_fecha_ingreso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 16, 'problema_generico', 'Problema Generico', 1, 'problema_generico', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 17, 'problema_generico_id', 'Problema Generico Id', 1, 'problema_generico_id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 42, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 18, 'areas_de_derecho', 'Areas De Derecho', 1, 'areas_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 19, 'categorias_de_derecho', 'Categorias De Derecho', 1, 'categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 20, 'sub_categorias_de_derecho', 'Sub Categorias De Derecho', 1, 'sub_categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 21, 'sub_sub_categorias_de_derecho', 'Sub Sub Categorias De Derecho', 1, 'sub_sub_categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 22, 'antecedente_cantidad_nnas_afectados', 'Antecedente Cantidad Nnas Afectados', 1, 'antecedente_cantidad_nnas_afectados', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 42, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 23, 'antecedente_descripcion_nnas_afectados', 'Antecedente Descripcion Nnas Afectados', 1, 'antecedente_descripcion_nnas_afectados', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 24, 'antecedente_cantidad_nnas_potencialmente_afectados', 'Antecedente Cantidad Nnas Potencialmente Afectados', 1, 'antecedente_cantidad_nnas_potencialmente_afectados', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 42, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 25, 'antecedente_descripcion_nnas_potencialmente_afectados', 'Antecedente Descripcion Nnas Potencialmente Afectados', 1, 'antecedente_descripcion_nnas_potencialmente_afectados', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 20, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 26, 'antecedente_descripcion_problema_antecedente', 'Antecedente Descripcion Problema Antecedente', 1, 'antecedente_descripcion_problema_antecedente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 8, 'antecedente_fecha_devolucion_evaluacion', 'Antecedente Fecha Devolucion Evaluacion', 1, 'antecedente_fecha_devolucion_evaluacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 9, 'antecedente_fecha_generacion_hallazgo', 'Antecedente Fecha Generacion Hallazgo', 1, 'antecedente_fecha_generacion_hallazgo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 10, 'autor', 'Autor', 1, 'autor', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 11, 'unidad', 'Unidad', 1, 'unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 6, 'antecedente_fecha_rechazo', 'Antecedente Fecha Rechazo', 1, 'antecedente_fecha_rechazo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 7, 'antecedente_fecha_devolucion', 'Antecedente Fecha Devolucion', 1, 'antecedente_fecha_devolucion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 2, 'estado', 'Estado', 1, 'estado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 3, 'antecedente_fecha_creacion', 'Antecedente Fecha Creacion', 1, 'antecedente_fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 5, 'antecedente_fecha_aprobacion', 'Antecedente Fecha Aprobacion', 1, 'antecedente_fecha_aprobacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 4, 'antecedente_fecha_revision_jefatura', 'Antecedente Fecha Revision Jefatura', 1, 'antecedente_fecha_revision_jefatura', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(745, 1, 'folio', 'Folio', 1, 'folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 71, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(749, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(749, 1, 'id_undidad', 'Id_undidad', 1, 'id_undidad', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(749, 2, 'unidad', 'Unidad', 1, 'unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(750, 2, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(750, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(751, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(751, 2, 'macro_proceso', 'Macro Proceso', 1, 'macro_proceso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(752, 2, 'proceso', 'Proceso', 1, 'proceso', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(752, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(753, 2, 'region', 'Region', 1, 'region', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(753, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(754, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(754, 2, 'comuna', 'Comuna', 1, 'comuna', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(754, 1, 'id_comuna', 'Id_comuna', 1, 'id_comuna', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(755, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(755, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(756, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(756, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(756, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(757, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(757, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(758, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(758, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(758, 3, 'total_antecedentes', 'Total Antecedentes', 1, 'total_antecedentes', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(744, 1, 'folio', 'Folio', 1, 'folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(751, 1, 'id_macro_proceso', 'Id_macro_proceso', 1, 'id_macro_proceso', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(752, 1, 'id_proceso', 'Id_proceso', 1, 'id_proceso', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(750, 1, 'id_sede', 'Id_sede', 1, 'id_sede', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(753, 1, 'id_region', 'Id_region', 1, 'id_region', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(755, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(757, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 32, 'nnas_tipos_discapacidades', 'Nnas Tipos Discapacidades', 1, 'nnas_tipos_discapacidades', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 7, 'requerimiento_tipo_solicitud', 'Requerimiento Tipo Solicitud', 1, 'requerimiento_tipo_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 6, 'fecha_hora_solicitud_sec', 'Fecha Hora Solicitud Sec', 1, 'fecha_hora_solicitud_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 36, 'comuna', 'Comuna', 1, 'comuna', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 37, 'rut_format', 'Rut Format', 1, 'rut_format', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 38, 'nombre_completo', 'Nombre Completo', 1, 'nombre_completo', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 21, 'areas_de_derecho', 'Areas De Derecho', 1, 'areas_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 20, 'responsable', 'Responsable', 1, 'responsable', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 19, 'urgencia_asignacion', 'Urgencia Asignacion', 1, 'urgencia_asignacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 17, 'estado', 'Estado', 1, 'estado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 18, 'complejidad', 'Complejidad', 1, 'complejidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 16, 'folio', 'Folio', 1, 'folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 15, 'fecha_creacion_sec', 'Fecha Creacion Sec', 1, 'fecha_creacion_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 14, 'fecha_creacion', 'Fecha Creacion', 1, 'fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 13, 'fecha_asignacion_sec', 'Fecha Asignacion Sec', 1, 'fecha_asignacion_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 12, 'fecha_asignacion', 'Fecha Asignacion', 1, 'fecha_asignacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 11, 'fecha_envio_unidad_sec', 'Fecha Envio Unidad Sec', 1, 'fecha_envio_unidad_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 9, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 10, 'fecha_envio_unidad', 'Fecha Envio Unidad', 1, 'fecha_envio_unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 8, 'unidad', 'Unidad', 1, 'unidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(760, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(760, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(760, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(761, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(761, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(761, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(762, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(762, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(763, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(763, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(763, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(771, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(764, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(764, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(765, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(765, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(766, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(766, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 1, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(762, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 5, 'fecha_hora_solicitud', 'Fecha Hora Solicitud', 1, 'fecha_hora_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 4, 'requerimiento_fecha_creacion_sec', 'Requerimiento Fecha Creacion Sec', 1, 'requerimiento_fecha_creacion_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(769, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(770, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(767, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(767, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(767, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(768, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(768, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(768, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(769, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(769, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(770, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 0, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(770, 3, 'total_casos', 'Total Casos', 1, 'total_casos', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 23, 'sub_categorias_de_derecho', 'Sub Categorias De Derecho', 1, 'sub_categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 22, 'categorias_de_derecho', 'Categorias De Derecho', 1, 'categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(764, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(765, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(766, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(771, 1, 'id', 'Id', 1, 'id', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(771, 2, 'nombre', 'Nombre', 1, 'nombre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 24, 'sub_sub_categorias_de_derecho', 'Sub Sub Categorias De Derecho', 1, 'sub_sub_categorias_de_derecho', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 25, 'objetivos_estrategicos', 'Objetivos Estrategicos', 1, 'objetivos_estrategicos', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 26, 'componente', 'Componente', 1, 'componente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 27, 'tarea_fecha_primera_gestion', 'Tarea Fecha Primera Gestion', 1, 'tarea_fecha_primera_gestion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 28, 'tarea_fecha_primera_gestion_sec', 'Tarea Fecha Primera Gestion Sec', 1, 'tarea_fecha_primera_gestion_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 29, 'bitacora_fecha_primera_gestion', 'Bitacora Fecha Primera Gestion', 1, 'bitacora_fecha_primera_gestion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 30, 'bitacora_fecha_primera_gestion_sec', 'Bitacora Fecha Primera Gestion Sec', 1, 'bitacora_fecha_primera_gestion_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 31, 'fecha_solicitud_cierre', 'Fecha Solicitud Cierre', 1, 'fecha_solicitud_cierre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 32, 'fecha_solicitud_cierre_sec', 'Fecha Solicitud Cierre Sec', 1, 'fecha_solicitud_cierre_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 33, 'fecha_cierre', 'Fecha Cierre', 1, 'fecha_cierre', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 34, 'fecha_cierre_sec', 'Fecha Cierre Sec', 1, 'fecha_cierre_sec', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 35, 'criticidad', 'Criticidad', 1, 'criticidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 36, 'contexto_de_vulneracion', 'Contexto De Vulneracion', 1, 'contexto_de_vulneracion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 37, 'tipo_de_vulneracion', 'Tipo De Vulneracion', 1, 'tipo_de_vulneracion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 3, 'id_estado', 'Id_estado', 1, 'id_estado', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 2, 'folio', 'Folio', 1, 'folio', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 4, 'fecha_hora_solicitud', 'Fecha Hora Solicitud', 1, 'fecha_hora_solicitud', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 39, 'razon_social', 'Razon Social', 1, 'razon_social', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 40, 'institucion_requirente', 'Institucion Requirente', 1, 'institucion_requirente', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 41, 'motivo_contacto', 'Motivo Contacto', 1, 'motivo_contacto', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 3, 'requerimiento_fecha_creacion', 'Requerimiento Fecha Creacion', 1, 'requerimiento_fecha_creacion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 2, 'folio_requerimiento', 'Folio Requerimiento', 1, 'folio_requerimiento', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 1, 'ano', 'Ano', 1, 'ano', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 38, 'figura_de_vulneracion', 'Figura De Vulneracion', 1, 'figura_de_vulneracion', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 39, 'nnas_nacionalidad', 'Nnas Nacionalidad', 1, 'nnas_nacionalidad', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 40, 'nnas_genero', 'Nnas Genero', 1, 'nnas_genero', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 41, 'nnas_tipos_discapacidades', 'Nnas Tipos Discapacidades', 1, 'nnas_tipos_discapacidades', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(759, 42, 'nnas_pueblos_originarios', 'Nnas Pueblos Originarios', 1, 'nnas_pueblos_originarios', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 7, 'id_unidad', 'Id_unidad', 1, 'id_unidad', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 6, 'sede', 'Sede', 1, 'sede', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 1, 'id_requerimiento', 'Id_requerimiento', 1, 'id_requerimiento', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(737, 33, 'nnas_pueblos_originarios', 'Nnas Pueblos Originarios', 1, 'nnas_pueblos_originarios', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 42, 'solicitud_realizada', 'Solicitud Realizada', 1, 'solicitud_realizada', 1, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 1, 0, 0, 0, 0, '', '', '', '', 0, 0, 1, 0, 0, 1, 0);
INSERT INTO `replet_sql_columna` (`id_reporte`, `id_columna`, `nombre_columna`, `caption`, `tipo`, `valor`, `formato_despliegue`, `cantidad_decimales`, `tipo_total`, `header_style`, `cell_style1`, `cell_style2`, `id_reporte_ref`, `ordenable`, `caption_agrupacion`, `cantidad_agrupar`, `no_wrap`, `reporte_ref_target`, `invisible`, `auto_nav`, `agrupar_despliegue`, `trasposicion_caption_grupo`, `trasposicion_tipo_columna`, `tooltip`, `width_imagen`, `height_imagen`, `path_imagen`, `width_ini`, `link_dinamico`, `formatear_miles`, `max_length`, `add_to_url_reporte_ref`, `escape_html`, `abrir_reporte_ref_modal`) VALUES
	(740, 43, 'escrito_antes', 'Escrito Antes', 1, 'escrito_antes', 2, 0, 0, 'report_header', 'report_cell_admin_1', 'report_cell_admin_2', 0, 1, '', 0, 1, '', 0, 0, 0, 0, 0, '', '', '', '', 0, 0, 0, 0, 0, 1, 0);
/*!40000 ALTER TABLE `replet_sql_columna` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_columna_param
DROP TABLE IF EXISTS `replet_sql_columna_param`;
CREATE TABLE IF NOT EXISTS `replet_sql_columna_param` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_columna` int(11) NOT NULL DEFAULT '0',
  `id_reporte_ref` int(11) DEFAULT NULL,
  `id_param_ref` int(11) NOT NULL DEFAULT '0',
  `tipo` int(11) DEFAULT NULL,
  `valor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_columna`,`id_param_ref`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_columna_param: 0 rows
DELETE FROM `replet_sql_columna_param`;
/*!40000 ALTER TABLE `replet_sql_columna_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_columna_param` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_columna_presentacion
DROP TABLE IF EXISTS `replet_sql_columna_presentacion`;
CREATE TABLE IF NOT EXISTS `replet_sql_columna_presentacion` (
  `id_reporte` int(11) unsigned NOT NULL DEFAULT '0',
  `id_columna` int(11) unsigned NOT NULL DEFAULT '0',
  `id_tipo_presentacion` int(4) unsigned DEFAULT '0',
  `form_id` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `max_len` int(3) unsigned DEFAULT NULL,
  `len` int(4) unsigned DEFAULT NULL,
  `valor_selected` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_selector` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_selector_presentacion` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_columna`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_columna_presentacion: 282 rows
DELETE FROM `replet_sql_columna_presentacion`;
/*!40000 ALTER TABLE `replet_sql_columna_presentacion` DISABLE KEYS */;
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 11, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 6, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(725, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(732, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(730, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(731, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(732, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(733, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(735, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(736, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 31, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 30, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 29, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 28, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 27, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 26, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 25, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 24, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 23, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 22, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 21, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 20, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 19, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 18, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 17, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 16, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 15, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 14, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(732, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(733, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(736, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(730, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(736, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(733, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(730, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 6, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 11, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 13, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 14, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 15, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 16, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 17, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 18, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 19, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 20, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 21, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 22, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 23, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 24, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 25, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 26, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 6, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 11, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 50, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 49, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 48, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 47, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 46, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 45, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 44, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 43, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 42, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 41, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 40, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 39, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 38, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 37, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 36, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 35, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 34, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 33, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 32, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 31, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 30, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 29, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 28, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 27, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 26, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 25, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 24, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 23, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 22, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 21, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 20, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 19, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 18, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 17, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 16, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 15, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 14, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 13, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 11, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 6, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(745, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(748, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(748, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(748, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(749, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(750, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(751, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(752, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(753, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(754, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(755, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(756, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(756, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(756, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(757, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(758, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(749, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(750, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(749, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(750, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(751, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(752, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(753, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(754, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(755, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(751, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(753, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(754, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(755, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(757, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(758, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(752, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(757, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(758, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 13, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(744, 14, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 27, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 28, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 29, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 30, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 31, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 32, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 33, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 34, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 35, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 36, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 21, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 22, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 23, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 24, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 25, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 26, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 27, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 28, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 29, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 30, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 31, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 32, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 33, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 34, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(760, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(760, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(760, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(761, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(762, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(763, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(764, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(765, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(766, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 20, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 19, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(761, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(763, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(762, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(767, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(768, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(768, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(768, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(769, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(770, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 39, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 38, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 37, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 36, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 35, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(764, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(765, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(766, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(767, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(769, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(770, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(762, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(763, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(761, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(767, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 40, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(771, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(771, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(771, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 18, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 17, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 16, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 15, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 14, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 13, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 11, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(764, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(765, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(766, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(769, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(770, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(735, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 13, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 12, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 11, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 10, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 9, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 8, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 37, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 38, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 39, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 40, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 41, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 6, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 4, 0, '', '', 0, 0, '', '0', '0');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(759, 42, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(731, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(731, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(730, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 7, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 6, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 4, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 5, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 2, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 1, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(735, 3, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 32, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(737, 33, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 41, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 42, 0, '', '', 0, 0, '', '', '');
INSERT INTO `replet_sql_columna_presentacion` (`id_reporte`, `id_columna`, `id_tipo_presentacion`, `form_id`, `descripcion`, `max_len`, `len`, `valor_selected`, `id_selector`, `id_selector_presentacion`) VALUES
	(740, 43, 0, '', '', 0, 0, '', '', '');
/*!40000 ALTER TABLE `replet_sql_columna_presentacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_columna_tipo_presentacion
DROP TABLE IF EXISTS `replet_sql_columna_tipo_presentacion`;
CREATE TABLE IF NOT EXISTS `replet_sql_columna_tipo_presentacion` (
  `id_tipo_presentacion` int(4) unsigned NOT NULL DEFAULT '0',
  `tag` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  PRIMARY KEY (`id_tipo_presentacion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_columna_tipo_presentacion: 6 rows
DELETE FROM `replet_sql_columna_tipo_presentacion`;
/*!40000 ALTER TABLE `replet_sql_columna_tipo_presentacion` DISABLE KEYS */;
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(0, '', 'Texto Simple');
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(1, '<input type="checkbox" name="$%fid%$" id="$%fid%$" $%sel_tag%$ class="checkbox">', 'CheckBox');
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(2, '<input type="radio" name="$%fid%$" id="$%fid%$" $%sel_tag%$ class="radio">', 'RadioButton');
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(3, '', 'Select');
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(4, '<input type="text" name="$%fid%$" id="$%fid%$" maxlength="$%maxlen%$" size="$%len%$" value="$%val%$">', 'TextBox');
INSERT INTO `replet_sql_columna_tipo_presentacion` (`id_tipo_presentacion`, `tag`, `descripcion`) VALUES
	(5, '<input type="hidden" name="$%fid%$" id="$%fid%$" value="$%val%$">', 'Hidden');
/*!40000 ALTER TABLE `replet_sql_columna_tipo_presentacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_form
DROP TABLE IF EXISTS `replet_sql_form`;
CREATE TABLE IF NOT EXISTS `replet_sql_form` (
  `id_replet` int(11) unsigned NOT NULL DEFAULT '0',
  `form_action` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `method` varchar(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  `open_in_popup` int(1) DEFAULT '0',
  `submit_button_caption` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `reset_button_caption` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_replet`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_form: 78 rows
DELETE FROM `replet_sql_form`;
/*!40000 ALTER TABLE `replet_sql_form` DISABLE KEYS */;
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(689, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(687, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(711, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(725, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(724, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(723, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(721, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(720, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(719, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(718, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(716, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(713, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(710, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(708, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(707, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(705, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(703, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(701, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(700, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(697, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(695, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(694, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(686, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(651, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(691, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(692, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(730, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(731, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(732, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(733, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(735, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(736, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(737, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(738, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(739, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(740, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(742, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(744, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(745, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(747, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(748, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(749, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(750, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(751, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(752, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(753, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(754, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(755, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(756, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(757, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(758, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(759, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(760, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(761, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(762, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(763, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(764, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(765, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(766, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(767, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(768, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(769, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(770, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(771, '', 'post', 0, 'Submit', 'reset');
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(772, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(774, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(775, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(776, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(777, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(778, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(779, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(780, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(781, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(782, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(783, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(784, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(785, NULL, NULL, 0, NULL, NULL);
INSERT INTO `replet_sql_form` (`id_replet`, `form_action`, `method`, `open_in_popup`, `submit_button_caption`, `reset_button_caption`) VALUES
	(786, NULL, NULL, 0, NULL, NULL);
/*!40000 ALTER TABLE `replet_sql_form` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_form_parameters
DROP TABLE IF EXISTS `replet_sql_form_parameters`;
CREATE TABLE IF NOT EXISTS `replet_sql_form_parameters` (
  `id_replet` int(11) NOT NULL DEFAULT '0',
  `form_id` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `label` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `param_id_in_source` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `default_value` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_form_parameters: 0 rows
DELETE FROM `replet_sql_form_parameters`;
/*!40000 ALTER TABLE `replet_sql_form_parameters` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_form_parameters` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_grafico
DROP TABLE IF EXISTS `replet_sql_grafico`;
CREATE TABLE IF NOT EXISTS `replet_sql_grafico` (
  `id_grafico` int(3) NOT NULL AUTO_INCREMENT,
  `id_replet` int(10) NOT NULL DEFAULT '0',
  `id_tipo_grafico` int(10) DEFAULT NULL,
  `abre_popup` int(1) NOT NULL DEFAULT '0',
  `xml_data` text COLLATE latin1_spanish_ci,
  `inline` int(1) unsigned DEFAULT '0',
  `default_width` int(10) unsigned DEFAULT '0',
  `default_height` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id_grafico`)
) ENGINE=MyISAM AUTO_INCREMENT=86 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_grafico: 29 rows
DELETE FROM `replet_sql_grafico`;
/*!40000 ALTER TABLE `replet_sql_grafico` DISABLE KEYS */;
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(81, 731, 203, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 300);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(82, 732, 203, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo solicitud</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 300);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(83, 733, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Región</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>0</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(80, 730, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Región</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>0</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>3</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 300);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(84, 735, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Vía ingreso</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 300);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(85, 736, 203, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo solicitud</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 400, 300);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(26, 749, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Unidad</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(27, 750, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sede</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(25, 748, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo de Problema</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(28, 751, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Macro proceso</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(29, 752, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Proceso</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(31, 753, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Región</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(32, 754, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Comuna</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(34, 755, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Área de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(35, 756, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(36, 757, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sub-categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(37, 758, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sub sub-categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(56, 764, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Unidad</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(57, 765, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sede</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(58, 766, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo de solicitud</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(59, 760, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Tipo de complejidad</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(60, 762, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Urgencia</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(61, 763, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Componente</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(62, 761, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Responsable</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(64, 767, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Area de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(65, 768, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(66, 769, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sub-Categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(67, 770, 201, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Sub-Sub-Categoría de derecho</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
INSERT INTO `replet_sql_grafico` (`id_grafico`, `id_replet`, `id_tipo_grafico`, `abre_popup`, `xml_data`, `inline`, `default_width`, `default_height`) VALUES
	(63, 771, 202, 0, '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\r\n<!--id corresponde al id del grafico parte en 0 y es correlativo -->\r\n<grafico id=\'0\' tipo_grafico=\'4\'>\r\n  <titulo></titulo>\r\n  <descripcion></descripcion>\r\n\r\n  <!-- color corresponde al color de fondo del Grafico -->\r\n  <color>#FFFFFF</color>\r\n  <interpretacion></interpretacion>\r\n\r\n  <columna_x>\r\n    <caption>Criticidad</caption>\r\n    <!-- id_col_reporte es el id de alguna columna del reporte -1 -->\r\n    <id_col_reporte>1</id_col_reporte>\r\n  </columna_x>\r\n\r\n  <columna_y>\r\n    <!-- caption para el Eje Y-->\r\n    <caption>Total</caption>\r\n  </columna_y>\r\n\r\n\r\n  <categorias>\r\n    <categoria id=\'0\'>\r\n      <id_col_reporte>2</id_col_reporte>\r\n      <caption>Total</caption>\r\n\r\n    </categoria>\r\n  </categorias>\r\n</grafico>', 1, 1000, 400);
/*!40000 ALTER TABLE `replet_sql_grafico` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_processor
DROP TABLE IF EXISTS `replet_sql_processor`;
CREATE TABLE IF NOT EXISTS `replet_sql_processor` (
  `id_replet_processor` int(10) NOT NULL AUTO_INCREMENT,
  `id_replet` int(10) NOT NULL DEFAULT '0',
  `id_processor` int(1) unsigned DEFAULT '0',
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `class` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `config` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_replet_processor`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_processor: 0 rows
DELETE FROM `replet_sql_processor`;
/*!40000 ALTER TABLE `replet_sql_processor` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_processor` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_rango
DROP TABLE IF EXISTS `replet_sql_rango`;
CREATE TABLE IF NOT EXISTS `replet_sql_rango` (
  `id_rango` int(3) NOT NULL DEFAULT '0',
  `nombre_rango` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `descr_rango` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `tipo` int(11) DEFAULT NULL,
  `usar_val_porcentaje_optimo` tinyint(4) NOT NULL DEFAULT '0',
  `valor_optimo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_rango`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_rango: 0 rows
DELETE FROM `replet_sql_rango`;
/*!40000 ALTER TABLE `replet_sql_rango` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_rango` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_rango_tramo
DROP TABLE IF EXISTS `replet_sql_rango_tramo`;
CREATE TABLE IF NOT EXISTS `replet_sql_rango_tramo` (
  `id_rango` int(3) NOT NULL DEFAULT '0',
  `id_tramo` int(3) NOT NULL DEFAULT '0',
  `inicio` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fin` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `color` varchar(7) COLLATE latin1_spanish_ci DEFAULT NULL,
  `color_texto` varchar(7) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_rango`,`id_tramo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_rango_tramo: 0 rows
DELETE FROM `replet_sql_rango_tramo`;
/*!40000 ALTER TABLE `replet_sql_rango_tramo` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_rango_tramo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_rango_uso
DROP TABLE IF EXISTS `replet_sql_rango_uso`;
CREATE TABLE IF NOT EXISTS `replet_sql_rango_uso` (
  `id_reporte` int(3) NOT NULL DEFAULT '0',
  `id_rango` int(3) NOT NULL DEFAULT '0',
  `id_col_comparacion` int(3) NOT NULL DEFAULT '0',
  `tipo_pintado` int(3) DEFAULT '0',
  `id_col_pintado` tinyint(3) DEFAULT '0',
  `columna_valor_optimo` int(11) DEFAULT NULL,
  `valor_optimo` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_rango`,`id_col_comparacion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_rango_uso: 0 rows
DELETE FROM `replet_sql_rango_uso`;
/*!40000 ALTER TABLE `replet_sql_rango_uso` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_rango_uso` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_relacionado
DROP TABLE IF EXISTS `replet_sql_relacionado`;
CREATE TABLE IF NOT EXISTS `replet_sql_relacionado` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_relacionado` int(11) NOT NULL DEFAULT '0',
  `id_reporte_relacionado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_relacionado`),
  KEY `id_reporte_ref` (`id_reporte_relacionado`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_relacionado: 0 rows
DELETE FROM `replet_sql_relacionado`;
/*!40000 ALTER TABLE `replet_sql_relacionado` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_relacionado` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_relacionado_param
DROP TABLE IF EXISTS `replet_sql_relacionado_param`;
CREATE TABLE IF NOT EXISTS `replet_sql_relacionado_param` (
  `id_reporte` int(11) NOT NULL DEFAULT '0',
  `id_relacionado` int(11) NOT NULL DEFAULT '0',
  `id_reporte_relacionado` int(11) DEFAULT NULL,
  `id_param_relacionado` int(11) NOT NULL DEFAULT '0',
  `tipo` int(11) DEFAULT NULL,
  `valor` char(30) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_reporte`,`id_relacionado`,`id_param_relacionado`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_relacionado_param: 0 rows
DELETE FROM `replet_sql_relacionado_param`;
/*!40000 ALTER TABLE `replet_sql_relacionado_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `replet_sql_relacionado_param` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.replet_sql_trasposicion_tipo_columna
DROP TABLE IF EXISTS `replet_sql_trasposicion_tipo_columna`;
CREATE TABLE IF NOT EXISTS `replet_sql_trasposicion_tipo_columna` (
  `id_trasposicion_tipo_columna` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_trasposicion_tipo_columna` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_trasposicion_tipo_columna`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.replet_sql_trasposicion_tipo_columna: 4 rows
DELETE FROM `replet_sql_trasposicion_tipo_columna`;
/*!40000 ALTER TABLE `replet_sql_trasposicion_tipo_columna` DISABLE KEYS */;
INSERT INTO `replet_sql_trasposicion_tipo_columna` (`id_trasposicion_tipo_columna`, `desc_trasposicion_tipo_columna`) VALUES
	(0, 'Campo de Grupo');
INSERT INTO `replet_sql_trasposicion_tipo_columna` (`id_trasposicion_tipo_columna`, `desc_trasposicion_tipo_columna`) VALUES
	(1, 'Llave de Grupo');
INSERT INTO `replet_sql_trasposicion_tipo_columna` (`id_trasposicion_tipo_columna`, `desc_trasposicion_tipo_columna`) VALUES
	(2, 'Llave de Tupla');
INSERT INTO `replet_sql_trasposicion_tipo_columna` (`id_trasposicion_tipo_columna`, `desc_trasposicion_tipo_columna`) VALUES
	(3, 'Campo de Tupla');
/*!40000 ALTER TABLE `replet_sql_trasposicion_tipo_columna` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.seccion
DROP TABLE IF EXISTS `seccion`;
CREATE TABLE IF NOT EXISTS `seccion` (
  `id_seccion` int(11) unsigned NOT NULL DEFAULT '0',
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `descr` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `tipo_home` int(3) unsigned NOT NULL DEFAULT '0',
  `ref_home` int(11) unsigned NOT NULL DEFAULT '0',
  `id_categoria` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_seccion`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.seccion: 1 rows
DELETE FROM `seccion`;
/*!40000 ALTER TABLE `seccion` DISABLE KEYS */;
INSERT INTO `seccion` (`id_seccion`, `nombre`, `descr`, `tipo_home`, `ref_home`, `id_categoria`) VALUES
	(2, 'Inicio', 'Sección Principal', 0, 0, 1);
/*!40000 ALTER TABLE `seccion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.security_manager
DROP TABLE IF EXISTS `security_manager`;
CREATE TABLE IF NOT EXISTS `security_manager` (
  `id_security_manager` int(5) unsigned NOT NULL DEFAULT '0',
  `class_name` text COLLATE latin1_spanish_ci,
  `id_pool_conexiones` varchar(255) COLLATE latin1_spanish_ci DEFAULT 'pool_control',
  PRIMARY KEY (`id_security_manager`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.security_manager: 1 rows
DELETE FROM `security_manager`;
/*!40000 ALTER TABLE `security_manager` DISABLE KEYS */;
INSERT INTO `security_manager` (`id_security_manager`, `class_name`, `id_pool_conexiones`) VALUES
	(0, 'ai.security.TAISecurityManager', 'pool_control');
/*!40000 ALTER TABLE `security_manager` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.selector
DROP TABLE IF EXISTS `selector`;
CREATE TABLE IF NOT EXISTS `selector` (
  `id_selector` varchar(50) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `sql_` text COLLATE latin1_spanish_ci,
  `desc_selector` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_base_datos` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `parametros` text COLLATE latin1_spanish_ci,
  `id_estado_selector` int(5) unsigned DEFAULT '1',
  `sql_1` text COLLATE latin1_spanish_ci,
  `sql_2` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_selector`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.selector: 27 rows
DELETE FROM `selector`;
/*!40000 ALTER TABLE `selector` DISABLE KEYS */;
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_procesor', 'select id_processor, descripcion from processor\r\norder by descripcion', 'Selector Processors', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_selectores', 'select id_selector, desc_selector \r\nfrom selector where id_selector <> \'selector_selectores\'\r\norder by 2', 'Selector de Selectores', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_tipo_plantilla', 'select id_plantilla_tipo, descripcion from plantilla_tipo order by descripcion', 'Selector Tipo Plantilla', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_trasposicion_tipo_columna', 'SELECT id_trasposicion_tipo_columna, desc_trasposicion_tipo_columna\r\nFROM replet_sql_trasposicion_tipo_columna\r\norder by 1', 'Selector Tipo Columna Traspuesta', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_tipo_grafico', 'select id_tipo_grafico, desc_tipo_grafico from replet_grafico_tipo_grafico order by id_tipo_grafico', 'Selector Tipos de Graficos', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_si_no', 'select codigo, valor\r\nfrom si_no\r\nwhere codigo<>0\r\norder by codigo asc', 'Selector SI/NO', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_plantillas_tipo', 'select id_plantilla, nombre \r\nfrom plantilla \r\nwhere \r\ntipo_plantilla = ?\r\norder by 2', 'Selector de Sector de Plantillas', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('si_no_TODOS', 'select null, \'(TODOS)\'\r\nunion\r\nselect 1, \'SI\'\r\nunion\r\nselect 0, \'NO\'', 'Selector SI/NO TODOS', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, 'select -1 as id, \'(TODOS)\' as nombre\r\nunion\r\nselect 1 as id, \'SI\' as nombre\r\nunion\r\nselect 0 as id, \'NO\' as nombre', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_sede_TODOS', 'SELECT -1,  \'(TODOS)\', -1\r\nUNION ALL \r\nSELECT id, nombre, 0\r\nFROM sede\r\nORDER BY 3, 2', 'REQ - Selector SEDE (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_tipo_solicitud_TODOS', 'SELECT -1,  \'(TODOS)\', -1\r\nUNION ALL \r\nSELECT id, nombre, 0\r\nFROM req_tipo_solicitud\r\nORDER BY 3, 2', 'REQ - Selector TIPO SOLICITUD (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('selector_usuarios', 'select -1, \'(TODOS)\' from dual\r\nunion all\r\nselect DISTINCT(usuario),usuario\r\nfrom estadistica', 'Selector de usuarios conectados', 'pool_control', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_ano_requerimiento_TODOS', 'SELECT -1,  \'(TODOS)\'\r\nUNION ALL \r\nSELECT DISTINCT YEAR(r.fecha_hora_solicitud), YEAR(r.fecha_hora_solicitud)\r\nFROM requerimiento r\r\nORDER BY 1', 'REQ - Selector AÑO REQUERIMIENTO (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_unidad_TODOS', 'SELECT -1,  \'(TODOS)\', -1\r\nUNION ALL \r\nSELECT id, nombre, 0\r\nFROM unidad\r\nORDER BY 3, 2', 'REQ - Selector UNIDAD (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_via_ingreso_TODOS', 'SELECT -1,  \'(TODOS)\', -1\r\nUNION ALL \r\nSELECT id, nombre, 0\r\nFROM req_via_ingreso\r\nORDER BY 3, 2', 'REQ - Selector VIA INGRESO (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_region_TODOS', 'SELECT -1,  \'(TODOS)\', -1, 0\r\nUNION ALL \r\nSELECT id, nombre, 0, orden\r\nFROM region\r\nORDER BY 3, 4', 'REQ - Selector REGION (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_mes_requerimiento_TODOS', 'SELECT -1,  \'(TODOS)\'\r\nUNION ALL \r\nSELECT DISTINCT MONTH(r.fecha_hora_solicitud), MONTH(r.fecha_hora_solicitud)\r\nFROM requerimiento r\r\nORDER BY 1', 'REQ - Selector MES REQUERIMIENTO (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_tipo_persona_requirente_TODOS', 'SELECT -1,  \'(TODOS)\', -1\r\nUNION ALL \r\nSELECT \'PERSONA_JURIDICA\', \'Persona Jurídica\', 3\r\nUNION ALL\r\nSELECT \'PERSONA_NATURAL\', \'Persona Natural\', 2\r\nUNION ALL\r\nSELECT \'NNA\', \'NNA\', 1\r\n\r\nORDER BY 3, 2', 'REQ - Selector TIPO PERSONA REQUIRENTE (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_tipo_problema_generico_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM problema_generico_tipo ORDER BY 3, 2', 'REQ - Selector Problema generico (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_macroproceso_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM macroproceso_tipo ORDER BY 3, 2', 'REQ - Selector Macroproceso (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, NULL, NULL);
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_proceso_tipo_TODOS', 'SELECT -1, \'(TODAS)\' nombre UNION ALL SELECT id, nombre FROM proceso_tipo WHERE  id_macroproceso = ? ORDER BY nombre;', 'Req - Selector proceso tipo por id_macro_proceso (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param><param nombre=\'id_macro_proceso\' tipo=\'int\' /></selector-param>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_area_derecho_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM area_derecho ORDER BY 3, 2;', 'REQ - Selector area de derecho (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_categoria_derecho_TODOS', 'SELECT -1, \'(TODAS)\' nombre UNION ALL SELECT id, nombre FROM categoria_derecho WHERE  id_area = ? ORDER BY nombre;', 'Req - Selector categoria derecho por id_area_derecho (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param><param nombre=\'id_area_derecho\' tipo=\'int\' /></selector-param>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_sub_categoria_derecho_TODOS', 'SELECT -1, \'(TODAS)\' nombre UNION ALL SELECT id, nombre FROM sub_categoria_derecho WHERE  id_categoria = ? ORDER BY nombre;', 'Req - Selector sub_categoria derecho por id_categoria_derecho (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param><param nombre=\'id_categoria_derecho\' tipo=\'int\' /></selector-param>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_sub_sub_categoria_derecho_TODOS', 'SELECT -1, \'(TODAS)\' nombre UNION ALL SELECT id, nombre FROM sub_sub_categoria_derecho WHERE  id_sub_categoria = ? ORDER BY nombre;', 'Req - Selector sub_sub_categoria derecho por id_sub_categoria_derecho (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param><param nombre=\'id_sub_categoria_derecho\' tipo=\'int\' /></selector-param>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_tipo_complejidad_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM caso_tipo_complejidad ORDER BY 3, 2', 'REQ - Selector Tipo de complejidad (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_tipo_urgencia_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM urgencia_asignacion_tipo ORDER BY 3, 2', 'REQ - Selector Tipo de urgencia (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, '', '');
INSERT INTO `selector` (`id_selector`, `sql_`, `desc_selector`, `id_base_datos`, `parametros`, `id_estado_selector`, `sql_1`, `sql_2`) VALUES
	('req_selector_componente_derivacion_TODOS', 'SELECT -1, \'(TODOS)\', -1 UNION ALL SELECT id, nombre, 0 FROM caso_componente_derivacion ORDER BY 3, 2', 'REQ - Selector Tipo de componente caso (TODOS)', 'data_sgp', '<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?><selector-param/>', 1, '', '');
/*!40000 ALTER TABLE `selector` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.selector_tipo_presentacion
DROP TABLE IF EXISTS `selector_tipo_presentacion`;
CREATE TABLE IF NOT EXISTS `selector_tipo_presentacion` (
  `id_tipo_presentacion` varchar(50) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `desc_tipo_presentacion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `html_prefix` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `html_tag` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `html_suffix` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `selected_label` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.selector_tipo_presentacion: 9 rows
DELETE FROM `selector_tipo_presentacion`;
/*!40000 ALTER TABLE `selector_tipo_presentacion` DISABLE KEYS */;
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('texto_simple', 'Texto Simple', '', '', '', '');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('checkbox_vertical', 'CheckBox hacia abajo', '', '<input type="checkbox" name="$%fid%$" id="$%fid%$" value="$%val%$" class="checkbox" $%sel_tag%$>$%desc%$<br>', '', 'checked');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('radio_vertical', 'RadioButton hacia abajo', '', '<input type="radio" name="$%fid%$" id="$%fid%$" value="$%val%$" class="radio" $%sel_tag%$>$%desc%$<br>', '', 'checked');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('select', 'Select', '<select name="$%fid%$" id="$%fid%$">', '<option value="$%val%$" $%sel_tag%$>$%desc%$</option>', '</select>', 'selected');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('textbox', 'TextBox', '', '<input type="text" name="$%fid%$" id="$%fid%$" maxlength="$%maxlen%$" size="$%len%$" value="$%val%$"><br>', '', '');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('hidden', 'Hidden', '', '<input type="hidden" name="$%fid%$" id="$%fid%$" value="$%val%$">', '', '');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('select_class', 'Select', '<select class=\'select\' name="$%fid%$" id="$%fid%$">', '<option value="$%val%$" $%sel_tag%$>$%desc%$</option>', '</select>', 'selected');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('select_js', 'Select Javascript', 'sel.length = 0;', 'sel.options[sel.length] = new Option(\'$%desc%$\', \'$%val%$\');', '', '');
INSERT INTO `selector_tipo_presentacion` (`id_tipo_presentacion`, `desc_tipo_presentacion`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`) VALUES
	('select_options', 'Html options para ajax', '', '<option value="$%val%$" $%sel_tag%$>$%desc%$</option>', '', 'selected');
/*!40000 ALTER TABLE `selector_tipo_presentacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.servicio
DROP TABLE IF EXISTS `servicio`;
CREATE TABLE IF NOT EXISTS `servicio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activo` int(11) NOT NULL,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `es_servicio_formal` int(11) NOT NULL,
  `externo` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sigla` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `siglas` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `ultima_carga_datos` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.servicio: ~0 rows (aproximadamente)
DELETE FROM `servicio`;
/*!40000 ALTER TABLE `servicio` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicio` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.si_no
DROP TABLE IF EXISTS `si_no`;
CREATE TABLE IF NOT EXISTS `si_no` (
  `codigo` int(5) unsigned NOT NULL DEFAULT '0',
  `valor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.si_no: 2 rows
DELETE FROM `si_no`;
/*!40000 ALTER TABLE `si_no` DISABLE KEYS */;
INSERT INTO `si_no` (`codigo`, `valor`) VALUES
	(1, 'SI');
INSERT INTO `si_no` (`codigo`, `valor`) VALUES
	(2, 'NO');
/*!40000 ALTER TABLE `si_no` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea
DROP TABLE IF EXISTS `tarea`;
CREATE TABLE IF NOT EXISTS `tarea` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `codigo` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` longtext COLLATE latin1_spanish_ci,
  `emails` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `entidad_contiene_descripcion` varchar(2048) COLLATE latin1_spanish_ci DEFAULT NULL,
  `entidad_contiene_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_real_inicio` datetime DEFAULT NULL,
  `fecha_termino` date DEFAULT NULL,
  `key_sistema_entidad` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `medio_verificacion` longtext COLLATE latin1_spanish_ci,
  `notificable` int(11) NOT NULL,
  `orden` int(11) NOT NULL,
  `permite_modificar_revisor` int(11) NOT NULL,
  `plazo_verificacion` int(11) NOT NULL,
  `requiere_medio_finalizar` int(11) NOT NULL,
  `requiere_verificacion` int(11) NOT NULL,
  `tipo_plazo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `titulo` varchar(4000) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_division` int(11) DEFAULT NULL,
  `id_entidad_contiene` int(11) DEFAULT NULL,
  `id_estado` int(11) DEFAULT NULL,
  `id_responsable` bigint(20) DEFAULT NULL,
  `id_responsable_verificacion` bigint(20) DEFAULT NULL,
  `id_unidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9s6vu7y4gwqg87qagoigjywyw` (`alt_key`),
  KEY `FK5lqxyptddh28m4frj038arhmo` (`id_division`),
  KEY `FK23swv87200hg99h6q3ew7ysb9` (`id_entidad_contiene`),
  KEY `FKtoutbjod4vg8a1631bo4kq2uy` (`id_estado`),
  KEY `FKeo7t89yg26d69ohphjban78hu` (`id_responsable`),
  KEY `FKomcoemcuxlqohvjmn5n3fhwld` (`id_responsable_verificacion`),
  KEY `FK2t8h096ie8eeyqvkuc18ns21w` (`id_unidad`),
  CONSTRAINT `FK23swv87200hg99h6q3ew7ysb9` FOREIGN KEY (`id_entidad_contiene`) REFERENCES `tarea_entidad_externa` (`id`),
  CONSTRAINT `FK2t8h096ie8eeyqvkuc18ns21w` FOREIGN KEY (`id_unidad`) REFERENCES `unidad` (`id`),
  CONSTRAINT `FK5lqxyptddh28m4frj038arhmo` FOREIGN KEY (`id_division`) REFERENCES `division` (`id`),
  CONSTRAINT `FKeo7t89yg26d69ohphjban78hu` FOREIGN KEY (`id_responsable`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKomcoemcuxlqohvjmn5n3fhwld` FOREIGN KEY (`id_responsable_verificacion`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKtoutbjod4vg8a1631bo4kq2uy` FOREIGN KEY (`id_estado`) REFERENCES `tarea_estado` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea: ~0 rows (aproximadamente)
DELETE FROM `tarea`;
/*!40000 ALTER TABLE `tarea` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_archivo_adjunto
DROP TABLE IF EXISTS `tarea_archivo_adjunto`;
CREATE TABLE IF NOT EXISTS `tarea_archivo_adjunto` (
  `id_tarea` bigint(20) NOT NULL,
  `id_archivo_adjunto` bigint(20) NOT NULL,
  UNIQUE KEY `UK_kcyj3xme80dpu0vb2o3gf0sfc` (`id_archivo_adjunto`),
  KEY `FK6wnbso0nhhf5bdeo1q7n1bouo` (`id_tarea`),
  CONSTRAINT `FK3wre63vu6dp7n3dmkp5tstd7h` FOREIGN KEY (`id_archivo_adjunto`) REFERENCES `archivo_adjunto` (`id_archivo_adjunto`),
  CONSTRAINT `FK6wnbso0nhhf5bdeo1q7n1bouo` FOREIGN KEY (`id_tarea`) REFERENCES `tarea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_archivo_adjunto: ~0 rows (aproximadamente)
DELETE FROM `tarea_archivo_adjunto`;
/*!40000 ALTER TABLE `tarea_archivo_adjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_archivo_adjunto` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_avance
DROP TABLE IF EXISTS `tarea_avance`;
CREATE TABLE IF NOT EXISTS `tarea_avance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `descripcion` longtext COLLATE latin1_spanish_ci,
  `fecha_avance` datetime DEFAULT NULL,
  `id_factor_riesgo` int(11) DEFAULT NULL,
  `id_persona` bigint(20) DEFAULT NULL,
  `id_tarea` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t5u3ninfvu3193ofnvb7vs7wl` (`alt_key`),
  KEY `FKs6fsbugnhpqyis5efydwuwc2y` (`id_factor_riesgo`),
  KEY `FKt2ejur7ac0swqtqqxte3fuca0` (`id_persona`),
  KEY `FKghwhqx3m4jt2q3e1fqfm90aib` (`id_tarea`),
  CONSTRAINT `FKghwhqx3m4jt2q3e1fqfm90aib` FOREIGN KEY (`id_tarea`) REFERENCES `tarea` (`id`),
  CONSTRAINT `FKs6fsbugnhpqyis5efydwuwc2y` FOREIGN KEY (`id_factor_riesgo`) REFERENCES `tarea_factor_riesgo` (`id`),
  CONSTRAINT `FKt2ejur7ac0swqtqqxte3fuca0` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_avance: ~0 rows (aproximadamente)
DELETE FROM `tarea_avance`;
/*!40000 ALTER TABLE `tarea_avance` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_avance` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_entidad_externa
DROP TABLE IF EXISTS `tarea_entidad_externa`;
CREATE TABLE IF NOT EXISTS `tarea_entidad_externa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aplica_codigo_tarea` int(11) NOT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `field_descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `sistema` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `url_callback` varchar(2083) COLLATE latin1_spanish_ci DEFAULT NULL,
  `url_datos` varchar(2083) COLLATE latin1_spanish_ci DEFAULT NULL,
  `url_ver` varchar(2083) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dvn2hap5tlj4j4gfcpaq0gbsg` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_entidad_externa: ~0 rows (aproximadamente)
DELETE FROM `tarea_entidad_externa`;
/*!40000 ALTER TABLE `tarea_entidad_externa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_entidad_externa` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_estado
DROP TABLE IF EXISTS `tarea_estado`;
CREATE TABLE IF NOT EXISTS `tarea_estado` (
  `id` int(11) NOT NULL,
  `activo` int(11) NOT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_estado: ~0 rows (aproximadamente)
DELETE FROM `tarea_estado`;
/*!40000 ALTER TABLE `tarea_estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_estado` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_factor_riesgo
DROP TABLE IF EXISTS `tarea_factor_riesgo`;
CREATE TABLE IF NOT EXISTS `tarea_factor_riesgo` (
  `id` int(11) NOT NULL,
  `activo` int(11) NOT NULL,
  `codigo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_factor_riesgo: ~0 rows (aproximadamente)
DELETE FROM `tarea_factor_riesgo`;
/*!40000 ALTER TABLE `tarea_factor_riesgo` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_factor_riesgo` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_historial
DROP TABLE IF EXISTS `tarea_historial`;
CREATE TABLE IF NOT EXISTS `tarea_historial` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha_real_termino` datetime DEFAULT NULL,
  `fecha_revision` date DEFAULT NULL,
  `observacion_revisor` longtext COLLATE latin1_spanish_ci,
  `observacion_termino` longtext COLLATE latin1_spanish_ci,
  `id_tarea` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2mwd0wnqk69ydnf1i9qgy585s` (`id_tarea`),
  CONSTRAINT `FK2mwd0wnqk69ydnf1i9qgy585s` FOREIGN KEY (`id_tarea`) REFERENCES `tarea` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_historial: ~0 rows (aproximadamente)
DELETE FROM `tarea_historial`;
/*!40000 ALTER TABLE `tarea_historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_historial` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tarea_historial_archivo_adjunto
DROP TABLE IF EXISTS `tarea_historial_archivo_adjunto`;
CREATE TABLE IF NOT EXISTS `tarea_historial_archivo_adjunto` (
  `id_tarea_historial` bigint(20) NOT NULL,
  `id_archivo_adjunto` bigint(20) NOT NULL,
  UNIQUE KEY `UK_fkpom6d8c900ngie4717tv6r` (`id_archivo_adjunto`),
  KEY `FKicxe9n1oyevc99y89pryqkgr4` (`id_tarea_historial`),
  CONSTRAINT `FKicxe9n1oyevc99y89pryqkgr4` FOREIGN KEY (`id_tarea_historial`) REFERENCES `tarea_historial` (`id`),
  CONSTRAINT `FKp666yedti2l1yooseq8itlqa4` FOREIGN KEY (`id_archivo_adjunto`) REFERENCES `archivo_adjunto` (`id_archivo_adjunto`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tarea_historial_archivo_adjunto: ~0 rows (aproximadamente)
DELETE FROM `tarea_historial_archivo_adjunto`;
/*!40000 ALTER TABLE `tarea_historial_archivo_adjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarea_historial_archivo_adjunto` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tickets_acceso
DROP TABLE IF EXISTS `tickets_acceso`;
CREATE TABLE IF NOT EXISTS `tickets_acceso` (
  `id` varchar(255) COLLATE latin1_spanish_ci NOT NULL,
  `fecha_expiracion` timestamp NULL DEFAULT NULL,
  `fecha` timestamp NULL DEFAULT NULL,
  `usr_recursos` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `usr_cliente` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `ip_servidor` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `ip_cliente` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tickets_acceso: ~0 rows (aproximadamente)
DELETE FROM `tickets_acceso`;
/*!40000 ALTER TABLE `tickets_acceso` DISABLE KEYS */;
/*!40000 ALTER TABLE `tickets_acceso` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tipo_presentacion
DROP TABLE IF EXISTS `tipo_presentacion`;
CREATE TABLE IF NOT EXISTS `tipo_presentacion` (
  `id` int(3) unsigned NOT NULL DEFAULT '0',
  `html_prefix` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `html_tag` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `html_suffix` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `selected_label` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `description` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tipo_presentacion: 7 rows
DELETE FROM `tipo_presentacion`;
/*!40000 ALTER TABLE `tipo_presentacion` DISABLE KEYS */;
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(0, '', '', '', '', 'Texto Simple');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(1, '', '<input type="checkbox" name="$%fid%$" id="$%fid%$" value="$%val%$" class="checkbox" $%sel_tag%$>$%desc%$<br>', '', 'checked', 'CheckBox hacia abajo');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(2, '', '<input type="radio" name="$%fid%$" id="$%fid%$" value="$%val%$" class="radio" $%sel_tag%$>$%desc%$<br>', '', 'checked', 'RadioButton hacia abajo');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(3, '<select name="$%fid%$" id="$%fid%$">', '<option value="$%val%$" $%sel_tag%$>$%desc%$</option>', '</select>', 'selected', 'Select');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(4, '', '<input type="text" name="$%fid%$" id="$%fid%$" maxlength="$%maxlen%$" size="$%len%$" value="$%val%$"><br>', '', '', 'TextBox');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(5, '', '<input type="hidden" name="$%fid%$" id="$%fid%$" value="$%val%$">', '', '', 'Hidden');
INSERT INTO `tipo_presentacion` (`id`, `html_prefix`, `html_tag`, `html_suffix`, `selected_label`, `description`) VALUES
	(6, NULL, '', NULL, NULL, 'Selector');
/*!40000 ALTER TABLE `tipo_presentacion` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.tipo_replet
DROP TABLE IF EXISTS `tipo_replet`;
CREATE TABLE IF NOT EXISTS `tipo_replet` (
  `mime_type` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0',
  `descripcion` varchar(255) COLLATE latin1_spanish_ci DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.tipo_replet: 0 rows
DELETE FROM `tipo_replet`;
/*!40000 ALTER TABLE `tipo_replet` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipo_replet` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.unidad
DROP TABLE IF EXISTS `unidad`;
CREATE TABLE IF NOT EXISTS `unidad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alt_key` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `activo` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre_corto` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `proceso_aprob_forms` int(11) NOT NULL,
  `representa_division` int(11) NOT NULL,
  `revisor_datos2_cdg` int(11) DEFAULT NULL,
  `id_director_responsable` bigint(20) DEFAULT NULL,
  `id_division` int(11) DEFAULT NULL,
  `id_revisor_datos` bigint(20) DEFAULT NULL,
  `id_revisor_datos2` bigint(20) DEFAULT NULL,
  `id_servicio` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2nbvxw30vfhvga6qc85o39iee` (`alt_key`),
  KEY `FK60ao8hjapuhx4xyh2c8d88e2` (`id_director_responsable`),
  KEY `FKingld8ge8hw8lovxq8iqhm57x` (`id_division`),
  KEY `FKs72ngy7g6vbiqd7q485wrc2c3` (`id_revisor_datos`),
  KEY `FK6nvulaeq15ux07vaywgb6sn8p` (`id_revisor_datos2`),
  KEY `FKljnrkovwvjsmlyp9wmnfwabyk` (`id_servicio`),
  CONSTRAINT `FK60ao8hjapuhx4xyh2c8d88e2` FOREIGN KEY (`id_director_responsable`) REFERENCES `persona` (`id`),
  CONSTRAINT `FK6nvulaeq15ux07vaywgb6sn8p` FOREIGN KEY (`id_revisor_datos2`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKingld8ge8hw8lovxq8iqhm57x` FOREIGN KEY (`id_division`) REFERENCES `division` (`id`),
  CONSTRAINT `FKljnrkovwvjsmlyp9wmnfwabyk` FOREIGN KEY (`id_servicio`) REFERENCES `servicio` (`id`),
  CONSTRAINT `FKs72ngy7g6vbiqd7q485wrc2c3` FOREIGN KEY (`id_revisor_datos`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.unidad: ~0 rows (aproximadamente)
DELETE FROM `unidad`;
/*!40000 ALTER TABLE `unidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `unidad` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.unidad_persona
DROP TABLE IF EXISTS `unidad_persona`;
CREATE TABLE IF NOT EXISTS `unidad_persona` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_persona` bigint(20) DEFAULT NULL,
  `id_unidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3lm8g63rgpmke1von7d878k6a` (`id_persona`),
  KEY `FKh5g7sswv2pn07uh4du9mmya2m` (`id_unidad`),
  CONSTRAINT `FK3lm8g63rgpmke1von7d878k6a` FOREIGN KEY (`id_persona`) REFERENCES `persona` (`id`),
  CONSTRAINT `FKh5g7sswv2pn07uh4du9mmya2m` FOREIGN KEY (`id_unidad`) REFERENCES `unidad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.unidad_persona: ~0 rows (aproximadamente)
DELETE FROM `unidad_persona`;
/*!40000 ALTER TABLE `unidad_persona` DISABLE KEYS */;
/*!40000 ALTER TABLE `unidad_persona` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.url
DROP TABLE IF EXISTS `url`;
CREATE TABLE IF NOT EXISTS `url` (
  `id_url` int(11) unsigned NOT NULL DEFAULT '0',
  `url` varchar(2083) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `despliega_info` tinyint(3) unsigned DEFAULT '0',
  `nueva_ventana` tinyint(3) unsigned DEFAULT '0',
  `cerrar_automatico` int(1) unsigned DEFAULT '0',
  PRIMARY KEY (`id_url`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.url: 46 rows
DELETE FROM `url`;
/*!40000 ALTER TABLE `url` DISABLE KEYS */;
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(239, 'app/about', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(234, 'ai_main?accion=login_ver', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(235, 'app/about', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(651, '${ctx_defensoria_req}/requerimientos/ingreso-requerimientos/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(687, '${ctx_defensoria_req}/requerimientos/requerimientos-historicos/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(707, '${ctx_defensoria_req}/escritorios/responsable/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(708, '${ctx_defensoria_req}/escritorios/unidad/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(710, '${ctx_defensoria_req}/crud#/TipoSolicitud/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(711, '${ctx_defensoria_req}/crud#/ViaIngreso/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(713, '${ctx_defensoria_req}/hallazgos/lista/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(716, '${ctx_defensoria_req}/hallazgos/revisar/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(718, '${ctx_defensoria_req}/hallazgos/confirmar/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(719, '${ctx_defensoria_req}/crud#/TipoRespuestaRequerimiento/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(724, '${ctx_defensoria_req}/crud#/ActividadHallazgoPlantilla/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(723, '${ctx_ai}/app/admin/notificacionTipo/lista', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(721, '${ctx_defensoria_req}/hallazgos/historico/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(720, '${ctx_defensoria_req}/crud#/TiempoRespuestaRequerimiento/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(705, '${ctx_defensoria_req}/hallazgos/antecedentes/evaluar/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(700, '${ctx_defensoria_req}/hallazgos/antecedentes/listado/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(701, '${ctx_defensoria_req}/hallazgos/antecedentes/revisar/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(703, '${ctx_defensoria_req}/hallazgos/antecedentes/listadoHistorico/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(697, '${ctx_defensoria_req}/admin/usuarios/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(695, '${ctx_defensoria_req}/punto-focal/lista-productos-caso-puntos-focales/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(694, '${ctx_defensoria_req}/punto-focal/lista-punto-focales/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(686, '${ctx_defensoria_req}/requerimientos/requerimientos-sin-asignar/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(689, '${ctx_defensoria_req}/casos/listado-casos/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(691, '${ctx_defensoria_req}/requerimientos/revision-defensora/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(692, '${ctx_defensoria_req}/casos/listado-cierre-casos/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(738, '${contextPath}/app/documentos/videos/15', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(739, 'plugins/carpeta_docs/verCarpeta.jsp?nombre=videos_requerimientos', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(742, '${ctx_defensoria_req}/indicadores/listado-indicadores/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(747, 'ai_main?accion=ee_table&tipo_entidad=al.selector.TSelector', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(772, '${ctx_defensoria_req}/casos/listado-casos-cerrados/', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(774, '${ctx_defensoria_req}/crud#/Region/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(775, '${ctx_defensoria_req}/crud#/Pais/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(776, '${ctx_defensoria_req}/crud#/Comuna/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(777, '${ctx_defensoria_req}/crud#/PuebloOriginario/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(778, '${ctx_defensoria_req}/crud#/Unidad/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(779, '${ctx_defensoria_req}/crud#/Sede/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(780, '${ctx_defensoria_req}/crud#/AreaDerecho/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(781, '${ctx_defensoria_req}/crud#/CategoriaDerecho/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(782, '${ctx_defensoria_req}/crud#/SubCategoriaDerecho/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(783, '${ctx_defensoria_req}/crud#/SubSubCategoriaDerecho/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(784, '${ctx_defensoria_req}/crud#/TipoMotivoTermino/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(785, '${ctx_defensoria_req}/crud#/ObjetivoEstrategico/list', 0, 0, 0);
INSERT INTO `url` (`id_url`, `url`, `despliega_info`, `nueva_ventana`, `cerrar_automatico`) VALUES
	(786, '${ctx_defensoria_req}/crud#/ComponenteDerivacionCaso/list', 0, 0, 0);
/*!40000 ALTER TABLE `url` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_item
DROP TABLE IF EXISTS `wf_item`;
CREATE TABLE IF NOT EXISTS `wf_item` (
  `id_item` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_proceso` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `titulo` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_tipo_item` int(5) DEFAULT '0',
  `autor` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT NULL,
  `id_estado_actual` int(5) unsigned DEFAULT '0',
  `fecha_ultima_modificacion` datetime DEFAULT NULL,
  `numero_step_actual` int(5) unsigned DEFAULT '1',
  `id_conexion_bd` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT 'data_esp',
  `xml_data` text COLLATE latin1_spanish_ci NOT NULL,
  `temp_key` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_desktop_actual` int(5) unsigned DEFAULT '0',
  `id_entity` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `data_entity` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_item: 0 rows
DELETE FROM `wf_item`;
/*!40000 ALTER TABLE `wf_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_item` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_item_event
DROP TABLE IF EXISTS `wf_item_event`;
CREATE TABLE IF NOT EXISTS `wf_item_event` (
  `id_item_event` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_item` int(10) unsigned DEFAULT '0',
  `id_tipo_evento` int(5) unsigned NOT NULL DEFAULT '0',
  `usuario` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `observaciones` text COLLATE latin1_spanish_ci,
  `fecha_evento` datetime DEFAULT NULL,
  `id_desktop_item` int(10) unsigned DEFAULT NULL,
  `step` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_item_event`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_item_event: 0 rows
DELETE FROM `wf_item_event`;
/*!40000 ALTER TABLE `wf_item_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_item_event` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_item_event_type
DROP TABLE IF EXISTS `wf_item_event_type`;
CREATE TABLE IF NOT EXISTS `wf_item_event_type` (
  `id_tipo_evento` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_evento` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_tipo_evento`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_item_event_type: 0 rows
DELETE FROM `wf_item_event_type`;
/*!40000 ALTER TABLE `wf_item_event_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_item_event_type` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_item_status
DROP TABLE IF EXISTS `wf_item_status`;
CREATE TABLE IF NOT EXISTS `wf_item_status` (
  `id_estado_item` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_estado_item` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_estado_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_item_status: 0 rows
DELETE FROM `wf_item_status`;
/*!40000 ALTER TABLE `wf_item_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_item_status` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_item_type
DROP TABLE IF EXISTS `wf_item_type`;
CREATE TABLE IF NOT EXISTS `wf_item_type` (
  `id_tipo_item` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_item` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_tipo_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_item_type: 0 rows
DELETE FROM `wf_item_type`;
/*!40000 ALTER TABLE `wf_item_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_item_type` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_process
DROP TABLE IF EXISTS `wf_process`;
CREATE TABLE IF NOT EXISTS `wf_process` (
  `id_proceso` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `nombre_proceso` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `desc_proceso` text COLLATE latin1_spanish_ci,
  `notas_internas` text COLLATE latin1_spanish_ci,
  PRIMARY KEY (`id_proceso`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_process: 0 rows
DELETE FROM `wf_process`;
/*!40000 ALTER TABLE `wf_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_process` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_process_step
DROP TABLE IF EXISTS `wf_process_step`;
CREATE TABLE IF NOT EXISTS `wf_process_step` (
  `id_step` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_proceso` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  `numero_step` int(5) unsigned NOT NULL DEFAULT '1',
  `id_tipo_step` int(5) unsigned DEFAULT '0',
  `nombre_step` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `desc_step` text COLLATE latin1_spanish_ci,
  `xml_data` text COLLATE latin1_spanish_ci,
  `id_esquema_asignacion` int(5) unsigned DEFAULT '0',
  `permite_participantes_dinamicos` int(5) unsigned DEFAULT '0',
  PRIMARY KEY (`id_step`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_process_step: 0 rows
DELETE FROM `wf_process_step`;
/*!40000 ALTER TABLE `wf_process_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_process_step` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_process_step_participant
DROP TABLE IF EXISTS `wf_process_step_participant`;
CREATE TABLE IF NOT EXISTS `wf_process_step_participant` (
  `id_proceso_participante` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_proceso` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `numero_step` int(5) unsigned DEFAULT '1',
  `usuario` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `email` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_proceso_participante`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_process_step_participant: 0 rows
DELETE FROM `wf_process_step_participant`;
/*!40000 ALTER TABLE `wf_process_step_participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_process_step_participant` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_step_type
DROP TABLE IF EXISTS `wf_step_type`;
CREATE TABLE IF NOT EXISTS `wf_step_type` (
  `id_tipo_step` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_tipo_step` varchar(255) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_tipo_step`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_step_type: 0 rows
DELETE FROM `wf_step_type`;
/*!40000 ALTER TABLE `wf_step_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_step_type` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_user_desktop
DROP TABLE IF EXISTS `wf_user_desktop`;
CREATE TABLE IF NOT EXISTS `wf_user_desktop` (
  `id_desktop_item` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_item` int(10) unsigned DEFAULT '0',
  `usuario` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  `id_estado_desktop_item` int(5) unsigned DEFAULT '0',
  `fecha_ingreso` datetime DEFAULT NULL,
  `fecha_apertura` datetime DEFAULT NULL,
  `observaciones` text COLLATE latin1_spanish_ci,
  `numero_step` int(5) unsigned NOT NULL DEFAULT '1',
  `temp_key` varchar(50) COLLATE latin1_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_desktop_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_user_desktop: 0 rows
DELETE FROM `wf_user_desktop`;
/*!40000 ALTER TABLE `wf_user_desktop` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_user_desktop` ENABLE KEYS */;

-- Volcando estructura para tabla ai_defensoria_req_qa.wf_user_desktop_status
DROP TABLE IF EXISTS `wf_user_desktop_status`;
CREATE TABLE IF NOT EXISTS `wf_user_desktop_status` (
  `id_estado_desktop_item` int(5) unsigned NOT NULL DEFAULT '0',
  `desc_estado_desktop_item` varchar(255) COLLATE latin1_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id_estado_desktop_item`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

-- Volcando datos para la tabla ai_defensoria_req_qa.wf_user_desktop_status: 0 rows
DELETE FROM `wf_user_desktop_status`;
/*!40000 ALTER TABLE `wf_user_desktop_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_user_desktop_status` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
