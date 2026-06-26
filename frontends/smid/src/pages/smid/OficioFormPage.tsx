import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, TextInput, Select, Group, Stack, Alert } from '@mantine/core';
import { ArrowLeft, Save, Info } from 'lucide-react';
import { notifications } from '@mantine/notifications';
import { useOficioFormData, useCreateOficio } from '../../hooks/useSmid';
import type { OficioPayload, TipoDocumento } from '../../types/smid';

const TIPOS: { value: TipoDocumento; label: string }[] = [
  { value: 'OFICIO_RECOMENDACION', label: 'Oficio de Recomendación' },
  { value: 'OFICIO_PIDE_CUENTA', label: 'Oficio Pide Cuenta' },
  { value: 'DERIVACION_CGR', label: 'Derivación CGR' },
  { value: 'CIERRE_ADMINISTRATIVO', label: 'Cierre Administrativo' },
];

export const OficioFormPage = () => {
  const { evalId } = useParams<{ evalId: string }>();
  const navigate = useNavigate();
  const evalIdNum = Number(evalId);
  const { data: formData, isLoading } = useOficioFormData(evalIdNum);
  const mutation = useCreateOficio();

  const [form, setForm] = useState<OficioPayload>({
    evaluacion: { id: evalIdNum },
    nroOficio: '',
    tipoDocumento: 'OFICIO_RECOMENDACION',
    fechaEnvio: '',
    plazoDias: 10,
  });

  const handleSubmit = async () => {
    if (!form.nroOficio || !form.fechaEnvio) {
      notifications.show({ title: 'Campos requeridos', message: 'Complete todos los campos.', color: 'red' });
      return;
    }
    try {
      const result = await mutation.mutateAsync(form);
      notifications.show({
        title: 'Oficio registrado',
        message: `Vencimiento calculado: ${result.fechaVencimiento} — Estado: ${result.estadoActual}`,
        color: 'teal',
      });
      navigate(-1);
    } catch {
      notifications.show({ title: 'Error', message: 'No se pudo crear el oficio.', color: 'red' });
    }
  };

  if (isLoading) return <div className="p-10 text-center"><Text c="dimmed">Cargando...</Text></div>;

  return (
    <div className="p-6 sm:p-10 max-w-2xl mx-auto flex flex-col gap-6">
      <div>
        <button onClick={() => navigate(-1)} className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 mb-2">
          <ArrowLeft size={14} /> Volver
        </button>
        <Badge color="pink" variant="light" radius="sm" className="mb-2">SMID</Badge>
        <Title order={2} className="font-extrabold tracking-tight">Generar Oficio de Seguimiento</Title>
      </div>

      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b bg-gray-50/50">
          <Text fw={700} className="text-simdPrimary">Formulario de Oficio</Text>
        </div>
        <div className="p-6">
          <Stack gap="lg">
            {formData?.evaluacion && (
              <Alert color="blue" variant="light" icon={<Info size={14} />} className="border-l-4 border-l-blue-400">
                <Group gap="xl">
                  <div><Text size="xs" c="dimmed">Garante:</Text><Text size="sm" fw={600}>{formData.evaluacion.institucion?.nombre || '-'}</Text></div>
                  <div><Text size="xs" c="dimmed">Expediente:</Text><Text size="sm" fw={600}>{formData.evaluacion.requerimiento?.codigo || '-'}</Text></div>
                </Group>
              </Alert>
            )}

            <TextInput label="Número de Oficio" placeholder="Ej: ORD-225" required value={form.nroOficio} onChange={(e) => setForm({ ...form, nroOficio: e.target.value })} />

            <Select
              label="Tipo de Documento"
              data={TIPOS}
              value={form.tipoDocumento}
              onChange={(val) => setForm({ ...form, tipoDocumento: (val as TipoDocumento) || 'OFICIO_RECOMENDACION' })}
              allowDeselect={false}
            />

            <Group grow>
              <TextInput label="Fecha de Envío" type="date" required value={form.fechaEnvio} onChange={(e) => setForm({ ...form, fechaEnvio: e.target.value })} />
              <TextInput label="Plazo (Días Hábiles)" type="number" min={1} required value={String(form.plazoDias)} onChange={(e) => setForm({ ...form, plazoDias: Number(e.target.value) })} description="El sistema calculará el vencimiento automáticamente." />
            </Group>

            <Group justify="flex-end" pt="md" className="border-t">
              <Button variant="default" onClick={() => navigate(-1)}>Cancelar</Button>
              <Button color="pink" leftSection={<Save size={16} />} onClick={handleSubmit} loading={mutation.isPending}>
                Registrar Oficio
              </Button>
            </Group>
          </Stack>
        </div>
      </div>
    </div>
  );
};
