import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, Table, Group, TextInput, Menu, ActionIcon } from '@mantine/core';
import { Plus, Search, Eye, ClipboardCheck, MoreVertical, FolderTree } from 'lucide-react';
import { useRequerimientos } from '../../hooks/useSmid';
import dayjs from 'dayjs';

export const RequerimientosListPage = () => {
  const navigate = useNavigate();
  const { data: requerimientos = [], isLoading } = useRequerimientos();
  const [search, setSearch] = useState('');

  const filtered = requerimientos.filter((r) =>
    r.codigo.toLowerCase().includes(search.toLowerCase()) ||
    r.comuna?.toLowerCase().includes(search.toLowerCase()) ||
    r.region?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-6 sm:p-10 max-w-[1600px] mx-auto flex flex-col gap-6">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
        <div>
          <Group gap="xs" mb={5}>
            <Badge color="pink" variant="light" radius="sm">Módulo SMID</Badge>
            <Text size="xs" c="dimmed" fw={600}>Gestión de Casos</Text>
          </Group>
          <Title order={1} className="font-extrabold tracking-tight">Requerimientos Activos</Title>
          <Text size="sm" c="dimmed" mt={4}>Bandeja de entrada de casos regionales</Text>
        </div>
        <Button color="pink" leftSection={<Plus size={16} />} onClick={() => navigate('/smid/requerimientos/nuevo')}>
          Nuevo Caso
        </Button>
      </div>

      <div className="bg-white rounded-xl border-t-4 border-t-simdPrimary shadow-sm flex-1 flex flex-col overflow-hidden">
        <div className="p-4 border-b bg-gray-50/50 flex flex-col md:flex-row justify-between gap-4">
          <TextInput
            placeholder="Buscar por código, comuna o región..."
            leftSection={<Search size={16} className="text-gray-400" />}
            className="w-full max-w-md"
            value={search}
            onChange={(e) => setSearch(e.currentTarget.value)}
          />
          <Text size="sm" c="dimmed" fw={500} pt={10}>{filtered.length} registros</Text>
        </div>

        <Table verticalSpacing="md" horizontalSpacing="lg" highlightOnHover>
          <Table.Thead className="bg-gray-50/80">
            <Table.Tr>
              <Table.Th>Código</Table.Th>
              <Table.Th>Fecha Ingreso</Table.Th>
              <Table.Th>Ubicación</Table.Th>
              <Table.Th className="text-right">Acciones</Table.Th>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {isLoading ? (
              <Table.Tr><Table.Td colSpan={4} className="text-center py-10"><Text c="dimmed">Cargando...</Text></Table.Td></Table.Tr>
            ) : filtered.length > 0 ? (
              filtered.map((req) => (
                <Table.Tr key={req.codigo}>
                  <Table.Td>
                    <Group gap={6}>
                      <FolderTree size={14} className="text-gray-400" />
                      <Text size="sm" fw={700} className="text-simdPrimary">{req.codigo}</Text>
                    </Group>
                  </Table.Td>
                  <Table.Td>
                    <Text size="sm" c="dimmed">{req.fechaIngreso ? dayjs(req.fechaIngreso).format('DD/MM/YYYY') : '-'}</Text>
                  </Table.Td>
                  <Table.Td>
                    <Text size="sm" fw={500}>{req.comuna}</Text>
                    <Text size="xs" c="dimmed">{req.region}</Text>
                  </Table.Td>
                  <Table.Td className="text-right">
                    <Menu position="bottom-end" withArrow>
                      <Menu.Target><ActionIcon variant="subtle" color="gray"><MoreVertical size={18} /></ActionIcon></Menu.Target>
                      <Menu.Dropdown>
                        <Menu.Item leftSection={<Eye size={14} />} onClick={() => navigate(`/smid/requerimientos/${req.codigo}`)}>
                          Ver Expediente
                        </Menu.Item>
                        <Menu.Item leftSection={<ClipboardCheck size={14} />} onClick={() => navigate(`/smid/evaluaciones/nueva/${req.codigo}`)}>
                          Evaluar
                        </Menu.Item>
                      </Menu.Dropdown>
                    </Menu>
                  </Table.Td>
                </Table.Tr>
              ))
            ) : (
              <Table.Tr><Table.Td colSpan={4} className="text-center py-10"><Text c="dimmed">No hay requerimientos activos.</Text></Table.Td></Table.Tr>
            )}
          </Table.Tbody>
        </Table>
      </div>
    </div>
  );
};
