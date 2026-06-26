import { Badge } from '@mantine/core';
import { AlertTriangle, ShieldCheck } from 'lucide-react';

export const AlertaCriticaBadge = ({ critica }: { critica: boolean }) => {
  if (critica) {
    return (
      <Badge color="red" variant="filled" size="lg" radius="sm" leftSection={<AlertTriangle size={14} />}>
        ALERTA CRÍTICA
      </Badge>
    );
  }
  return (
    <Badge color="teal" variant="light" size="lg" radius="sm" leftSection={<ShieldCheck size={14} />}>
      Estándar Normal
    </Badge>
  );
};
