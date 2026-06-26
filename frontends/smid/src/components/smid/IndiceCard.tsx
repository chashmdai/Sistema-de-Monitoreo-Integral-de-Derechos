import { Text } from '@mantine/core';

interface IndiceCardProps {
  valor: number | string;
  label: string;
  size?: 'sm' | 'lg';
}

export const IndiceCard = ({ valor, label, size = 'lg' }: IndiceCardProps) => {
  const numVal = typeof valor === 'string' ? parseFloat(valor) : valor;
  const colorClass =
    numVal >= 3.5 ? 'text-teal-600' :
    numVal >= 2.0 ? 'text-amber-500' :
    'text-red-500';

  return (
    <div className="text-center">
      <div className={`${size === 'lg' ? 'text-4xl' : 'text-xl'} font-extrabold ${colorClass}`}>
        {typeof valor === 'number' ? valor.toFixed(2) : valor}
      </div>
      <Text size="xs" c="dimmed" tt="uppercase" fw={700} mt={2} style={{ letterSpacing: 0.5 }}>
        {label}
      </Text>
    </div>
  );
};
