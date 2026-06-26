// ============================================================
// TIPOS DEL MÓDULO SMID
// ============================================================

export interface Eje {
  id: number;
  nombre: string;
}

export interface Derecho {
  id: number;
  nombre: string;
  eje: Eje;
}

export interface FactorRiesgo {
  id: number;
  nombre: string;
}

export interface Institucion {
  id: number;
  nombre: string;
  esEspecializado: boolean;
  region: string;
}

export type EstadoOficio = 'PENDIENTE' | 'EN_PLAZO' | 'POR_VENCER' | 'VENCIDO' | 'RESPONDIDO' | 'CERRADO';
export type TipoDocumento = 'OFICIO_RECOMENDACION' | 'OFICIO_PIDE_CUENTA' | 'DERIVACION_CGR' | 'CIERRE_ADMINISTRATIVO';

export interface OficioSeguimiento {
  id: number;
  evaluacion?: EvaluacionGarante | number;
  nroOficio: string;
  tipoDocumento: TipoDocumento;
  fechaEnvio: string;
  plazoDias: number;
  fechaVencimiento: string;
  estadoActual: EstadoOficio;
}

export interface EvaluacionGarante {
  id: number;
  requerimiento: Requerimiento | string;
  institucion: Institucion;
  derecho: Derecho;
  scoreEfectividad: number;
  scorePriorizacion: number;
  scoreParticipacion: number;
  scoreIntersector: number;
  danioFisico: number;
  danioPsicologico: number;
  danioEstructural: number;
  danioUrgencia: number;
  estadoOmision: number;
  estadoRevictimiza: number;
  estadoContradiccion: number;
  indiceAlineacion: number;
  oficios: OficioSeguimiento[];
}

export interface Requerimiento {
  codigo: string;
  anio: number;
  fechaIngreso: string;
  region: string;
  comuna: string;
  factoresRiesgo: FactorRiesgo[];
  evaluaciones: EvaluacionGarante[];
  prioridadReforzada: boolean;
}

export interface Ley {
  id: number;
  nombreLey: string;
  numeroBoletin: string;
  fechaInicio: string;
}

export interface TramiteLegislativo {
  id: number;
  ley: Ley;
  nombreTramite: string;
  fechaTramite: string;
  analisisInteresSuperior?: string;
  analisisAutonomia?: string;
  analisisParticipacion?: string;
  scoreInteresSup: number;
  scoreParticipacion: number;
  scoreAutonomia: number;
  scoreInterseccion: number;
  scoreProporcion: number;
  scoreRolGarante: number;
  scoreOperatividad: number;
  indiceIrn: number;
  indiceIrri: number;
  indiceCompuesto: number;
}

// ============================================================
// DTOs DE RESPUESTA
// ============================================================

export interface AlertaEvaluacionDTO {
  evaluacion: EvaluacionGarante;
  alertaCritica: boolean;
  mensaje: string;
}

export interface IndicadoresEvaluacion {
  gravedadTotal: number;
  danioInstitucional: number;
  alertaCritica: boolean;
}

export interface RequerimientoDetailResponse {
  requerimiento: Requerimiento;
  indicadores: Record<string, IndicadoresEvaluacion>;
}

export interface RankingDTO {
  nombreInstitucion: string;
  indicePromedio: number;
  cantidadCasos: number;
}

export interface DashboardStatsDTO {
  totalCasos: number;
  casosPrioritarios: number;
  labelsRegiones: string[];
  dataRegiones: number[];
  labelsInstituciones: string[];
  dataDesempeno: number[];
}

export interface DashboardResponseDTO {
  alertasCriticas: number;
  oficiosPendientes: number;
  totalInstituciones: number;
  stats: DashboardStatsDTO;
  ranking: RankingDTO[];
  matrizCalor: Record<string, Record<string, number>>;
  columnasDerechos: string[];
  instituciones: Institucion[];
}

export interface ComparacionDTO {
  nombreGarante1: string;
  scoreGarante1: number;
  nombreGarante2: string;
  scoreGarante2: number;
  infoComparacion: string;
}

// ============================================================
// PAYLOADS DE CREACIÓN (lo que envía React al backend)
// ============================================================

export interface RequerimientoPayload {
  codigo: string;
  anio: number;
  fechaIngreso: string;
  region: string;
  comuna: string;
  factoresRiesgo?: { id: number }[];
}

export interface EvaluacionPayload {
  requerimiento: { codigo: string };
  institucion: { id: number };
  derecho: { id: number };
  scoreEfectividad: number;
  scorePriorizacion: number;
  scoreParticipacion: number;
  scoreIntersector: number;
  danioFisico: number;
  danioPsicologico: number;
  danioEstructural: number;
  danioUrgencia: number;
  estadoOmision: number;
  estadoRevictimiza: number;
  estadoContradiccion: number;
}

export interface OficioPayload {
  evaluacion: { id: number };
  nroOficio: string;
  tipoDocumento: TipoDocumento;
  fechaEnvio: string;
  plazoDias: number;
}

export interface LeyPayload {
  nombreLey: string;
  numeroBoletin: string;
  fechaInicio: string;
}

export interface TramitePayload {
  ley: { id: number };
  nombreTramite: string;
  fechaTramite: string;
  analisisInteresSuperior?: string;
  analisisAutonomia?: string;
  analisisParticipacion?: string;
  scoreInteresSup: number;
  scoreParticipacion: number;
  scoreAutonomia: number;
  scoreInterseccion: number;
  scoreProporcion: number;
  scoreRolGarante: number;
  scoreOperatividad: number;
}
