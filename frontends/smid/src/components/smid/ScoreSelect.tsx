import { Select } from '@mantine/core';

interface ScoreSelectProps {
  label: string;
  value: number;
  onChange: (val: number) => void;
  min?: number;
  max?: number;
  labels?: Record<number, string>;
}

const DEFAULT_LABELS_04: Record<number, string> = {
  0: '0 - Inexistente',
  1: '1 - Formal',
  2: '2 - Parcial',
  3: '3 - Sustantivo',
  4: '4 - Óptimo',
};

const DEFAULT_LABELS_02: Record<number, string> = {
  0: '0 - Sin daño',
  1: '1 - Leve/Moderado',
  2: '2 - Grave',
};

export const ScoreSelect = ({ label, value, onChange, min = 0, max = 4, labels }: ScoreSelectProps) => {
  const defaultLabels = max === 2 ? DEFAULT_LABELS_02 : max === 4 ? DEFAULT_LABELS_04 : undefined;
  const resolvedLabels = labels || defaultLabels;

  const data = Array.from({ length: max - min + 1 }, (_, i) => {
    const v = min + i;
    return { value: String(v), label: resolvedLabels?.[v] || String(v) };
  });

  return (
    <Select
      label={label}
      data={data}
      value={String(value)}
      onChange={(val) => onChange(Number(val))}
      allowDeselect={false}
    />
  );
};
