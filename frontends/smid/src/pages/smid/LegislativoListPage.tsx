import { useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, Table, Group } from '@mantine/core';
import { PlusCircle, BookOpen, Calendar } from 'lucide-react';
import { useLeyes } from '../../hooks/useSmid';
import dayjs from 'dayjs';

export const LegislativoListPage = () => {
  const navigate = useNavigate();
  const { data: leyes = [], isLoading } = useLeyes();

  return (
    <div className="p-6 sm:p-10 max-w-[1400px] mx-auto flex flex-col gap-6">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
        <div>
          <Group gap="xs" mb={5}>
            <Badge color="orange" variant="light" radius="sm">Módulo SMID</Badge>
            <Text size="xs" c="dimmed" fw={600}>Observatorio Legislativo</Text>
          </Group>
          <Title order={1} className="font-extrabold tracking-tight">Observatorio Legislativo</Title>
          <Text size="sm" c="dimmed" mt={4}>Seguimiento de normas y estándares de infancia</Text>
        </div>
        <Button color="orange" leftSection={<PlusCircle size={16} />} onClick={() => navigate('/smid/legislativo/nueva')}>
          Registrar Nueva Ley
        </Button>
      </div>

      <div className="bg-white rounded-xl border-t-4 border-t-simdAccent shadow-sm flex-1 flex flex-col overflow-hidden">
        <Table verticalSpacing="md" horizontalSpacing="lg" highlightOnHover>
          <Table.Thead className="bg-gray-50/80">
            <Table.Tr>
              <Table.Th style={{ width: 150 }}>Boletín</Table.Th>
              <Table.Th>Nombre de la Ley / Proyecto</Table.Th>
              <Table.Th>Inicio Tramitación</Table.Th>
              <Table.Th className="text-right">Acciones</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {isLoading ? (
              <Table.Tr><Table.Td colSpan={4} className="text-center py-10"><Text c="dimmed">Cargando...</Text></Table.Td></Table.Tr>
            ) : leyes.length > 0 ? (
              leyes.map((ley) => (
                <Table.Tr key={ley.id}>
                  <Table.Td>
                    <Badge variant="light" color="orange" size="lg" className="font-mono font-bold">
                      {ley.numeroBoletin}
                    </Badge>
                  </Table.Td>
                  <Table.Td><Text size="sm" fw={500}>{ley.nombreLey}</Text></Table.Td>
                  <Table.Td>
                    <Group gap={6}>
                      <Calendar size={12} className="text-gray-400" />
                      <Text size="sm" c="dimmed">{ley.fechaInicio ? dayjs(ley.fechaInicio).format('DD/MM/YYYY') : '-'}</Text>
                    </Group>
                  </Table.Td>
                  <Table.Td className="text-right">
                    <Button size="xs" variant="light" color="orange" leftSection={<BookOpen size={14} />} onClick={() => navigate(`/smid/legislativo/${ley.id}`)}>
                      Ver Bitácora
                    </Button>
                  </Table.Td>
                </Table.Tr>
              ))
            ) : (
              <Table.Tr><Table.Td colSpan={4} className="text-center py-10"><Text c="dimmed">No hay leyes en seguimiento.</Text></Table.Td></Table.Tr>
            )}
          </Table.Tbody>
        </Table>
      </div>
    </div>
  );
};
