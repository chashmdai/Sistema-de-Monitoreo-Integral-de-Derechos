import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Title,
  Text,
  Badge,
  Button,
  Select,
  Group,
  Stack,
  Alert,
} from "@mantine/core";
import { ArrowLeft, Save, AlertTriangle } from "lucide-react";
import { notifications } from "@mantine/notifications";
import {
  useEvaluacionFormData,
  useCreateEvaluacion,
} from "../../hooks/useSmid";
import { ScoreSelect } from "../../components/smid/ScoreSelect";
import type { EvaluacionPayload } from "../../types/smid";

const OMISION_LABELS: Record<number, string> = {
  0: "0 - Actuó oportunamente",
  1: "1 - Retardo injustificado",
  2: "2 - Inacción total",
};
const REVICTIMIZA_LABELS: Record<number, string> = {
  0: "0 - Trato adecuado",
  1: "1 - Inadecuado puntual",
  2: "2 - Degradante/Sistemático",
};
const CONTRADICCION_LABELS: Record<number, string> = {
  0: "0 - Coordinado",
  1: "1 - Descoordinación menor",
  2: "2 - Órdenes contradictorias",
};
const DANIO_FISICO_LABELS: Record<number, string> = {
  0: "0 - Sin daño",
  1: "1 - Leve/Moderado",
  2: "2 - Grave",
};
const DANIO_PSI_LABELS: Record<number, string> = {
  0: "0 - Sin daño",
  1: "1 - Leve/Moderado",
  2: "2 - Grave",
};
const DANIO_EST_LABELS: Record<number, string> = {
  0: "0 - No afecta",
  1: "1 - Temporal",
  2: "2 - Permanente",
};
const URGENCIA_LABELS: Record<number, string> = {
  0: "0 - Baja",
  1: "1 - Media",
  2: "2 - Alta/Inmediata",
};

export const EvaluacionFormPage = () => {
  const { reqId } = useParams<{ reqId: string }>();
  const navigate = useNavigate();
  // Se agregó isError para capturar fallos de red silenciosos
  const {
    data: formData,
    isLoading,
    isError,
  } = useEvaluacionFormData(reqId || "");
  const mutation = useCreateEvaluacion();

  const [form, setForm] = useState<EvaluacionPayload>({
    requerimiento: { codigo: reqId || "" },
    institucion: { id: 0 },
    derecho: { id: 0 },
    scoreEfectividad: 0,
    scorePriorizacion: 0,
    scoreParticipacion: 0,
    scoreIntersector: 0,
    danioFisico: 0,
    danioPsicologico: 0,
    danioEstructural: 0,
    danioUrgencia: 0,
    estadoOmision: 0,
    estadoRevictimiza: 0,
    estadoContradiccion: 0,
  });

  const set = (field: keyof EvaluacionPayload, val: any) =>
    setForm({ ...form, [field]: val });

  const handleSubmit = async () => {
    if (!form.institucion.id || !form.derecho.id) {
      notifications.show({
        title: "Campos requeridos",
        message: "Seleccione institución y derecho.",
        color: "red",
      });
      return;
    }
    try {
      const result = await mutation.mutateAsync(form);
      if (result.alertaCritica) {
        notifications.show({
          title: "¡ALERTA CRÍTICA!",
          message: result.mensaje,
          color: "red",
          icon: <AlertTriangle size={16} />,
          autoClose: 8000,
        });
      } else {
        notifications.show({
          title: "Evaluación registrada",
          message: `Índice de alineación: ${result.evaluacion.indiceAlineacion}`,
          color: "teal",
        });
      }
      navigate(`/smid/requerimientos/${reqId}`);
    } catch {
      notifications.show({
        title: "Error",
        message: "No se pudo guardar la evaluación.",
        color: "red",
      });
    }
  };

  // Escudo defensivo: Evita renderizar si no hay datos listos
  if (isLoading || !formData) {
    return (
      <div className="p-10 text-center">
        <Text c="dimmed">Cargando catálogos...</Text>
      </div>
    );
  }

  // Escudo defensivo: Muestra alerta si la API falló (ej. error 500)
  if (isError) {
    return (
      <div className="p-10 text-center max-w-lg mx-auto">
        <Alert
          color="red"
          title="Error de conexión"
          icon={<AlertTriangle size={16} />}
        >
          No se pudieron cargar los datos del formulario.
        </Alert>
      </div>
    );
  }

  return (
    <div className="p-6 sm:p-10 max-w-4xl mx-auto flex flex-col gap-6">
      <div>
        <button
          onClick={() => navigate(`/smid/requerimientos/${reqId}`)}
          className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 mb-2"
        >
          <ArrowLeft size={14} /> Volver al Expediente
        </button>
        <Badge color="pink" variant="light" radius="sm" className="mb-2">
          SMID
        </Badge>
        <Title order={2} className="font-extrabold tracking-tight">
          Evaluación de Garante
        </Title>
        <Text size="sm" c="dimmed">
          Expediente: <strong>{reqId}</strong>
        </Text>
      </div>

      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b bg-gray-50/50">
          <Text fw={700} className="text-simdPrimary">
            Matriz de Evaluación
          </Text>
        </div>
        <div className="p-6">
          <Stack gap="lg">
            {/* Seccion 1 */}
            <div className="bg-gray-50 p-4 rounded-lg border">
              <Text size="xs" c="dimmed" tt="uppercase" fw={700} mb="sm">
                1. Sujeto Obligado y Derecho
              </Text>
              <Group grow>
                <Select
                  label="Institución Garante"
                  placeholder="Seleccione..."
                  // Se agregó el fallback || 'Sin nombre' para proteger el toLowerCase de Mantine
                  data={formData.instituciones.map((i) => ({
                    value: String(i.id),
                    label: i.nombre || "Sin nombre",
                  }))}
                  value={
                    form.institucion.id ? String(form.institucion.id) : null
                  }
                  onChange={(val) => set("institucion", { id: Number(val) })}
                  required
                />
                <Select
                  label="Derecho Vulnerado"
                  placeholder="Seleccione..."
                  // Se agregó el fallback || 'Sin nombre' para proteger el toLowerCase de Mantine
                  data={formData.derechos.map((d) => ({
                    value: String(d.id),
                    label: d.nombre || "Sin nombre",
                  }))}
                  value={form.derecho.id ? String(form.derecho.id) : null}
                  onChange={(val) => set("derecho", { id: Number(val) })}
                  required
                />
              </Group>
            </div>

            {/* Seccion 2 */}
            <Text
              size="xs"
              tt="uppercase"
              fw={700}
              className="text-simdPrimary border-b pb-2"
            >
              2. Alineación Paradigmática (0 a 4)
            </Text>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <ScoreSelect
                label="Efectividad"
                value={form.scoreEfectividad}
                onChange={(v) => set("scoreEfectividad", v)}
                max={4}
              />
              <ScoreSelect
                label="Priorización"
                value={form.scorePriorizacion}
                onChange={(v) => set("scorePriorizacion", v)}
                max={4}
              />
              <ScoreSelect
                label="Participación"
                value={form.scoreParticipacion}
                onChange={(v) => set("scoreParticipacion", v)}
                max={4}
              />
              <ScoreSelect
                label="Intersectorialidad"
                value={form.scoreIntersector}
                onChange={(v) => set("scoreIntersector", v)}
                max={4}
              />
            </div>

            {/* Seccion 3 */}
            <Text
              size="xs"
              tt="uppercase"
              fw={700}
              className="text-red-600 border-b pb-2"
            >
              3. Gravedad de la Vulneración
            </Text>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <ScoreSelect
                label="Daño Físico"
                value={form.danioFisico}
                onChange={(v) => set("danioFisico", v)}
                max={2}
                labels={DANIO_FISICO_LABELS}
              />
              <ScoreSelect
                label="Daño Psicológico"
                value={form.danioPsicologico}
                onChange={(v) => set("danioPsicologico", v)}
                max={2}
                labels={DANIO_PSI_LABELS}
              />
              <ScoreSelect
                label="Daño Estructural"
                value={form.danioEstructural}
                onChange={(v) => set("danioEstructural", v)}
                max={2}
                labels={DANIO_EST_LABELS}
              />
              <ScoreSelect
                label="Urgencia"
                value={form.danioUrgencia}
                onChange={(v) => set("danioUrgencia", v)}
                max={2}
                labels={URGENCIA_LABELS}
              />
            </div>

            {/* Seccion 4 */}
            <Text
              size="xs"
              tt="uppercase"
              fw={700}
              className="text-amber-700 border-b pb-2"
            >
              4. Responsabilidad Institucional
            </Text>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <ScoreSelect
                label="Omisión Estatal"
                value={form.estadoOmision}
                onChange={(v) => set("estadoOmision", v)}
                max={2}
                labels={OMISION_LABELS}
              />
              <ScoreSelect
                label="Revictimización"
                value={form.estadoRevictimiza}
                onChange={(v) => set("estadoRevictimiza", v)}
                max={2}
                labels={REVICTIMIZA_LABELS}
              />
              <ScoreSelect
                label="Contradicción"
                value={form.estadoContradiccion}
                onChange={(v) => set("estadoContradiccion", v)}
                max={2}
                labels={CONTRADICCION_LABELS}
              />
            </div>

            <Group justify="flex-end" pt="md" className="border-t">
              <Button
                variant="default"
                onClick={() => navigate(`/smid/requerimientos/${reqId}`)}
              >
                Cancelar
              </Button>
              <Button
                color="pink"
                leftSection={<Save size={16} />}
                onClick={handleSubmit}
                loading={mutation.isPending}
              >
                Guardar Evaluación
              </Button>
            </Group>
          </Stack>
        </div>
      </div>
    </div>
  );
};
