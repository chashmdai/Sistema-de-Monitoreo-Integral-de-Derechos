import { useNavigate } from 'react-router-dom';
import { Title, Text, Badge } from '@mantine/core';
import { FolderTree, Scale, ChartPie, ArrowUpRight } from 'lucide-react';

const modules = [
  {
    id: 'casos',
    title: 'Gestión de Casos',
    badge: 'Requerimientos',
    description: 'Ingreso de requerimientos, evaluación de garantes institucionales y seguimiento de oficios técnicos.',
    icon: <FolderTree size={26} strokeWidth={2.5} />,
    cardStyle: 'border-t-4 border-t-simdPrimary ring-1 ring-gray-900/5 shadow-sm hover:shadow-md hover:ring-simdPrimary/30',
    iconBg: 'bg-[#FDF2F8] text-simdPrimary',
    badgeColor: 'pink',
    path: '/smid/requerimientos',
  },
  {
    id: 'legislativo',
    title: 'Observatorio Legislativo',
    badge: 'Bitácora',
    description: 'Seguimiento de leyes, trámites constitucionales y cumplimiento de estándares internacionales.',
    icon: <Scale size={26} strokeWidth={2.5} />,
    cardStyle: 'border-t-4 border-t-simdAccent ring-1 ring-gray-900/5 shadow-sm hover:shadow-md hover:ring-simdAccent/30',
    iconBg: 'bg-[#FEF9D4] text-simdAccent',
    badgeColor: 'orange',
    path: '/smid/legislativo',
  },
  {
    id: 'reportes',
    title: 'Inteligencia Territorial',
    badge: 'Dashboard',
    description: 'Estadísticas regionales, mapas de calor, alertas críticas y benchmarking de garantes.',
    icon: <ChartPie size={26} strokeWidth={2.5} />,
    cardStyle: 'border-t-4 border-t-simdDanger ring-1 ring-gray-900/5 shadow-sm hover:shadow-md hover:ring-simdDanger/30',
    iconBg: 'bg-[#FCE1E7] text-simdDanger',
    badgeColor: 'red',
    path: '/smid/reportes',
  },
];

export const SmidHome = () => {
  const navigate = useNavigate();

  return (
    <div className="py-10 px-6 sm:px-10 max-w-7xl mx-auto">
      <div className="mb-10 pb-6 border-b border-gray-200/80">
        <Badge color="pink" variant="light" size="sm" radius="sm" className="mb-3 font-bold tracking-[0.15em] uppercase">
          Módulo SMID
        </Badge>
        <Title order={1} className="text-3xl text-gray-900 tracking-tight font-extrabold mb-1">
          Panel de Control
        </Title>
        <Text size="md" c="dimmed" className="font-medium">
          Sistema Integral de Monitoreo de Derechos — Jurisdicción O'Higgins y Maule
        </Text>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {modules.map((mod) => (
          <div
            key={mod.id}
            onClick={() => navigate(mod.path)}
            className={`group bg-white rounded-xl cursor-pointer transition-all duration-300 flex flex-col h-full relative overflow-hidden ${mod.cardStyle}`}
          >
            <div className="p-7 flex-1 flex flex-col">
              <div className="flex items-start justify-between mb-6">
                <div className={`w-14 h-14 rounded-xl flex items-center justify-center transition-transform duration-300 group-hover:scale-110 ${mod.iconBg}`}>
                  {mod.icon}
                </div>
                <Badge variant="light" color={mod.badgeColor} radius="sm" className="font-bold">
                  {mod.badge}
                </Badge>
              </div>
              <Title order={3} className="text-xl font-extrabold text-gray-900 mb-2.5">
                {mod.title}
              </Title>
              <Text c="dimmed" size="sm" className="leading-relaxed font-medium">
                {mod.description}
              </Text>
            </div>
            <div className="px-7 py-4 bg-gray-50/50 border-t border-gray-100 flex items-center justify-between group-hover:bg-gray-50 transition-colors">
              <Text size="sm" fw={700} className="text-gray-500 group-hover:text-gray-900 transition-colors">
                Acceder
              </Text>
              <div className="text-gray-400 group-hover:text-gray-900 transform group-hover:translate-x-0.5 group-hover:-translate-y-0.5 transition-all">
                <ArrowUpRight size={20} strokeWidth={2.5} />
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
