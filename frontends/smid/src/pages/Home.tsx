import { useContext } from "react";
import {
  ShieldHalf,
  ShieldAlert,
  TableProperties,
  ArrowRight,
  Map,
  CheckCircle2,
  Calendar,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import { Title, Text, Badge } from "@mantine/core";
import { AuthContext } from "../context/AuthContext";

export const Home = () => {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);

  const firstName = user?.fullName?.split(" ")[0] || "Funcionario";

  // Obtenemos la fecha actual para darle contexto al dashboard
  const today = new Date().toLocaleDateString("es-CL", {
    weekday: "long",
    day: "numeric",
    month: "long",
  });

  // ==========================================================================
  // Cada módulo declara sus clases de acento como STRINGS LITERALES COMPLETOS.
  // Esto es deliberado: el JIT de Tailwind no puede detectar clases construidas
  // dinámicamente (p.ej. `group-hover:${mod.themeBg}`), por lo que el código
  // original no generaba esos estilos. Todo acento se ancla a tokens simd*.
  // ==========================================================================
  const modules = [
    {
      id: "smid",
      title: "SMID",
      badge: "Principal",
      description:
        "Gestión de casos, observatorio legislativo e inteligencia de datos en tiempo real.",
      icon: <ShieldHalf size={28} strokeWidth={2} />,
      iconWrap: "bg-simdPrimary/10 text-simdPrimary",
      accentBar: "bg-simdPrimary",
      arrowHover: "group-hover:bg-simdPrimary group-hover:text-white",
      path: "/smid",
      isStatic: false,
    },
    {
      id: "esnna",
      title: "ESNNA",
      badge: "Alerta",
      description:
        "Análisis y consolidación documental para casos de Explotación Sexual (NNA).",
      icon: <ShieldAlert size={28} strokeWidth={2} />,
      iconWrap: "bg-simdDanger/10 text-simdDanger",
      accentBar: "bg-simdDanger",
      arrowHover: "group-hover:bg-simdDanger group-hover:text-white",
      path: "/esnna",
      isStatic: false,
    },
    {
      id: "sgs",
      title: "SGS",
      badge: "Matriz",
      description:
        "Matriz de Seguimiento. Extracción y consolidación automática de oficios institucionales.",
      icon: <TableProperties size={28} strokeWidth={2} />,
      iconWrap: "bg-simdAccent/15 text-simdAccent",
      accentBar: "bg-simdAccent",
      arrowHover: "group-hover:bg-simdAccent group-hover:text-white",
      path: "/sgs",
      isStatic: false,
    },
    {
      id: "geoespacial",
      title: "Tablero Geoespacial",
      badge: "Prototipo",
      description:
        "Visualización interactiva de datos territoriales, mapas de calor y distribución.",
      icon: <Map size={28} strokeWidth={2} />,
      iconWrap: "bg-simdInfo/10 text-simdInfo",
      accentBar: "bg-simdInfo",
      arrowHover: "group-hover:bg-simdInfo group-hover:text-white",
      path: "/Tablero-Geoespacial.html",
      isStatic: true,
    },
  ];

  const handleNavigation = (mod) => {
    if (mod.isStatic) {
      window.open(mod.path, "_blank", "noopener,noreferrer");
    } else {
      navigate(mod.path);
    }
  };

  return (
    <div className="relative isolate min-h-screen py-12 px-6 sm:px-10 font-sans selection:bg-simdPrimary/20">
      {/* Capa de luz ambiental institucional */}
      <div aria-hidden className="aurora aurora--home" />

      <div className="max-w-7xl mx-auto">
        {/* === CABECERA INSTITUCIONAL === */}
        <header className="flex flex-col md:flex-row md:items-end justify-between gap-6 mb-12">
          <div>
            <Text className="text-simdPrimary font-semibold tracking-widest uppercase text-xs mb-3">
              Defensoría de la Niñez
            </Text>
            <Title
              order={1}
              className="text-4xl sm:text-5xl text-gray-900 tracking-tight font-black mb-3"
            >
              Hola, {firstName}.
            </Title>
            {/* Acento de marca: gradiente institucional sutil */}
            <div className="h-1 w-16 rounded-full bg-gradient-to-r from-simdPrimary to-simdAccent mb-4" />
            <Text size="lg" className="text-simdTextMuted font-medium max-w-xl">
              ¿Qué área vamos a gestionar hoy?
            </Text>
          </div>

          {/* Widgets de contexto (Fecha y Estado) en formato "Píldora" de cristal */}
          <div className="flex flex-col sm:flex-row gap-3">
            <div className="glass-panel flex items-center gap-2 px-4 py-2.5 rounded-full">
              <Calendar size={16} className="text-simdTextMuted" />
              <span className="text-sm font-medium text-simdTextMain capitalize">
                {today}
              </span>
            </div>
            <div className="glass-panel flex items-center gap-2 px-4 py-2.5 rounded-full">
              <CheckCircle2 size={16} className="text-simdSuccess" />
              <span className="text-sm font-medium text-simdTextMain">
                Sistemas operativos
              </span>
            </div>
          </div>
        </header>

        {/* === GRILLA DE MÓDULOS (tarjetas de cristal) === */}
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
          {modules.map((mod) => (
            <div
              key={mod.id}
              onClick={() => handleNavigation(mod)}
              className="group glass-card relative rounded-card overflow-hidden p-8 pt-9 flex flex-col h-full cursor-pointer transition-all duration-300 hover:shadow-glass-hover hover:-translate-y-1.5"
            >
              {/* Barra de acento institucional superior */}
              <div
                className={`absolute inset-x-0 top-0 h-1 ${mod.accentBar} opacity-80`}
              />

              {/* Parte Superior: Icono y Badge */}
              <div className="flex items-start justify-between mb-8">
                <div
                  className={`w-16 h-16 rounded-2xl flex items-center justify-center transition-transform duration-300 group-hover:scale-105 ${mod.iconWrap}`}
                >
                  {mod.icon}
                </div>
                <Badge
                  color="gray"
                  variant="light"
                  size="sm"
                  className="font-bold text-simdTextMuted"
                >
                  {mod.badge}
                </Badge>
              </div>

              {/* Contenido Central */}
              <div className="flex-1">
                <Title
                  order={3}
                  className="text-2xl font-bold text-gray-900 mb-3 transition-colors group-hover:text-black"
                >
                  {mod.title}
                </Title>
                <Text
                  size="sm"
                  className="leading-relaxed text-simdTextMuted font-medium mb-8"
                >
                  {mod.description}
                </Text>
              </div>

              {/* Botón de Acción Integrado */}
              <div className="mt-auto flex items-center gap-3">
                <div
                  className={`w-10 h-10 rounded-full flex items-center justify-center bg-white/60 text-simdTextMuted transition-all duration-300 ${mod.arrowHover}`}
                >
                  <ArrowRight
                    size={18}
                    strokeWidth={2.5}
                    className="transition-transform duration-300 group-hover:translate-x-1"
                  />
                </div>
                <span className="text-sm font-bold text-simdTextMuted transition-colors group-hover:text-gray-900">
                  {mod.isStatic ? "Ver prototipo" : "Acceder"}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
