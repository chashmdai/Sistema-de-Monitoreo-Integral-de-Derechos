import { useParams, useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, Table, Group, Stack, Paper } from '@mantine/core';
import { ArrowLeft, Plus, FileSignature } from 'lucide-react';
import { useRequerimientoDetail } from '../../hooks/useSmid';
import { AlertaCriticaBadge } from '../../components/smid/AlertaCriticaBadge';
import { PrioridadReforzadaBadge } from '../../components/smid/PrioridadReforzadaBadge';
import { EstadoOficioBadge } from '../../components/smid/EstadoOficioBadge';
import { IndiceCard } from '../../components/smid/IndiceCard';
import dayjs from 'dayjs';

export const RequerimientoDetailPage = () => {
  const { codigo } = useParams<{ codigo: string }>();
  const navigate = useNavigate();
  const { data, isLoading } = useRequerimientoDetail(codigo || '');

  if (isLoading) return <div className="p-10 text-center"><Text c="dimmed">Cargando expediente...</Text></div>;
  if (!data) return <div className="p-10 text-center"><Text c="red">Caso no encontrado.</Text></div>;

  const { requerimiento: req, indicadores } = data;

  return (
    <div className="p-6 sm:p-10 max-w-[1400px] mx-auto flex flex-col gap-6">
      <button onClick={() => navigate('/smid/requerimientos')} className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 w-fit">
        <ArrowLeft size={14} /> Volver al listado
      </button>

      {/* Header */}
      <Paper p="lg" radius="md" className="bg-white ring-1 ring-gray-900/5 shadow-sm">
        <Group justify="space-between" align="flex-start">
          <div>
            <Group gap="sm" mb={4}>
              <Badge variant="light" color="pink" size="sm">Expediente: {req.codigo}</Badge>
              <Text size="xs" c="dimmed">Ingresado: {req.fechaIngreso ? dayjs(req.fechaIngreso).format('DD/MM/YYYY') : '-'}</Text>
            </Group>
            <Title order={2} className="font-extrabold text-gray-900">
              Región {req.region} — {req.comuna}
            </Title>
          </div>
          <PrioridadReforzadaBadge factores={req.factoresRiesgo || []} />
        </Group>
      </Paper>

      {/* Evaluaciones header */}
      <Group justify="space-between" className="border-b pb-2">
        <Title order={4} className="font-bold text-gray-600">Evaluaciones de Garantes</Title>
        <Button size="sm" color="pink" leftSection={<Plus size={14} />} onClick={() => navigate(`/smid/evaluaciones/nueva/${req.codigo}`)}>
          Nueva Evaluación
        </Button>
      </Group>

      {/* Evaluaciones */}
      {(!req.evaluaciones || req.evaluaciones.length === 0) ? (
        <Paper p="xl" radius="md" className="text-center bg-gray-50">
          <Text c="dimmed">No hay evaluaciones registradas para este caso.</Text>
        </Paper>
      ) : (
        req.evaluaciones.map((ev) => {
          const ind = indicadores?.[String(ev.id)];
          const isCritica = ind?.alertaCritica || false;

          return (
            <Paper
              key={ev.id}
              radius="md"
              className={`bg-white ring-1 ring-gray-900/5 shadow-sm overflow-hidden border-l-[5px] ${isCritica ? 'border-l-red-500' : 'border-l-teal-500'}`}
            >
              {/* Eval header */}
              <div className="px-6 py-4 border-b bg-gray-50/30 flex justify-between items-center">
                <Group gap="sm">
                  <Text fw={700} className="text-simdPrimary">{ev.institucion?.nombre || 'Institución'}</Text>
                  <Text c="dimmed">|</Text>
                  <Text size="sm" c="dimmed" tt="uppercase">{ev.derecho?.nombre || 'Derecho'}</Text>
                </Group>
                <AlertaCriticaBadge critica={isCritica} />
              </div>

              <div className="p-6">
                {/* Scores row */}
                <div className="flex flex-wrap items-center gap-8 mb-6">
                  <div className="border-r pr-8">
                    <IndiceCard valor={ev.indiceAlineacion ?? 0} label="Índice Alineación" />
                  </div>
                  <div className="flex gap-8">
                    <div className="text-center">
                      <Text className="text-xl font-bold">{ev.scoreEfectividad}</Text>
                      <Text size="xs" c="dimmed">Efectividad</Text>
                    </div>
                    <div className="text-center">
                      <Text className="text-xl font-bold">{ev.scorePriorizacion}</Text>
                      <Text size="xs" c="dimmed">Priorización</Text>
                    </div>
                    <div className="text-center">
                      <Text className="text-xl font-bold">{ev.scoreParticipacion}</Text>
                      <Text size="xs" c="dimmed">Participación</Text>
                    </div>
                    <div className="text-center">
                      <Text className="text-xl font-bold">{ev.scoreIntersector}</Text>
                      <Text size="xs" c="dimmed">Intersectorialidad</Text>
                    </div>
                  </div>
                </div>

                {/* Oficios */}
                <div className="bg-gray-50 p-4 rounded-lg border">
                  <Group justify="space-between" mb="sm">
                    <Text size="sm" fw={700} c="dimmed" tt="uppercase">Gestión de Oficios</Text>
                    <Button size="xs" variant="default" leftSection={<FileSignature size={14} />} onClick={() => navigate(`/smid/oficios/nuevo/${ev.id}`)}>
                      Generar Oficio
                    </Button>
                  </Group>

                  {(!ev.oficios || ev.oficios.length === 0) ? (
                    <Text size="sm" c="dimmed" ta="center" py="md">No hay oficios gestionados para este garante.</Text>
                  ) : (
                    <Table verticalSpacing="xs" highlightOnHover className="bg-white rounded">
                      <Table.Thead className="bg-gray-100/50">
                        <Table.Tr>
                          <Table.Th>N° Oficio</Table.Th>
                          <Table.Th>Tipo</Table.Th>
                          <Table.Th>Vencimiento</Table.Th>
                          <Table.Th>Estado</Table.Th>
                        </Table.Tr>
                      </Table.Thead>
                      <Table.Tbody>
                        {ev.oficios.map((of_) => (
                          <Table.Tr key={of_.id}>
                            <Table.Td><Text size="sm" fw={700} className="text-simdPrimary">{of_.nroOficio}</Text></Table.Td>
                            <Table.Td><Text size="xs">{of_.tipoDocumento?.replace(/_/g, ' ')}</Text></Table.Td>
                            <Table.Td><Text size="xs">{of_.fechaVencimiento ? dayjs(of_.fechaVencimiento).format('DD/MM/YYYY') : '-'}</Text></Table.Td>
                            <Table.Td><EstadoOficioBadge estado={of_.estadoActual} /></Table.Td>
                          </Table.Tr>
                        ))}
                      </Table.Tbody>
                    </Table>
                  )}
                </div>
              </div>
            </Paper>
          );
        })
      )}
    </div>
  );
};
