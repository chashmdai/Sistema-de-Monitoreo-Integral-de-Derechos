import { useState } from 'react';
import { Title, Text, Badge, Button, Select, Group, Stack } from '@mantine/core';
import { Radiation, Mail, Building2, Zap } from 'lucide-react';
import { notifications } from '@mantine/notifications';
import { useDashboard } from '../../hooks/useSmid';
import { fetchComparacion } from '../../api/smid';
import { KpiCard } from '../../components/smid/KpiCard';
import { MatrizCalor } from '../../components/smid/MatrizCalor';
import { IndiceCard } from '../../components/smid/IndiceCard';
import type { ComparacionDTO } from '../../types/smid';

export const ReportesDashboardPage = () => {
  const { data, isLoading } = useDashboard();
  const [g1, setG1] = useState<string | null>(null);
  const [g2, setG2] = useState<string | null>(null);
  const [comparacion, setComparacion] = useState<ComparacionDTO | null>(null);
  const [isComparing, setIsComparing] = useState(false);

  const handleComparar = async () => {
    if (!g1 || !g2) {
      notifications.show({ title: 'Selección requerida', message: 'Elija dos instituciones.', color: 'red' });
      return;
    }
    setIsComparing(true);
    try {
      const result = await fetchComparacion(Number(g1), Number(g2));
      setComparacion(result);
    } catch {
      notifications.show({ title: 'Error', message: 'No se pudo generar la comparación.', color: 'red' });
    } finally {
      setIsComparing(false);
    }
  };

  if (isLoading) return <div className="p-10 text-center"><Text c="dimmed">Cargando dashboard...</Text></div>;
  if (!data) return null;

  const instOptions = (data.instituciones || []).map((i) => ({ value: String(i.id), label: i.nombre }));

  const getScoreColor = (score: number) => {
    if (score < 2.0) return 'text-red-500';
    if (score < 3.5) return 'text-amber-500';
    return 'text-teal-600';
  };

  return (
    <div className="p-6 sm:p-10 max-w-[1600px] mx-auto flex flex-col gap-6">
      <div>
        <Group gap="xs" mb={5}>
          <Badge color="red" variant="light" radius="sm">Módulo SMID</Badge>
          <Text size="xs" c="dimmed" fw={600}>Inteligencia Territorial</Text>
        </Group>
        <Title order={1} className="font-extrabold tracking-tight">Dashboard Estratégico</Title>
      </div>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <KpiCard icon={<Radiation size={24} />} value={data.alertasCriticas} label="Alertas Críticas" iconBg="bg-red-50" iconColor="text-red-500" />
        <KpiCard icon={<Mail size={24} />} value={data.oficiosPendientes} label="Oficios Pendientes" iconBg="bg-amber-50" iconColor="text-amber-500" />
        <KpiCard icon={<Building2 size={24} />} value={data.totalInstituciones} label="Garantes Activos" iconBg="bg-blue-50" iconColor="text-blue-500" />
      </div>

      {/* Charts row */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Bar chart - ranking */}
        <div className="lg:col-span-2 bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
          <Text fw={700} className="text-simdPrimary mb-4">Ranking de Alineación por Institución</Text>
          {data.stats.labelsInstituciones.length > 0 ? (
            <div className="flex items-end gap-4 h-[220px]">
              {data.stats.labelsInstituciones.map((label, i) => {
                const val = data.stats.dataDesempeno[i];
                const pct = (val / 4) * 100;
                const color = val < 2.0 ? 'bg-simdDanger' : val < 3.5 ? 'bg-simdWarning' : 'bg-simdSuccess';
                return (
                  <div key={label} className="flex flex-col items-center flex-1 gap-1">
                    <Text size="xs" fw={700}>{val.toFixed(2)}</Text>
                    <div className={`w-full rounded-t-md ${color} transition-all`} style={{ height: `${Math.max(pct, 5)}%` }} />
                    <Text size="[8px]" c="dimmed" className="text-[8px] text-center truncate w-full">{label}</Text>
                  </div>
                );
              })}
            </div>
          ) : (
            <Text c="dimmed" ta="center" py="xl">Sin datos.</Text>
          )}
        </div>

        {/* Donut - regiones */}
        <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
          <Text fw={700} className="text-simdPrimary mb-4">Distribución de Casos</Text>
          {data.stats.labelsRegiones.length > 0 ? (
            <Stack gap="sm">
              {data.stats.labelsRegiones.map((region, i) => {
                const count = data.stats.dataRegiones[i];
                const total = data.stats.dataRegiones.reduce((a, b) => a + b, 0);
                const pct = total > 0 ? ((count / total) * 100).toFixed(0) : '0';
                const colors = ['bg-simdPrimary', 'bg-simdAccent', 'bg-simdSuccess', 'bg-simdWarning', 'bg-simdDanger'];
                return (
                  <div key={region}>
                    <Group justify="space-between" mb={4}>
                      <Text size="sm" fw={500}>{region}</Text>
                      <Text size="sm" fw={700}>{count} ({pct}%)</Text>
                    </Group>
                    <div className="w-full bg-gray-100 rounded-full h-2.5">
                      <div className={`h-2.5 rounded-full ${colors[i % colors.length]}`} style={{ width: `${pct}%` }} />
                    </div>
                  </div>
                );
              })}
            </Stack>
          ) : (
            <Text c="dimmed" ta="center" py="xl">Sin datos.</Text>
          )}
        </div>
      </div>

      {/* Matriz de calor */}
      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
        <Text fw={700} className="text-simdPrimary mb-4">Mapa de Calor: Institución vs Derecho Vulnerado</Text>
        <MatrizCalor matriz={data.matrizCalor} derechos={data.columnasDerechos} />
      </div>

      {/* Benchmarking */}
      <div className="bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm border-t-4 border-t-simdPrimary p-6">
        <Title order={4} className="font-bold text-simdPrimary mb-4">Benchmarking de Garantes</Title>
        <div className="flex flex-col md:flex-row items-center gap-4">
          <Select className="flex-1" placeholder="Seleccione Garante A (Base)..." data={instOptions} value={g1} onChange={setG1} />
          <Button color="pink" leftSection={<Zap size={14} />} onClick={handleComparar} loading={isComparing}>COMPARAR</Button>
          <Select className="flex-1" placeholder="Seleccione Garante B (Objetivo)..." data={instOptions} value={g2} onChange={setG2} />
        </div>

        {comparacion && (
          <div className="mt-6 bg-gray-50 rounded-xl overflow-hidden ring-1 ring-gray-200">
            <div className="bg-gray-800 text-white px-6 py-3 flex justify-between items-center">
              <Text size="sm" fw={700} tt="uppercase">Resultado del Análisis Comparativo</Text>
            </div>
            <div className="grid grid-cols-5">
              <div className="col-span-2 p-6 text-center border-r">
                <Badge color="pink" variant="light" size="sm" mb="xs">Base</Badge>
                <Title order={4} className="text-simdPrimary mb-3">{comparacion.nombreGarante1}</Title>
                <div className={`text-5xl font-extrabold ${getScoreColor(comparacion.scoreGarante1)}`}>{comparacion.scoreGarante1.toFixed(2)}</div>
                <Text size="xs" c="dimmed" fw={700} tt="uppercase" mt={4}>Nivel de cumplimiento</Text>
              </div>
              <div className="flex items-center justify-center bg-gray-100">
                <div className="w-12 h-12 rounded-full bg-white shadow flex items-center justify-center font-bold text-simdPrimary border">VS</div>
              </div>
              <div className="col-span-2 p-6 text-center">
                <Badge color="gray" variant="light" size="sm" mb="xs">Objetivo</Badge>
                <Title order={4} className="text-simdPrimary mb-3">{comparacion.nombreGarante2}</Title>
                <div className={`text-5xl font-extrabold ${getScoreColor(comparacion.scoreGarante2)}`}>{comparacion.scoreGarante2.toFixed(2)}</div>
                <Text size="xs" c="dimmed" fw={700} tt="uppercase" mt={4}>Nivel de cumplimiento</Text>
              </div>
            </div>
            <div className="bg-gray-100 px-6 py-3 text-center border-t">
              <Text size="sm" fw={600} c="dimmed">{comparacion.infoComparacion}</Text>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
