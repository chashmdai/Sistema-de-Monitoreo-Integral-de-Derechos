import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import * as smidApi from '../api/smid';
import type {
  RequerimientoPayload, EvaluacionPayload, OficioPayload,
  LeyPayload, TramitePayload,
} from '../types/smid';

// ============================================================
// REQUERIMIENTOS
// ============================================================

export const useRequerimientos = () =>
  useQuery({ queryKey: ['requerimientos'], queryFn: smidApi.fetchRequerimientos });

export const useRequerimientoFormData = () =>
  useQuery({ queryKey: ['requerimiento-form-data'], queryFn: smidApi.fetchRequerimientoFormData });

export const useRequerimientoDetail = (codigo: string) =>
  useQuery({
    queryKey: ['requerimiento', codigo],
    queryFn: () => smidApi.fetchRequerimientoDetail(codigo),
    enabled: !!codigo,
  });

export const useCreateRequerimiento = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: RequerimientoPayload) => smidApi.createRequerimiento(payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['requerimientos'] }),
  });
};

export const useDeleteRequerimiento = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (codigo: string) => smidApi.deleteRequerimiento(codigo),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['requerimientos'] }),
  });
};

// ============================================================
// EVALUACIONES
// ============================================================

export const useEvaluacionFormData = (reqId: string) =>
  useQuery({
    queryKey: ['evaluacion-form-data', reqId],
    queryFn: () => smidApi.fetchEvaluacionFormData(reqId),
    enabled: !!reqId,
  });

export const useCreateEvaluacion = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: EvaluacionPayload) => smidApi.createEvaluacion(payload),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['requerimientos'] });
      qc.invalidateQueries({ queryKey: ['requerimiento'] });
      qc.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });
};

// ============================================================
// OFICIOS
// ============================================================

export const useOficioFormData = (evalId: number) =>
  useQuery({
    queryKey: ['oficio-form-data', evalId],
    queryFn: () => smidApi.fetchOficioFormData(evalId),
    enabled: !!evalId,
  });

export const useCreateOficio = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: OficioPayload) => smidApi.createOficio(payload),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['requerimiento'] });
      qc.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });
};

// ============================================================
// LEGISLATIVO
// ============================================================

export const useLeyes = () =>
  useQuery({ queryKey: ['leyes'], queryFn: smidApi.fetchLeyes });

export const useLeyDetail = (id: number) =>
  useQuery({
    queryKey: ['ley', id],
    queryFn: () => smidApi.fetchLeyDetail(id),
    enabled: !!id,
  });

export const useCreateLey = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: LeyPayload) => smidApi.createLey(payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['leyes'] }),
  });
};

export const useCreateTramite = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: TramitePayload) => smidApi.createTramite(payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['ley'] }),
  });
};

// ============================================================
// REPORTES
// ============================================================

export const useDashboard = () =>
  useQuery({ queryKey: ['dashboard'], queryFn: smidApi.fetchDashboard });

export const useComparacion = (g1: number, g2: number, enabled: boolean) =>
  useQuery({
    queryKey: ['comparacion', g1, g2],
    queryFn: () => smidApi.fetchComparacion(g1, g2),
    enabled,
  });
