import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, TextInput, Textarea, Select, Group, Stack } from '@mantine/core';
import { ArrowLeft, Calculator } from 'lucide-react';
import { notifications } from '@mantine/notifications';
import { useCreateTramite, useLeyDetail } from '../../hooks/useSmid';
import type { TramitePayload } from '../../types/smid';

const SCORE_15 = ['1', '2', '3', '4', '5'].map((v) => ({ value: v, label: v }));

export const TramiteFormPage = () => {
  const { leyId } = useParams<{ leyId: string }>();
  const navigate = useNavigate();
  const leyIdNum = Number(leyId);
  const { data: leyData } = useLeyDetail(leyIdNum);
  const mutation = useCreateTramite();

  const [form, setForm] = useState<TramitePayload>({
    ley: { id: leyIdNum },
    nombreTramite: '',
    fechaTramite: '',
    analisisInteresSuperior: '',
    analisisAutonomia: '',
    analisisParticipacion: '',
    scoreInteresSup: 1, scoreParticipacion: 1, scoreAutonomia: 1,
    scoreInterseccion: 1, scoreRolGarante: 1,
    scoreProporcion: 1, scoreOperatividad: 1,
  });

  const set = (field: keyof TramitePayload, val: any) => setForm({ ...form, [field]: val });

  const handleSubmit = async () => {
    if (!form.nombreTramite) {
      notifications.show({ title: 'Campo requerido', message: 'Ingrese el nombre del trámite.', color: 'red' });
      return;
    }
    try {
      const result = await mutation.mutateAsync(form);
      notifications.show({
        title: 'Trámite registrado',
        message: `Índice Compuesto: ${result.indiceCompuesto}`,
        color: 'teal',
      });
      navigate(`/smid/legislativo/${leyIdNum}`);
    } catch {
      notifications.show({ title: 'Error', message: 'No se pudo guardar el trámite.', color: 'red' });
    }
  };

  return (
    <div className="p-6 sm:p-10 max-w-4xl mx-auto flex flex-col gap-6">
      <div>
        <button onClick={() => navigate(`/smid/legislativo/${leyIdNum}`)} className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 mb-2">
          <ArrowLeft size={14} /> Volver
        </button>
        <Badge color="orange" variant="light" radius="sm" className="mb-2">SMID</Badge>
        <Title order={2} className="font-extrabold tracking-tight">Registrar Hito Legislativo</Title>
        <Text size="sm" c="dimmed">Ley: <strong>{leyData?.ley?.nombreLey || '...'}</strong></Text>
      </div>

      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
        <Stack gap="lg">
          <Group grow>
            <TextInput label="Nombre del Trámite" placeholder="Ej: Primer Trámite Constitucional" required value={form.nombreTramite} onChange={(e) => set('nombreTramite', e.target.value)} />
            <TextInput label="Fecha" type="date" required value={form.fechaTramite} onChange={(e) => set('fechaTramite', e.target.value)} />
          </Group>

          <Text size="xs" c="dimmed" tt="uppercase" fw={700} className="border-b pb-2">Análisis Cualitativo</Text>
          <Textarea label="Interés Superior del Niño" rows={3} value={form.analisisInteresSuperior || ''} onChange={(e) => set('analisisInteresSuperior', e.target.value)} />
          <Textarea label="Autonomía Progresiva" rows={3} value={form.analisisAutonomia || ''} onChange={(e) => set('analisisAutonomia', e.target.value)} />
          <Textarea label="Participación" rows={3} value={form.analisisParticipacion || ''} onChange={(e) => set('analisisParticipacion', e.target.value)} />

          <Text size="xs" tt="uppercase" fw={700} className="text-blue-700 border-b pb-2">Estándares Nacionales (IRN)</Text>
          <div className="grid grid-cols-2 md:grid-cols-5 gap-3">
            <Select label="Interés Sup." data={SCORE_15} value={String(form.scoreInteresSup)} onChange={(v) => set('scoreInteresSup', Number(v))} allowDeselect={false} />
            <Select label="Participación" data={SCORE_15} value={String(form.scoreParticipacion)} onChange={(v) => set('scoreParticipacion', Number(v))} allowDeselect={false} />
            <Select label="Autonomía" data={SCORE_15} value={String(form.scoreAutonomia)} onChange={(v) => set('scoreAutonomia', Number(v))} allowDeselect={false} />
            <Select label="Intersección" data={SCORE_15} value={String(form.scoreInterseccion)} onChange={(v) => set('scoreInterseccion', Number(v))} allowDeselect={false} />
            <Select label="Rol Garante" data={SCORE_15} value={String(form.scoreRolGarante)} onChange={(v) => set('scoreRolGarante', Number(v))} allowDeselect={false} />
          </div>

          <Text size="xs" tt="uppercase" fw={700} className="text-gray-600 border-b pb-2">Estándares Internacionales (IRRI)</Text>
          <Group grow>
            <Select label="Proporcionalidad" data={SCORE_15} value={String(form.scoreProporcion)} onChange={(v) => set('scoreProporcion', Number(v))} allowDeselect={false} />
            <Select label="Operatividad" data={SCORE_15} value={String(form.scoreOperatividad)} onChange={(v) => set('scoreOperatividad', Number(v))} allowDeselect={false} />
          </Group>

          <Group justify="flex-end" pt="md" className="border-t">
            <Button variant="default" onClick={() => navigate(`/smid/legislativo/${leyIdNum}`)}>Cancelar</Button>
            <Button color="orange" leftSection={<Calculator size={16} />} onClick={handleSubmit} loading={mutation.isPending}>
              Guardar y Calcular Índices
            </Button>
          </Group>
        </Stack>
      </div>
    </div>
  );
};
