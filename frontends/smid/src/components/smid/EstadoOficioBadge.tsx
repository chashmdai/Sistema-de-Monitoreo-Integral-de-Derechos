import { Badge } from '@mantine/core';
import type { EstadoOficio } from '../../types/smid';

const CONFIG: Record<EstadoOficio, { color: string; label: string }> = {
  PENDIENTE: { color: 'gray', label: 'Pendiente' },
  EN_PLAZO: { color: 'teal', label: 'En Plazo' },
  POR_VENCER: { color: 'yellow', label: 'Por Vencer' },
  VENCIDO: { color: 'red', label: 'VENCIDO' },
  RESPONDIDO: { color: 'blue', label: 'Respondido' },
  CERRADO: { color: 'gray', label: 'Cerrado' },
};

export const EstadoOficioBadge = ({ estado }: { estado: EstadoOficio }) => {
  const cfg = CONFIG[estado] || CONFIG.PENDIENTE;
  return (
    <Badge color={cfg.color} variant="light" size="sm" radius="sm">
      {cfg.label}
    </Badge>
  );
};
