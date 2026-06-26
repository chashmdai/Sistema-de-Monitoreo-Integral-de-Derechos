import { api } from './axiosConfig';
import type {
  Requerimiento, RequerimientoPayload, RequerimientoDetailResponse,
  AlertaEvaluacionDTO, EvaluacionPayload,
  OficioSeguimiento, OficioPayload,
  Ley, LeyPayload, TramiteLegislativo, TramitePayload,
  DashboardResponseDTO, ComparacionDTO,
  FactorRiesgo, Institucion, Derecho,
} from '../types/smid';

// ============================================================
// REQUERIMIENTOS
// ============================================================

export const fetchRequerimientos = async (): Promise<Requerimiento[]> => {
  const { data } = await api.get('/smid/requerimientos');
  return data;
};

export const fetchRequerimientoFormData = async (): Promise<{ factores: FactorRiesgo[] }> => {
  const { data } = await api.get('/smid/requerimientos/form-data');
  return data;
};

export const fetchRequerimientoDetail = async (codigo: string): Promise<RequerimientoDetailResponse> => {
  const { data } = await api.get(`/smid/requerimientos/${codigo}`);
  return data;
};

export const createRequerimiento = async (payload: RequerimientoPayload): Promise<Requerimiento> => {
  const { data } = await api.post('/smid/requerimientos', payload);
  return data;
};

export const deleteRequerimiento = async (codigo: string): Promise<void> => {
  await api.delete(`/smid/requerimientos/${codigo}`);
};

// ============================================================
// EVALUACIONES
// ============================================================

export const fetchEvaluacionFormData = async (reqId: string): Promise<{
  requerimiento: Requerimiento;
  instituciones: Institucion[];
  derechos: Derecho[];
}> => {
  const { data } = await api.get(`/smid/evaluaciones/form-data/${reqId}`);
  return data;
};

export const createEvaluacion = async (payload: EvaluacionPayload): Promise<AlertaEvaluacionDTO> => {
  const { data } = await api.post('/smid/evaluaciones', payload);
  return data;
};

// ============================================================
// OFICIOS
// ============================================================

export const fetchOficioFormData = async (evalId: number): Promise<{
  evaluacion: any;
  tiposDocumento: string[];
  plazoDiasDefault: number;
}> => {
  const { data } = await api.get(`/smid/oficios/form-data/${evalId}`);
  return data;
};

export const createOficio = async (payload: OficioPayload): Promise<OficioSeguimiento> => {
  const { data } = await api.post('/smid/oficios', payload);
  return data;
};

// ============================================================
// LEGISLATIVO
// ============================================================

export const fetchLeyes = async (): Promise<Ley[]> => {
  const { data } = await api.get('/smid/legislativo/leyes');
  return data;
};

export const createLey = async (payload: LeyPayload): Promise<Ley> => {
  const { data } = await api.post('/smid/legislativo/leyes', payload);
  return data;
};

export const fetchLeyDetail = async (id: number): Promise<{ ley: Ley; tramites: TramiteLegislativo[] }> => {
  const { data } = await api.get(`/smid/legislativo/leyes/${id}`);
  return data;
};

export const fetchTramiteFormData = async (leyId: number): Promise<{ ley: Ley }> => {
  const { data } = await api.get(`/smid/legislativo/tramites/form-data/${leyId}`);
  return data;
};

export const createTramite = async (payload: TramitePayload): Promise<TramiteLegislativo> => {
  const { data } = await api.post('/smid/legislativo/tramites', payload);
  return data;
};

// ============================================================
// REPORTES
// ============================================================

export const fetchDashboard = async (): Promise<DashboardResponseDTO> => {
  const { data } = await api.get('/smid/reportes/dashboard');
  return data;
};

export const fetchComparacion = async (g1: number, g2: number): Promise<ComparacionDTO> => {
  const { data } = await api.get(`/smid/reportes/comparar?g1=${g1}&g2=${g2}`);
  return data;
};
