import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, TextInput, Select, Checkbox, Group, Stack, Alert } from '@mantine/core';
import { ArrowLeft, Save } from 'lucide-react';
import { notifications } from '@mantine/notifications';
import { useCreateRequerimiento, useRequerimientoFormData } from '../../hooks/useSmid';
import type { RequerimientoPayload } from '../../types/smid';

export const RequerimientoFormPage = () => {
  const navigate = useNavigate();
  const { data: formData } = useRequerimientoFormData();
  const mutation = useCreateRequerimiento();

  const [form, setForm] = useState<RequerimientoPayload>({
    codigo: '',
    anio: new Date().getFullYear(),
    fechaIngreso: '',
    region: '',
    comuna: '',
    factoresRiesgo: [],
  });

  const [selectedFactores, setSelectedFactores] = useState<number[]>([]);

  const handleSubmit = async () => {
    if (!form.codigo || !form.fechaIngreso || !form.region || !form.comuna) {
      notifications.show({ title: 'Campos requeridos', message: 'Complete todos los campos obligatorios.', color: 'red' });
      return;
    }
    try {
      await mutation.mutateAsync({
        ...form,
        factoresRiesgo: selectedFactores.map((id) => ({ id })),
      });
      notifications.show({ title: 'Caso registrado', message: `Requerimiento ${form.codigo} creado.`, color: 'teal' });
      navigate('/smid/requerimientos');
    } catch {
      notifications.show({ title: 'Error', message: 'No se pudo crear el requerimiento.', color: 'red' });
    }
  };

  return (
    <div className="p-6 sm:p-10 max-w-3xl mx-auto flex flex-col gap-6">
      <div>
        <button onClick={() => navigate('/smid/requerimientos')} className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 mb-2">
          <ArrowLeft size={14} /> Volver al listado
        </button>
        <Badge color="pink" variant="light" radius="sm" className="mb-2">Módulo SMID</Badge>
        <Title order={2} className="font-extrabold tracking-tight">Ingreso de Nuevo Requerimiento</Title>
        <Text size="sm" c="dimmed">Complete la ficha técnica para registrar un nuevo caso.</Text>
      </div>

      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b bg-gray-50/50">
          <Text fw={700} className="text-simdPrimary">Ficha de Ingreso</Text>
        </div>
        <div className="p-6">
          <Stack gap="lg">
            <Text size="xs" c="dimmed" tt="uppercase" fw={700} className="border-b pb-2">1. Identificación del Caso</Text>
            <TextInput label="Código Interno" placeholder="Ej: REQ-2026-001" required value={form.codigo} onChange={(e) => setForm({ ...form, codigo: e.target.value })} />
            <Group grow>
              <TextInput label="Año" type="number" required value={String(form.anio)} onChange={(e) => setForm({ ...form, anio: Number(e.target.value) })} />
              <TextInput label="Fecha de Ingreso" type="date" required value={form.fechaIngreso} onChange={(e) => setForm({ ...form, fechaIngreso: e.target.value })} />
            </Group>

            <Text size="xs" c="dimmed" tt="uppercase" fw={700} className="border-b pb-2 mt-2">2. Ubicación Territorial</Text>
            <Group grow>
              <Select
                label="Región"
                data={[{ value: "O'Higgins", label: "O'Higgins" }, { value: 'Maule', label: 'Maule' }]}
                value={form.region}
                onChange={(val) => setForm({ ...form, region: val || '' })}
                required
              />
              <TextInput label="Comuna" placeholder="Ej: Talca" required value={form.comuna} onChange={(e) => setForm({ ...form, comuna: e.target.value })} />
            </Group>

            <Text size="xs" c="dimmed" tt="uppercase" fw={700} className="border-b pb-2 mt-2">3. Factores de Interseccionalidad</Text>
            <div className="bg-gray-50 p-4 rounded-lg border">
              <Text size="xs" c="dimmed" mb="sm">Seleccione los factores de riesgo asociados:</Text>
              <Group gap="lg">
                {(formData?.factores || []).map((f) => (
                  <Checkbox
                    key={f.id}
                    label={f.nombre}
                    checked={selectedFactores.includes(f.id)}
                    onChange={(e) => {
                      if (e.currentTarget.checked) setSelectedFactores([...selectedFactores, f.id]);
                      else setSelectedFactores(selectedFactores.filter((id) => id !== f.id));
                    }}
                    color="grape"
                  />
                ))}
              </Group>
            </div>

            <Group justify="flex-end" pt="md" className="border-t">
              <Button variant="default" onClick={() => navigate('/smid/requerimientos')}>Cancelar</Button>
              <Button color="pink" leftSection={<Save size={16} />} onClick={handleSubmit} loading={mutation.isPending}>
                Guardar Caso
              </Button>
            </Group>
          </Stack>
        </div>
      </div>
    </div>
  );
};
