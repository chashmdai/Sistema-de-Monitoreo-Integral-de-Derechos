import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, TextInput, Textarea, Stack, Group } from '@mantine/core';
import { Save } from 'lucide-react';
import { notifications } from '@mantine/notifications';
import { useCreateLey } from '../../hooks/useSmid';
import type { LeyPayload } from '../../types/smid';

export const LeyFormPage = () => {
  const navigate = useNavigate();
  const mutation = useCreateLey();
  const [form, setForm] = useState<LeyPayload>({ nombreLey: '', numeroBoletin: '', fechaInicio: '' });

  const handleSubmit = async () => {
    if (!form.nombreLey || !form.numeroBoletin) {
      notifications.show({ title: 'Campos requeridos', message: 'Complete nombre y boletín.', color: 'red' });
      return;
    }
    try {
      await mutation.mutateAsync(form);
      notifications.show({ title: 'Ley registrada', message: `Boletín ${form.numeroBoletin} ingresado.`, color: 'teal' });
      navigate('/smid/legislativo');
    } catch {
      notifications.show({ title: 'Error', message: 'No se pudo crear la ley.', color: 'red' });
    }
  };

  return (
    <div className="p-6 sm:p-10 max-w-xl mx-auto flex flex-col gap-6">
      <div>
        <Badge color="orange" variant="light" radius="sm" className="mb-2">SMID</Badge>
        <Title order={2} className="font-extrabold tracking-tight">Registro de Nueva Ley</Title>
      </div>

      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
        <Stack gap="md">
          <TextInput label="Número de Boletín" placeholder="Ej: 11.728-04" required value={form.numeroBoletin} onChange={(e) => setForm({ ...form, numeroBoletin: e.target.value })} description="Identificador oficial del Congreso." />
          <Textarea label="Nombre de la Ley / Proyecto" rows={3} placeholder="Título completo..." required value={form.nombreLey} onChange={(e) => setForm({ ...form, nombreLey: e.target.value })} />
          <TextInput label="Fecha de Inicio de Tramitación" type="date" value={form.fechaInicio} onChange={(e) => setForm({ ...form, fechaInicio: e.target.value })} />
          <Group justify="flex-end" pt="md" className="border-t">
            <Button variant="default" onClick={() => navigate('/smid/legislativo')}>Cancelar</Button>
            <Button color="orange" leftSection={<Save size={16} />} onClick={handleSubmit} loading={mutation.isPending}>Guardar Ley</Button>
          </Group>
        </Stack>
      </div>
    </div>
  );
};
