import { Text } from '@mantine/core';
import type { ReactNode } from 'react';

interface KpiCardProps {
  icon: ReactNode;
  value: number;
  label: string;
  iconBg: string;
  iconColor: string;
}

export const KpiCard = ({ icon, value, label, iconBg, iconColor }: KpiCardProps) => (
  <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-5 flex items-center gap-4">
    <div className={`w-14 h-14 rounded-full flex items-center justify-center ${iconBg} ${iconColor}`}>
      {icon}
    </div>
    <div>
      <Text className="text-2xl font-extrabold text-gray-900">{value}</Text>
      <Text size="xs" c="dimmed" tt="uppercase" fw={700} style={{ letterSpacing: 0.3 }}>
        {label}
      </Text>
    </div>
  </div>
);
