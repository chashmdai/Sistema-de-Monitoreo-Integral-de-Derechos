import { Badge, Group } from '@mantine/core';
import { AlertCircle } from 'lucide-react';
import type { FactorRiesgo } from '../../types/smid';

export const PrioridadReforzadaBadge = ({ factores }: { factores: FactorRiesgo[] }) => {
  if (!factores || factores.length === 0) return null;
  return (
    <div className="flex flex-col items-end gap-1.5">
      <Badge
        color="grape"
        variant="light"
        size="lg"
        radius="sm"
        leftSection={<AlertCircle size={14} />}
        className="border border-purple-200"
      >
        PRIORIDAD REFORZADA
      </Badge>
      <Group gap={4} justify="flex-end">
        {factores.map((f) => (
          <Badge key={f.id} color="gray" variant="filled" size="xs">
            {f.nombre}
          </Badge>
        ))}
      </Group>
    </div>
  );
};
