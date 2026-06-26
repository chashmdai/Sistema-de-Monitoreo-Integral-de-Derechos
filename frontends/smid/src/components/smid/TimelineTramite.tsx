import { Text, Badge, Group } from '@mantine/core';
import { Gavel, Calendar } from 'lucide-react';
import { IndiceCard } from './IndiceCard';
import type { TramiteLegislativo } from '../../types/smid';
import dayjs from 'dayjs';

const getBorderColor = (ic: number) => {
  if (ic >= 4.0) return 'border-l-teal-500';
  if (ic >= 2.5) return 'border-l-amber-400';
  return 'border-l-red-500';
};

const getIconBg = (ic: number) => {
  if (ic >= 4.0) return 'bg-teal-500';
  if (ic >= 2.5) return 'bg-amber-400';
  return 'bg-red-500';
};

export const TimelineTramite = ({ tramite }: { tramite: TramiteLegislativo }) => {
  const ic = tramite.indiceCompuesto;
  return (
    <div className="relative ml-12 mb-8">
      {/* Timeline dot */}
      <div className={`absolute -left-[42px] top-4 w-8 h-8 rounded-full ${getIconBg(ic)} text-white flex items-center justify-center shadow-sm z-10`}>
        <Gavel size={14} />
      </div>

      {/* Card */}
      <div className={`bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm border-l-[5px] ${getBorderColor(ic)} overflow-hidden`}>
        <div className="p-5">
          <div className="flex flex-col lg:flex-row gap-6">
            {/* Left: info */}
            <div className="flex-1 lg:border-r lg:pr-6">
              <Group justify="space-between" align="flex-start" mb="md">
                <Text className="text-lg font-extrabold text-gray-900">{tramite.nombreTramite}</Text>
                <Badge variant="light" color="gray" size="sm" leftSection={<Calendar size={10} />}>
                  {tramite.fechaTramite ? dayjs(tramite.fechaTramite).format('DD/MM/YYYY') : 'S/F'}
                </Badge>
              </Group>

              {tramite.analisisInteresSuperior && (
                <div className="mb-3">
                  <Text size="xs" c="dimmed" tt="uppercase" fw={700} mb={2}>Interés Superior</Text>
                  <Text size="sm" className="italic text-gray-600">{tramite.analisisInteresSuperior}</Text>
                </div>
              )}
              {tramite.analisisParticipacion && (
                <div>
                  <Text size="xs" c="dimmed" tt="uppercase" fw={700} mb={2}>Participación NNA</Text>
                  <Text size="sm" className="italic text-gray-600">{tramite.analisisParticipacion}</Text>
                </div>
              )}
            </div>

            {/* Right: indices */}
            <div className="flex flex-col items-center justify-center gap-3 min-w-[140px]">
              <IndiceCard valor={ic} label="Índice Compuesto" size="lg" />
              <Group gap="md" mt="xs">
                <div className="text-center px-3 py-1.5 bg-gray-50 rounded-md ring-1 ring-gray-200">
                  <Text size="sm" fw={700} className="text-simdPrimary">{tramite.indiceIrn}</Text>
                  <Text size="[10px]" c="dimmed" className="text-[10px]">IRN</Text>
                </div>
                <div className="text-center px-3 py-1.5 bg-gray-50 rounded-md ring-1 ring-gray-200">
                  <Text size="sm" fw={700} className="text-simdPrimary">{tramite.indiceIrri}</Text>
                  <Text size="[10px]" c="dimmed" className="text-[10px]">IRRI</Text>
                </div>
              </Group>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
