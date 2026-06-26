import { useParams, useNavigate } from 'react-router-dom';
import { Title, Text, Badge, Button, Group } from '@mantine/core';
import { ArrowLeft, PlusCircle, Gavel } from 'lucide-react';
import { useLeyDetail } from '../../hooks/useSmid';
import { TimelineTramite } from '../../components/smid/TimelineTramite';

export const LeyDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const leyId = Number(id);
  const { data, isLoading } = useLeyDetail(leyId);

  if (isLoading) return <div className="p-10 text-center"><Text c="dimmed">Cargando bitácora...</Text></div>;
  if (!data) return <div className="p-10 text-center"><Text c="red">Ley no encontrada.</Text></div>;

  const { ley, tramites } = data;

  return (
    <div className="p-6 sm:p-10 max-w-[1200px] mx-auto flex flex-col gap-6">
      <button onClick={() => navigate('/smid/legislativo')} className="text-gray-400 hover:text-gray-600 text-sm flex items-center gap-1 w-fit">
        <ArrowLeft size={14} /> Volver al Observatorio
      </button>

      {/* Header card */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="bg-simdPrimary text-white rounded-xl p-6 shadow-sm relative overflow-hidden">
          <Gavel size={120} className="absolute -right-4 -bottom-4 opacity-10" />
          <Title order={3} className="font-extrabold mb-3 relative z-10">{ley.nombreLey}</Title>
          <Text size="sm" className="opacity-75 mb-1 relative z-10">Boletín: {ley.numeroBoletin}</Text>
          <Text size="sm" className="opacity-75 mb-4 relative z-10">Inicio: {ley.fechaInicio || 'Sin fecha'}</Text>
          <Button color="white" c="dark" variant="white" fullWidth className="relative z-10" leftSection={<PlusCircle size={16} />} onClick={() => navigate(`/smid/legislativo/tramite/nuevo/${ley.id}`)}>
            Registrar Nuevo Trámite
          </Button>
        </div>

        <div className="lg:col-span-2 bg-white rounded-xl ring-1 ring-gray-900/5 shadow-sm p-6">
          <Text fw={700} className="text-simdPrimary mb-3">Evolución de Estándares</Text>
          {tramites.length > 0 ? (
            <div className="flex items-end gap-3 h-[160px]">
              {tramites.map((t) => {
                const pct = ((t.indiceCompuesto - 1) / 4) * 100;
                const color = t.indiceCompuesto >= 4 ? 'bg-teal-500' : t.indiceCompuesto >= 2.5 ? 'bg-amber-400' : 'bg-red-400';
                return (
                  <div key={t.id} className="flex flex-col items-center flex-1 gap-1">
                    <Text size="xs" fw={700} className={t.indiceCompuesto >= 4 ? 'text-teal-600' : t.indiceCompuesto >= 2.5 ? 'text-amber-600' : 'text-red-600'}>
                      {t.indiceCompuesto}
                    </Text>
                    <div className={`w-full rounded-t-md ${color} transition-all`} style={{ height: `${Math.max(pct, 5)}%` }} />
                    <Text size="[9px]" c="dimmed" className="text-[9px] text-center truncate w-full">{t.nombreTramite}</Text>
                  </div>
                );
              })}
            </div>
          ) : (
            <Text c="dimmed" ta="center" py="xl">Sin datos para graficar.</Text>
          )}
        </div>
      </div>

      {/* Timeline */}
      <Title order={4} className="font-bold text-simdPrimary border-b pb-2">Historia de la Ley</Title>

      {tramites.length > 0 ? (
        <div className="relative pl-6">
          {/* Vertical line */}
          <div className="absolute left-[50px] top-0 bottom-0 w-1 bg-gray-200 rounded" />
          {tramites.map((t) => (
            <TimelineTramite key={t.id} tramite={t} />
          ))}
        </div>
      ) : (
        <div className="bg-white rounded-xl p-10 text-center shadow-sm">
          <Text c="dimmed">Aún no se han registrado hitos en la bitácora.</Text>
        </div>
      )}
    </div>
  );
};
