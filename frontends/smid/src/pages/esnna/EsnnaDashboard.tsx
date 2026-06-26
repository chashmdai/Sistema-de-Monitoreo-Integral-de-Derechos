/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect, useCallback } from "react";
import {
  Title,
  Text,
  Button,
  Badge,
  Table,
  Group,
  ActionIcon,
  Menu,
  Drawer,
  ScrollArea,
  TextInput,
  Textarea,
  Grid,
  Modal,
  Select,
  Stack,
  Alert,
  Paper,
  Pagination,
  Tooltip,
  Divider,
  Loader,
  Center,
  Box,
  TagsInput,
  Tabs,
} from "@mantine/core";
import { Dropzone, PDF_MIME_TYPE } from "@mantine/dropzone";
import { notifications } from "@mantine/notifications";
import {
  FileUp,
  Search,
  MoreVertical,
  Eye,
  FileSpreadsheet,
  Check,
  Info,
  AlertTriangle,
  ShieldAlert,
  Activity,
  FileText,
  Ban,
  GitBranch,
  BarChart3,
  Trash2,
  Plus,
  RefreshCw,
  User,
  Calendar,
  Hash,
  FileSearch,
  Settings,
} from "lucide-react";
import { api } from "../../api/axiosConfig";

// ============================================================
// TIPOS (contrato backend v2)
// ============================================================
export type Semaforo = "ROJO" | "AMARILLO" | "VERDE";
export type SiNo = "SÍ" | "NO";
export type Sexo = "M" | "F" | "X";
export type EstadoGestion =
  | "PENDIENTE_REVISION"
  | "EN_REVISION"
  | "EN_QUERELLA"
  | "DERIVADO"
  | "CERRADO"
  | "ANULADO";

export interface Imputado {
  orden?: number | null;
  nombre?: string | null;
  rut?: string | null;
  domicilio?: string | null;
  sexo?: string | null;
  esFuncionarioPublico?: SiNo | null;
}

interface Criterio {
  cumple: boolean;
  evidencia?: string | null;
  subtipos?: string[];
}
interface Criterios {
  c1PluralidadNna: Criterio;
  c2Interseccionalidad: Criterio;
  c3AgenteCualificado: Criterio;
  c4PluralidadVictimarios: Criterio;
  c5CausaBasal: Criterio;
  exclusionAplicable: boolean;
  cualExclusion?: string | null;
  improcedenciaPmaNad: boolean;
  detalleImprocedencia?: string | null;
}

interface FormCaso {
  paraQuerella?: string;
  requerimiento?: string;
  nroCorrelativo?: string;
  fecha?: string;
  nroOficio?: string;
  carpeta?: string;
  region?: string;
  tipoPrograma?: string;
  nombreProgramaResidencia?: string;
  delitoConcreto?: string;
  nnaBajoCuidadoEstado?: SiNo;
  residencia?: string;
  denunciante?: string;
  contactoDenunciante?: string;
  nna?: string;
  sexoNna?: Sexo;
  cedulaNna?: string;
  nacionalidadNna?: string;
  fechaNacimiento?: string;
  edad?: string;
  consumoDrogasAlcohol?: string;
  curador?: string;
  nadPma?: string;
  contactoNadPma?: string;
  imputadoConocido?: SiNo;
  imputados: Imputado[];
  lugarOcurrenciaHechos?: string;
  comunasInvolucradas?: string;
  hechos?: string;
  tipoViolencia?: string;
  redesSocialesMencionadas: string[];
  identificacionLocalesBaresHoteles?: string;
  presuntaRedExplotacion?: SiNo;
  observacion?: string;
  querella?: string;
  denunciasAnteriores?: string;
  rucAsociados?: string;
  gestiones?: string;
  descripcion?: string;
  pendiente?: string;
}

interface Consolidado extends FormCaso {
  criterios: Criterios;
  confianzaAnalisis: number;
}
interface DocumentoOmitido {
  nombreArchivo: string;
  fase: string;
  razon: string;
}
interface Usage {
  tokensPrompt: number;
  tokensCompletion: number;
  tokensReasoning: number;
  costoEstimado: number;
}
interface ProcesoResultado {
  draftId: string;
  semaforoIa: Semaforo;
  justificacionIa: string;
  consolidado: Consolidado;
  estadoCalidad: "COMPLETA" | "PARCIAL";
  documentosTotales: number;
  documentosProcesados: number;
  documentosOmitidos: DocumentoOmitido[];
  usage: Usage;
}
interface DetalleCaso extends FormCaso {
  id: number;
  version: number;
  semaforoIa: Semaforo;
  justificacionIa: string;
  confianzaIa: number;
  semaforoFinal: Semaforo;
  semaforoFinalAutor?: string;
  semaforoFinalFecha?: string;
  estadoGestion: EstadoGestion;
  creadoPor?: string;
  fechaIngreso?: string;
  fechaActualizacion?: string;
  anulado: boolean;
  motivoAnulacion?: string;
}
interface ResumenCaso {
  id: number;
  nroOficio?: string;
  semaforoFinal?: Semaforo;
  semaforoIa?: Semaforo;
  region?: string;
  delitoConcreto?: string;
  edad?: number;
  sexoNna?: Sexo;
  estadoGestion: EstadoGestion;
  fechaIngreso?: string;
  fechaActualizacion?: string;
}
interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
interface Metricas {
  totalCasos: number;
  overrides: number;
  tasaOverride: number;
  matrizConfusion: Record<string, number>;
  subestimacionIa: number;
  sobreestimacionIa: number;
}

// ============================================================
// CONSTANTES Y HELPERS
// ============================================================
const DELITOS = [
  "sustracción de menores (Art. 142)",
  "violación propia (Art. 361)",
  "violación impropia (Art. 362)",
  "estupro (Art. 363)",
  "abuso sexual (Art. 365 bis-366 ter)",
  "acciones de significación sexual ante menor (Art. 366 quáter)",
  "explotación sexual comercial (Art. 367 / 367 ter)",
  "producción/almacenamiento/difusión CSAM (Art. 367 quáter)",
  "transmisión imágenes/sonidos acciones sexuales (Art. 367 septies)",
  "parricidio (Art. 390)",
  "femicidio (Art. 390 bis-390 quáter)",
  "homicidio (Art. 391)",
  "infanticidio (Art. 394)",
  "lesiones graves gravísimas (Art. 397 inc.1)",
];

const ESTADOS: EstadoGestion[] = [
  "PENDIENTE_REVISION",
  "EN_REVISION",
  "EN_QUERELLA",
  "DERIVADO",
  "CERRADO",
  "ANULADO",
];

const TRANSICIONES: Record<EstadoGestion, EstadoGestion[]> = {
  PENDIENTE_REVISION: ["EN_REVISION", "ANULADO"],
  EN_REVISION: ["EN_QUERELLA", "DERIVADO", "CERRADO", "ANULADO"],
  EN_QUERELLA: ["DERIVADO", "CERRADO", "ANULADO"],
  DERIVADO: ["CERRADO", "ANULADO"],
  CERRADO: ["EN_REVISION", "ANULADO"],
  ANULADO: [],
};

const semColor = (s?: Semaforo) =>
  s === "ROJO"
    ? "red"
    : s === "AMARILLO"
      ? "yellow"
      : s === "VERDE"
        ? "teal"
        : "gray";

const estadoColor = (e?: EstadoGestion) => {
  switch (e) {
    case "PENDIENTE_REVISION":
      return "gray";
    case "EN_REVISION":
      return "blue";
    case "EN_QUERELLA":
      return "grape";
    case "DERIVADO":
      return "cyan";
    case "CERRADO":
      return "green";
    case "ANULADO":
      return "dark";
    default:
      return "gray";
  }
};

const fmt = (d?: string) => {
  if (!d) return "—";
  const date = new Date(d);
  return isNaN(date.getTime())
    ? d
    : date.toLocaleDateString("es-CL", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
};

const apiError = (e: any, fallback: string) =>
  e?.response?.data?.message || e?.message || fallback;

const SINO = ["SÍ", "NO"];

const emptyForm = (): FormCaso => ({
  imputados: [],
  redesSocialesMencionadas: [],
});

const detalleToForm = (d: DetalleCaso): FormCaso => ({
  ...d,
  edad: d.edad != null ? String(d.edad) : undefined,
  imputados: d.imputados ?? [],
  redesSocialesMencionadas: d.redesSocialesMencionadas ?? [],
});

const REGIONES_SELECT = [
  "Región de Arica y Parinacota",
  "Región de Tarapacá",
  "Región de Antofagasta",
  "Región de Atacama",
  "Región de Coquimbo",
  "Región de Valparaíso",
  "Región Metropolitana de Santiago",
  "Región Metropolitana",
  "Región del Libertador General Bernardo O'Higgins",
  "Región del Maule",
  "Región de Ñuble",
  "Región del Biobío",
  "Región de La Araucanía",
  "Región de Los Ríos",
  "Región de Los Lagos",
  "Región de Aysén del General Carlos Ibáñez del Campo",
  "Región de Magallanes y de la Antártica Chilena",
];

const getSemaforoGradient = (s?: Semaforo): string | undefined => {
  if (!s) return undefined;
  const colorMap: Record<Semaforo, string> = {
    ROJO: "rgba(239, 68, 68, 0.15)", // Rojo suave
    AMARILLO: "rgba(234, 179, 8, 0.15)", // Amarillo suave
    VERDE: "rgba(20, 184, 166, 0.15)", // Teal/Verde suave
  };
  const color = colorMap[s];
  return `linear-gradient(to right, ${color} 0%, ${color} 15%, transparent 50%)`;
};

// ============================================================
// SUB-COMPONENTES ESTILADOS
// ============================================================
const FieldRow = ({
  label,
  value,
}: {
  label: string;
  value?: string | null;
}) => (
  <div>
    <Text
      size="xs"
      c="dimmed"
      tt="uppercase"
      fw={700}
      mb={2}
      style={{ letterSpacing: 0.4 }}
    >
      {label}
    </Text>
    <Text size="sm" fw={500}>
      {value && value.toString().trim() ? (
        value
      ) : (
        <Text span c="dimmed" fs="italic" size="sm">
          Sin información
        </Text>
      )}
    </Text>
  </div>
);

const SemaforoBadge = ({ s, size = "md" }: { s?: Semaforo; size?: any }) => (
  <Badge color={semColor(s)} variant="filled" size={size} radius="sm">
    {s ?? "—"}
  </Badge>
);

const CriterioRow = ({ label, c }: { label: string; c?: Criterio }) => (
  <Group justify="space-between" wrap="nowrap" align="flex-start" gap="xs">
    <Group gap={6} wrap="nowrap" style={{ flex: 1 }}>
      {c?.cumple ? (
        <Check size={16} className="text-red-600" />
      ) : (
        <Ban size={16} className="text-gray-400" />
      )}
      <div>
        <Text
          size="sm"
          fw={c?.cumple ? 600 : 400}
          c={c?.cumple ? undefined : "dimmed"}
        >
          {label}
        </Text>
        {c?.cumple && c?.evidencia && (
          <Text size="xs" c="dimmed">
            {c.evidencia}
          </Text>
        )}
        {c?.subtipos && c.subtipos.length > 0 && (
          <Group gap={4} mt={2}>
            {c.subtipos.map((st) => (
              <Badge key={st} size="xs" variant="light" color="red">
                {st}
              </Badge>
            ))}
          </Group>
        )}
      </div>
    </Group>
  </Group>
);

const CriteriosPanel = ({ cr }: { cr: Criterios }) => {
  const n = [
    cr.c1PluralidadNna,
    cr.c2Interseccionalidad,
    cr.c3AgenteCualificado,
    cr.c4PluralidadVictimarios,
    cr.c5CausaBasal,
  ].filter((c) => c?.cumple).length;
  return (
    <Stack gap="xs">
      <Group justify="space-between">
        <Text fw={600} size="sm">
          Criterios PR-PDR-05 ({n}/5 verificados)
        </Text>
      </Group>
      <CriterioRow
        label="C1 · Pluralidad de NNA víctimas"
        c={cr.c1PluralidadNna}
      />
      <CriterioRow
        label="C2 · Interseccionalidad"
        c={cr.c2Interseccionalidad}
      />
      <CriterioRow
        label="C3 · Agente vulnerador cualificado"
        c={cr.c3AgenteCualificado}
      />
      <CriterioRow
        label="C4 · Pluralidad de victimarios / red"
        c={cr.c4PluralidadVictimarios}
      />
      <CriterioRow label="C5 · Causa basal estructural" c={cr.c5CausaBasal} />
      {cr.exclusionAplicable && (
        <Alert color="teal" icon={<Info size={16} />} p="xs">
          Exclusión: {cr.cualExclusion ?? "no especificada"}
        </Alert>
      )}
      {cr.improcedenciaPmaNad && (
        <Alert color="teal" icon={<Info size={16} />} p="xs">
          Improcedencia PMA/NAD:{" "}
          {cr.detalleImprocedencia ?? "PMA/NAD interviene"}
        </Alert>
      )}
    </Stack>
  );
};

const ImputadosEditor = ({
  value,
  onChange,
}: {
  value: Imputado[];
  onChange: (v: Imputado[]) => void;
}) => {
  const upd = (i: number, k: keyof Imputado, v: any) => {
    const next = [...value];
    next[i] = { ...next[i], [k]: v };
    onChange(next);
  };
  return (
    <Stack gap="sm">
      {value.length === 0 && (
        <Text size="sm" c="dimmed">
          Sin imputados registrados.
        </Text>
      )}
      {value.map((im, i) => (
        <Paper key={i} withBorder p="sm" radius="md" className="bg-gray-50/50">
          <Grid gutter="xs">
            <Grid.Col span={5}>
              <TextInput
                label="Nombre"
                size="xs"
                value={im.nombre ?? ""}
                onChange={(e) => upd(i, "nombre", e.target.value)}
              />
            </Grid.Col>
            <Grid.Col span={4}>
              <TextInput
                label="RUT"
                size="xs"
                value={im.rut ?? ""}
                onChange={(e) => upd(i, "rut", e.target.value)}
              />
            </Grid.Col>
            <Grid.Col span={3}>
              <Select
                label="Func. público"
                size="xs"
                data={SINO}
                value={im.esFuncionarioPublico ?? null}
                onChange={(v) => upd(i, "esFuncionarioPublico", v)}
                clearable
              />
            </Grid.Col>
            <Grid.Col span={8}>
              <TextInput
                label="Domicilio"
                size="xs"
                value={im.domicilio ?? ""}
                onChange={(e) => upd(i, "domicilio", e.target.value)}
              />
            </Grid.Col>
            <Grid.Col span={3}>
              <TextInput
                label="Sexo"
                size="xs"
                value={im.sexo ?? ""}
                onChange={(e) => upd(i, "sexo", e.target.value)}
              />
            </Grid.Col>
            <Grid.Col
              span={1}
              style={{ display: "flex", alignItems: "flex-end" }}
            >
              <ActionIcon
                color="red"
                variant="subtle"
                onClick={() => onChange(value.filter((_, j) => j !== i))}
              >
                <Trash2 size={16} />
              </ActionIcon>
            </Grid.Col>
          </Grid>
        </Paper>
      ))}
      <Button
        variant="light"
        size="xs"
        leftSection={<Plus size={14} />}
        onClick={() => onChange([...value, { nombre: "", rut: "" }])}
      >
        Agregar imputado
      </Button>
    </Stack>
  );
};

// Formulario de campos organizado por pestañas fieles al look de SGS
const FichaTabsForm = ({
  form,
  set,
}: {
  form: FormCaso;
  set: (k: keyof FormCaso, v: any) => void;
}) => (
  <Tabs defaultValue="nna" color="simdPrimary">
    <Tabs.List grow className="mb-4">
      <Tabs.Tab value="nna" leftSection={<User size={14} />}>
        NNA y Delito
      </Tabs.Tab>
      <Tabs.Tab value="hechos" leftSection={<FileText size={14} />}>
        Hechos y Lugares
      </Tabs.Tab>
      <Tabs.Tab value="imputados" leftSection={<ShieldAlert size={14} />}>
        Imputados
      </Tabs.Tab>
      <Tabs.Tab value="institucional" leftSection={<Info size={14} />}>
        Programa / Defensa
      </Tabs.Tab>
      <Tabs.Tab value="gestion" leftSection={<Activity size={14} />}>
        Gestión y Seguimiento
      </Tabs.Tab>
    </Tabs.List>

    <Tabs.Panel value="nna">
      <Grid gutter="md">
        <Grid.Col span={6}>
          <TextInput
            label="Nombre NNA"
            value={form.nna ?? ""}
            onChange={(e) => set("nna", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <TextInput
            label="Cédula NNA"
            value={form.cedulaNna ?? ""}
            onChange={(e) => set("cedulaNna", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <Select
            label="Sexo"
            data={["M", "F", "X"]}
            value={form.sexoNna ?? null}
            onChange={(v) => set("sexoNna", v)}
            clearable
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <TextInput
            label="Edad"
            value={form.edad ?? ""}
            onChange={(e) => set("edad", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <TextInput
            label="Fecha nacimiento"
            placeholder="AAAA-MM-DD"
            value={form.fechaNacimiento ?? ""}
            onChange={(e) => set("fechaNacimiento", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <TextInput
            label="Nacionalidad"
            value={form.nacionalidadNna ?? ""}
            onChange={(e) => set("nacionalidadNna", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <Select
            label="Bajo cuidado del Estado"
            data={SINO}
            value={form.nnaBajoCuidadoEstado ?? null}
            onChange={(v) => set("nnaBajoCuidadoEstado", v)}
            clearable
          />
        </Grid.Col>
        <Grid.Col span={8}>
          <Select
            label="Delito concreto"
            data={DELITOS}
            searchable
            value={form.delitoConcreto ?? null}
            onChange={(v) => set("delitoConcreto", v)}
            clearable
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Tipo de violencia"
            value={form.tipoViolencia ?? ""}
            onChange={(e) => set("tipoViolencia", e.target.value)}
          />
        </Grid.Col>
      </Grid>
    </Tabs.Panel>

    <Tabs.Panel value="hechos">
      <Grid gutter="md">
        <Grid.Col span={12}>
          <Textarea
            label="Relato de hechos"
            autosize
            minRows={4}
            value={form.hechos ?? ""}
            onChange={(e) => set("hechos", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Lugar de ocurrencia"
            value={form.lugarOcurrenciaHechos ?? ""}
            onChange={(e) => set("lugarOcurrenciaHechos", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Comunas involucradas"
            value={form.comunasInvolucradas ?? ""}
            onChange={(e) => set("comunasInvolucradas", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <Select
            label="Presunta red de explotación"
            data={SINO}
            value={form.presuntaRedExplotacion ?? null}
            onChange={(v) => set("presuntaRedExplotacion", v)}
            clearable
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Locales/bares/hoteles"
            value={form.identificacionLocalesBaresHoteles ?? ""}
            onChange={(e) =>
              set("identificacionLocalesBaresHoteles", e.target.value)
            }
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <TagsInput
            label="Redes sociales mencionadas"
            placeholder="instagram:@usuario (Enter)"
            value={form.redesSocialesMencionadas ?? []}
            onChange={(v) => set("redesSocialesMencionadas", v)}
            clearable
          />
        </Grid.Col>
      </Grid>
    </Tabs.Panel>

    <Tabs.Panel value="imputados">
      <Grid mb="sm">
        <Grid.Col span={6}>
          <Select
            label="Imputado conocido"
            data={SINO}
            value={form.imputadoConocido ?? null}
            onChange={(v) => set("imputadoConocido", v)}
            clearable
          />
        </Grid.Col>
      </Grid>
      <ImputadosEditor
        value={form.imputados ?? []}
        onChange={(v) => set("imputados", v)}
      />
    </Tabs.Panel>

    <Tabs.Panel value="institucional">
      <Grid gutter="md">
        <Grid.Col span={4}>
          <TextInput
            label="Tipo de programa"
            value={form.tipoPrograma ?? ""}
            onChange={(e) => set("tipoPrograma", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Nombre programa/residencia"
            value={form.nombreProgramaResidencia ?? ""}
            onChange={(e) => set("nombreProgramaResidencia", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Residencia"
            value={form.residencia ?? ""}
            onChange={(e) => set("residencia", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={3}>
          <TextInput
            label="NAD/PMA"
            value={form.nadPma ?? ""}
            onChange={(e) => set("nadPma", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Contacto NAD/PMA"
            value={form.contactoNadPma ?? ""}
            onChange={(e) => set("contactoNadPma", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={5}>
          <TextInput
            label="Curador ad litem"
            value={form.curador ?? ""}
            onChange={(e) => set("curador", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <TextInput
            label="Consumo drogas/alcohol"
            value={form.consumoDrogasAlcohol ?? ""}
            onChange={(e) => set("consumoDrogasAlcohol", e.target.value)}
          />
        </Grid.Col>
      </Grid>
    </Tabs.Panel>

    <Tabs.Panel value="gestion">
      <Grid gutter="md">
        <Grid.Col span={4}>
          <TextInput
            label="N° Oficio"
            value={form.nroOficio ?? ""}
            onChange={(e) => set("nroOficio", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Fecha"
            placeholder="AAAA-MM-DD"
            value={form.fecha ?? ""}
            onChange={(e) => set("fecha", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <Select
            label="Región"
            data={REGIONES_SELECT}
            searchable
            value={form.region ?? null}
            onChange={(v) => set("region", v)}
            clearable
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="RUC asociados"
            value={form.rucAsociados ?? ""}
            onChange={(e) => set("rucAsociados", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="Carpeta"
            value={form.carpeta ?? ""}
            onChange={(e) => set("carpeta", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={4}>
          <TextInput
            label="N° correlativo"
            value={form.nroCorrelativo ?? ""}
            onChange={(e) => set("nroCorrelativo", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Denunciante"
            value={form.denunciante ?? ""}
            onChange={(e) => set("denunciante", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Contacto denunciante"
            value={form.contactoDenunciante ?? ""}
            onChange={(e) => set("contactoDenunciante", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Querella"
            value={form.querella ?? ""}
            onChange={(e) => set("querella", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={6}>
          <TextInput
            label="Para querella"
            value={form.paraQuerella ?? ""}
            onChange={(e) => set("paraQuerella", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Denuncias anteriores"
            autosize
            minRows={2}
            value={form.denunciasAnteriores ?? ""}
            onChange={(e) => set("denunciasAnteriores", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Gestiones"
            autosize
            minRows={2}
            value={form.gestiones ?? ""}
            onChange={(e) => set("gestiones", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Observación"
            autosize
            minRows={2}
            value={form.observacion ?? ""}
            onChange={(e) => set("observacion", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea
            label="Descripción"
            autosize
            minRows={2}
            value={form.descripcion ?? ""}
            onChange={(e) => set("descripcion", e.target.value)}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <TextInput
            label="Pendiente"
            value={form.pendiente ?? ""}
            onChange={(e) => set("pendiente", e.target.value)}
          />
        </Grid.Col>
      </Grid>
    </Tabs.Panel>
  </Tabs>
);

// ============================================================
// COMPONENTE PRINCIPAL
// ============================================================
export default function EsnnaDashboard() {
  // Tablero
  const [casos, setCasos] = useState<ResumenCaso[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [fSemaforo, setFSemaforo] = useState<string | null>(null);
  const [fEstado, setFEstado] = useState<string | null>(null);
  const [fRegion, setFRegion] = useState<string | null>(null);
  const [fTexto, setFTexto] = useState("");

  // Ingesta
  const [ingestaOpen, setIngestaOpen] = useState(false);
  const [files, setFiles] = useState<File[]>([]);
  const [procesando, setProcesando] = useState(false);

  // Revisión (resultado de /procesar, antes de guardar)
  const [resultado, setResultado] = useState<ProcesoResultado | null>(null);
  const [revForm, setRevForm] = useState<FormCaso>(emptyForm());
  const [revSemaforoFinal, setRevSemaforoFinal] = useState<Semaforo>("VERDE");
  const [guardando, setGuardando] = useState(false);

  // Detalle / edición
  const [detalle, setDetalle] = useState<DetalleCaso | null>(null);
  const [editForm, setEditForm] = useState<FormCaso>(emptyForm());
  const [editSemaforoFinal, setEditSemaforoFinal] = useState<Semaforo>("VERDE");
  const [editOpen, setEditOpen] = useState(false);
  const [editSaving, setEditSaving] = useState(false);

  // Estado
  const [estadoOpen, setEstadoOpen] = useState(false);
  const [estadoCaso, setEstadoCaso] = useState<ResumenCaso | null>(null);
  const [estadoDestino, setEstadoDestino] = useState<string | null>(null);
  const [estadoMotivo, setEstadoMotivo] = useState("");

  // Métricas
  const [metricas, setMetricas] = useState<Metricas | null>(null);
  const [metricasOpen, setMetricasOpen] = useState(false);

  // ---------------- API ----------------
  const fetchCasos = useCallback(async () => {
    setLoading(true);
    try {
      const params: any = { page: page - 1, size: 20 };
      if (fSemaforo) params.semaforo = fSemaforo;
      if (fEstado) params.estado = fEstado;
      if (fRegion) params.region = fRegion;
      if (fTexto.trim()) params.delito = fTexto.trim();
      const { data } = await api.get<Page<ResumenCaso>>("/esnna/casos", {
        params,
      });
      setCasos(data.content);
      setTotalPages(Math.max(1, data.totalPages));
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error",
        message: apiError(e, "No se pudieron cargar los casos"),
      });
    } finally {
      setLoading(false);
    }
  }, [page, fSemaforo, fEstado, fRegion, fTexto]);

  useEffect(() => {
    fetchCasos();
  }, [page, fSemaforo, fEstado, fRegion, fetchCasos]);

  // Procesar PDFs
  const handleProcesar = async () => {
    if (files.length === 0) return;
    setProcesando(true);
    const fd = new FormData();
    files.forEach((f) => fd.append("files", f));
    try {
      const { data } = await api.post<ProcesoResultado>("/esnna/procesar", fd, {
        headers: { "Content-Type": "multipart/form-data" },
        timeout: 300_000,
      });
      setResultado(data);
      setRevForm({ ...data.consolidado });
      setRevSemaforoFinal(data.semaforoIa);
      setIngestaOpen(false);
      setFiles([]);
      if (data.estadoCalidad === "PARCIAL") {
        notifications.show({
          color: "yellow",
          title: "Análisis parcial",
          message: `${data.documentosOmitidos.length} documento(s) omitido(s). Revise antes de guardar.`,
        });
      }
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error al procesar",
        message: apiError(e, "Falló el motor"),
      });
    } finally {
      setProcesando(false);
    }
  };

  // Guardar caso nuevo (desde revisión)
  const handleGuardar = async () => {
    if (!resultado) return;
    setGuardando(true);
    try {
      const body = {
        draftId: resultado.draftId,
        semaforoFinal: revSemaforoFinal,
        ...revForm,
      };
      const { data } = await api.post<DetalleCaso>("/esnna/guardar", body);
      notifications.show({
        color: "green",
        title: "Caso guardado",
        message: `Caso #${data.id} registrado de forma correcta.`,
        icon: <Check size={16} />,
      });
      setResultado(null);
      setPage(1);
      fetchCasos();
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error al guardar",
        message: apiError(e, "No se pudo guardar el caso"),
      });
    } finally {
      setGuardando(false);
    }
  };

  // Abrir detalle
  const abrirDetalle = async (id: number) => {
    try {
      const { data } = await api.get<DetalleCaso>(`/esnna/casos/${id}`);
      setDetalle(data);
      setEditForm(detalleToForm(data));
      setEditSemaforoFinal(data.semaforoFinal);
      setEditOpen(true);
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error",
        message: apiError(e, "No se pudo abrir el caso"),
      });
    }
  };

  // Guardar edición (PATCH)
  const handleEditar = async () => {
    if (!detalle) return;
    setEditSaving(true);
    try {
      const body: any = { ...editForm };
      if (editSemaforoFinal !== detalle.semaforoFinal)
        body.semaforoFinal = editSemaforoFinal;
      const { data } = await api.patch<DetalleCaso>(
        `/esnna/casos/${detalle.id}`,
        body,
      );
      notifications.show({
        color: "green",
        title: "Caso actualizado",
        message: `Caso #${data.id} actualizado de forma correcta.`,
      });
      setEditOpen(false);
      fetchCasos();
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error",
        message: apiError(e, "No se pudo actualizar"),
      });
    } finally {
      setEditSaving(false);
    }
  };

  // Cambio de estado
  const abrirEstado = (c: ResumenCaso) => {
    setEstadoCaso(c);
    setEstadoDestino(null);
    setEstadoMotivo("");
    setEstadoOpen(true);
  };
  const handleEstado = async () => {
    if (!estadoCaso || !estadoDestino) return;
    try {
      await api.post(`/esnna/casos/${estadoCaso.id}/estado`, {
        estadoDestino,
        motivo: estadoMotivo || null,
      });
      notifications.show({
        color: "green",
        title: "Estado actualizado",
        message: `→ ${estadoDestino}`,
      });
      setEstadoOpen(false);
      fetchCasos();
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Transición rechazada",
        message: apiError(e, "No permitido"),
      });
    }
  };

  // Descargas (blob)
  const descargar = async (url: string, filename: string) => {
    try {
      const { data } = await api.get(url, { responseType: "blob" });
      const href = URL.createObjectURL(data);
      const a = document.createElement("a");
      a.href = href;
      a.download = filename;
      a.click();
      URL.revokeObjectURL(href);
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error de descarga",
        message: apiError(e, "Falló la descarga"),
      });
    }
  };

  const verMetricas = async () => {
    try {
      const { data } = await api.get<Metricas>("/esnna/metricas/concordancia");
      setMetricas(data);
      setMetricasOpen(true);
    } catch (e: any) {
      notifications.show({
        color: "red",
        title: "Error",
        message: apiError(e, "No se pudieron cargar las métricas"),
      });
    }
  };

  const setRev = (k: keyof FormCaso, v: any) =>
    setRevForm((p) => ({ ...p, [k]: v }));
  const setEdit = (k: keyof FormCaso, v: any) =>
    setEditForm((p) => ({ ...p, [k]: v }));

  // ---------------- RENDER ----------------
  return (
    <div className="relative isolate p-6 sm:p-10 max-w-[1600px] mx-auto h-full flex flex-col gap-6">
      <div aria-hidden className="aurora aurora--esnna" />
      {/* HEADER FIEL A SGS */}
      <div className="relative overflow-hidden glass-panel rounded-2xl px-6 py-5 pl-7 flex flex-col md:flex-row md:items-end justify-between gap-4 before:content-[''] before:absolute before:inset-y-0 before:left-0 before:w-1.5 before:bg-gradient-to-b before:from-simdDanger before:to-simdPrimary">
        <div>
          <Group gap="xs" mb={5}>
            <Badge color="simdPrimary" variant="light" radius="sm">
              Motor ESNNA
            </Badge>
            <Text size="xs" c="dimmed" fw={600}>
              SMID | Priorización de Casos — Protocolo PR-PDR-05
            </Text>
          </Group>
          <Title order={1} className="font-extrabold tracking-tight">
            Análisis y Priorización de Casos
          </Title>
          <Group gap="md" mt={6}>
            <Group gap={6}>
              <Hash size={14} className="text-gray-400" />
              <Text size="xs" c="dimmed">
                {casos.length} visibles en página
              </Text>
            </Group>
            <Group gap={6}>
              <Activity size={14} className="text-gray-400" />
              <Text size="xs" c="dimmed">
                Gestión activa e ingesta automatizada
              </Text>
            </Group>
          </Group>
        </div>
        <Group>
          <Button
            variant="default"
            leftSection={<BarChart3 size={16} />}
            onClick={verMetricas}
          >
            Métricas
          </Button>
          <Button
            variant="default"
            leftSection={<FileSpreadsheet size={16} />}
            onClick={() =>
              descargar("/esnna/exportar-excel", "Matriz_ESNNA.xlsx")
            }
          >
            Exportar Excel
          </Button>
          <Button
            color="simdPrimary"
            leftSection={<FileUp size={16} />}
            onClick={() => setIngestaOpen(true)}
          >
            Ingestar oficios
          </Button>
        </Group>
      </div>

      {/* FILTROS CON CONTENEDOR MODERNO */}
      <Paper p="sm" radius="md">
        <Group>
          <TextInput
            placeholder="Buscar por delito o N° Oficio..."
            leftSection={<Search size={16} className="text-gray-400" />}
            value={fTexto}
            onChange={(e) => setFTexto(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && (setPage(1), fetchCasos())}
            className="flex-1 max-w-md"
          />
          <Select
            placeholder="Semáforo"
            data={["ROJO", "AMARILLO", "VERDE"]}
            value={fSemaforo}
            onChange={(v) => {
              setFSemaforo(v);
              setPage(1);
            }}
            clearable
            w={140}
          />
          <Select
            placeholder="Estado"
            data={ESTADOS}
            value={fEstado}
            onChange={(v) => {
              setFEstado(v);
              setPage(1);
            }}
            clearable
            w={200}
          />
          <Select
            placeholder="Región"
            data={REGIONES_SELECT}
            searchable
            value={fRegion}
            onChange={(v) => {
              setFRegion(v);
              setPage(1);
            }}
            clearable
            w={240}
          />
          <ActionIcon variant="default" size="lg" onClick={() => fetchCasos()}>
            <RefreshCw size={16} />
          </ActionIcon>
        </Group>
      </Paper>

      {/* TABLA CON BORDE SUPERIOR DE ACENTO INSTITUCIONAL Y GRADIENTE */}
      <div className="glass-panel rounded-xl border-t-4 border-t-simdPrimary flex-1 flex flex-col overflow-hidden">
        <ScrollArea className="flex-1">
          <Table verticalSpacing="md" horizontalSpacing="lg" highlightOnHover>
            <Table.Thead className="bg-gray-50/80">
              <Table.Tr>
                <Table.Th>Oficio</Table.Th>
                <Table.Th>Semáforo</Table.Th>
                <Table.Th>Motor</Table.Th>
                <Table.Th>Región</Table.Th>
                <Table.Th>Delito</Table.Th>
                <Table.Th>Edad</Table.Th>
                <Table.Th>Estado</Table.Th>
                <Table.Th>Ingreso</Table.Th>
                <Table.Th className="text-right">Acciones</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {loading ? (
                <Table.Tr>
                  <Table.Td colSpan={9} className="text-center py-10">
                    <Center p="xl">
                      <Loader color="simdPrimary" />
                    </Center>
                  </Table.Td>
                </Table.Tr>
              ) : casos.length === 0 ? (
                <Table.Tr>
                  <Table.Td colSpan={9} className="text-center py-10">
                    <Text c="dimmed">
                      No se encontraron registros de casos.
                    </Text>
                  </Table.Td>
                </Table.Tr>
              ) : (
                casos.map((c) => {
                  const rowGradient = getSemaforoGradient(c.semaforoFinal);
                  return (
                    <Table.Tr
                      key={c.id}
                      style={
                        rowGradient ? { background: rowGradient } : undefined
                      }
                    >
                      <Table.Td fw={700}>{c.nroOficio ?? "—"}</Table.Td>
                      <Table.Td>
                        <SemaforoBadge s={c.semaforoFinal} />
                      </Table.Td>
                      <Table.Td>
                        {c.semaforoFinal !== c.semaforoIa ? (
                          <Tooltip label={`El motor sugirió ${c.semaforoIa}`}>
                            <Badge
                              color={semColor(c.semaforoIa)}
                              variant="dot"
                              size="sm"
                            >
                              {c.semaforoIa}
                            </Badge>
                          </Tooltip>
                        ) : (
                          <Badge
                            color={semColor(c.semaforoIa)}
                            variant="light"
                            size="sm"
                          >
                            {c.semaforoIa}
                          </Badge>
                        )}
                      </Table.Td>
                      <Table.Td>{c.region ?? "—"}</Table.Td>
                      <Table.Td className="max-w-[220px]">
                        <Text size="sm" className="truncate fw-semibold">
                          {c.delitoConcreto ?? "—"}
                        </Text>
                      </Table.Td>
                      <Table.Td>{c.edad ?? "—"}</Table.Td>
                      <Table.Td>
                        <Badge
                          color={estadoColor(c.estadoGestion)}
                          variant="light"
                          size="sm"
                          radius="sm"
                        >
                          {c.estadoGestion}
                        </Badge>
                      </Table.Td>
                      <Table.Td>{fmt(c.fechaIngreso)}</Table.Td>
                      <Table.Td className="text-right">
                        <Menu position="bottom-end" withArrow>
                          <Menu.Target>
                            <ActionIcon variant="subtle" color="gray">
                              <MoreVertical size={18} />
                            </ActionIcon>
                          </Menu.Target>
                          <Menu.Dropdown>
                            <Menu.Item
                              leftSection={<Eye size={14} />}
                              onClick={() => abrirDetalle(c.id)}
                            >
                              Ver / Editar Caso
                            </Menu.Item>
                            <Menu.Item
                              leftSection={<GitBranch size={14} />}
                              onClick={() => abrirEstado(c)}
                            >
                              Cambiar Estado
                            </Menu.Item>
                            <Menu.Item
                              leftSection={<FileText size={14} />}
                              onClick={() =>
                                descargar(
                                  `/esnna/casos/${c.id}/informe`,
                                  `Informe_ESNNA_${c.id}.pdf`,
                                )
                              }
                            >
                              Descargar Informe PDF
                            </Menu.Item>
                          </Menu.Dropdown>
                        </Menu>
                      </Table.Td>
                    </Table.Tr>
                  );
                })
              )}
            </Table.Tbody>
          </Table>
        </ScrollArea>
      </div>

      <Group justify="center" mt="md">
        <Pagination
          total={totalPages}
          value={page}
          onChange={setPage}
          color="simdPrimary"
        />
      </Group>

      {/* ============ DRAWER INGESTA ============ */}
      <Drawer
        opened={ingestaOpen}
        onClose={() => setIngestaOpen(false)}
        title={
          <Text fw={700} size="lg">
            Ingestar Oficios de Casos (PDF)
          </Text>
        }
        position="right"
        size="lg"
      >
        <Stack gap="md" h="100%">
          <Alert
            color="simdPrimary"
            variant="light"
            icon={<FileSearch size={16} />}
          >
            Sube los archivos correspondientes a los oficios en formato PDF. El
            motor de análisis extraerá y clasificará las prioridades de forma
            automatizada siguiendo el protocolo institucional.
          </Alert>

          <Dropzone
            onDrop={setFiles}
            accept={PDF_MIME_TYPE}
            multiple
            maxFiles={30}
            loading={procesando}
            className={`border-2 border-dashed rounded-xl p-10 transition-all ${files.length > 0 ? "border-simdPrimary bg-simdPrimary/5" : "border-gray-300 hover:border-simdPrimary/50"}`}
          >
            <Center mih={140}>
              <Stack align="center" gap={4}>
                <FileUp size={40} className="text-simdPrimary" />
                <Text fw={700} size="lg">
                  Arrastra hasta 30 PDF aquí
                </Text>
                <Text size="sm" c="dimmed">
                  o haz clic para examinar tus archivos locales
                </Text>
              </Stack>
            </Center>
          </Dropzone>

          {files.length > 0 && (
            <Paper withBorder p="sm" radius="md" className="bg-gray-50">
              <Text size="sm" fw={600} mb={4}>
                {files.length} archivo(s) seleccionados:
              </Text>
              <ScrollArea h={120}>
                <Stack gap={2}>
                  {files.map((f, i) => (
                    <Text key={i} size="xs" c="dimmed" className="truncate">
                      • {f.name}
                    </Text>
                  ))}
                </Stack>
              </ScrollArea>
            </Paper>
          )}

          <Button
            color="simdPrimary"
            leftSection={<Settings size={16} />}
            loading={procesando}
            disabled={files.length === 0}
            onClick={handleProcesar}
            mt="auto"
            size="md"
          >
            Procesar Documentos {files.length > 0 ? `(${files.length})` : ""}
          </Button>
        </Stack>
      </Drawer>

      {/* ============ MODAL REVISIÓN (Resultado de procesar) ============ */}
      <Modal
        opened={!!resultado}
        onClose={() => setResultado(null)}
        title={
          <Group gap="xs">
            <FileSearch size={20} className="text-simdPrimary" />
            <Text fw={700} size="lg">
              Revisión del Análisis Automatizado
            </Text>
          </Group>
        }
        size="90%"
        scrollAreaComponent={ScrollArea.Autosize}
      >
        {resultado && (
          <Stack gap="lg">
            <Grid gutter="md">
              <Grid.Col span={{ base: 12, md: 5 }}>
                <Stack gap="md">
                  <Paper
                    withBorder
                    p="md"
                    radius="md"
                  >
                    <Group justify="space-between" mb="xs">
                      <Text fw={700}>Propuesta de Prioridad</Text>
                      <SemaforoBadge s={resultado.semaforoIa} size="lg" />
                    </Group>
                    <Text size="xs" c="dimmed" mb="sm">
                      Nivel de Confianza:{" "}
                      <b>
                        {(
                          resultado.consolidado.confianzaAnalisis * 100
                        ).toFixed(0)}
                        %
                      </b>
                    </Text>
                    <Alert color="blue" variant="light" p="xs">
                      {resultado.justificacionIa}
                    </Alert>
                    <Divider my="sm" />
                    <CriteriosPanel cr={resultado.consolidado.criterios} />
                  </Paper>

                  <Paper
                    withBorder
                    p="md"
                    radius="md"
                    className="border-l-4 border-l-simdPrimary"
                  >
                    <Text
                      fw={700}
                      size="sm"
                      mb="xs"
                      tt="uppercase"
                      c="simdPrimary"
                    >
                      Validación de Decisión Humana
                    </Text>
                    <Select
                      label="Confirmar o corregir semáforo final"
                      data={["ROJO", "AMARILLO", "VERDE"]}
                      value={revSemaforoFinal}
                      onChange={(v) => v && setRevSemaforoFinal(v as Semaforo)}
                      allowDeselect={false}
                    />
                    {revSemaforoFinal !== resultado.semaforoIa && (
                      <Alert
                        color="orange"
                        mt="sm"
                        p="xs"
                        icon={<AlertTriangle size={14} />}
                      >
                        Se registrará una discrepancia/override manual respecto
                        a la sugerencia del motor ({resultado.semaforoIa} →{" "}
                        {revSemaforoFinal}).
                      </Alert>
                    )}
                  </Paper>

                  <Paper
                    p="sm"
                    radius="md"
                    className="bg-gray-50 border text-xs"
                  >
                    <Stack gap={2}>
                      <Text>
                        Calidad: <b>{resultado.estadoCalidad}</b>
                      </Text>
                      <Text>
                        Documentos:{" "}
                        <b>
                          {resultado.documentosProcesados}/
                          {resultado.documentosTotales}
                        </b>
                      </Text>
                      <Text>
                        Uso de tokens: Prompt {resultado.usage.tokensPrompt} |
                        Completion {resultado.usage.tokensCompletion}
                      </Text>
                    </Stack>
                    {resultado.documentosOmitidos.length > 0 && (
                      <Alert
                        color="yellow"
                        mt="xs"
                        p="xs"
                        icon={<AlertTriangle size={14} />}
                      >
                        Archivos omitidos por error en fase:{" "}
                        {resultado.documentosOmitidos
                          .map((d) => d.nombreArchivo)
                          .join(", ")}
                      </Alert>
                    )}
                  </Paper>
                </Stack>
              </Grid.Col>

              <Grid.Col span={{ base: 12, md: 7 }}>
                <Paper withBorder p="md" radius="md" className="shadow-sm">
                  <FichaTabsForm form={revForm} set={setRev} />
                </Paper>
              </Grid.Col>
            </Grid>

            <Group
              justify="flex-end"
              pt="md"
              className="border-t sticky bottom-0 bg-white z-10"
            >
              <Button variant="default" onClick={() => setResultado(null)}>
                Descartar Análisis
              </Button>
              <Button
                color="simdPrimary"
                leftSection={<Check size={16} />}
                loading={guardando}
                onClick={handleGuardar}
              >
                Confirmar y Guardar Caso
              </Button>
            </Group>
          </Stack>
        )}
      </Modal>

      {/* ============ MODAL DETALLE / EDICIÓN ============ */}
      <Modal
        opened={editOpen}
        onClose={() => setEditOpen(false)}
        title={
          detalle && (
            <Group gap="xs">
              <ShieldAlert size={20} className="text-simdPrimary" />
              <Text fw={700} size="lg">
                Expediente de Caso #{detalle.id}
              </Text>
              <Badge
                color={estadoColor(detalle.estadoGestion)}
                variant="light"
                radius="sm"
              >
                {detalle.estadoGestion}
              </Badge>
            </Group>
          )
        }
        size="90%"
        scrollAreaComponent={ScrollArea.Autosize}
      >
        {detalle && (
          <Stack gap="lg">
            {/* PANEL HEAD VISUAL SUPERIOR */}
            <Paper
              p="md"
              radius="md"
              className="border"
            >
              <Grid align="center">
                <Grid.Col span={4}>
                  <Text size="xs" c="dimmed" tt="uppercase" fw={700}>
                    N° Oficio Referencial
                  </Text>
                  <Title order={3} className="font-mono">
                    {detalle.nroOficio || "Sin Oficio"}
                  </Title>
                  <Group gap={6} mt={4}>
                    <Calendar size={12} className="text-gray-400" />
                    <Text size="xs" c="dimmed">
                      Ingreso: {fmt(detalle.fechaIngreso)}
                    </Text>
                  </Group>
                </Grid.Col>
                <Grid.Col span={4}>
                  <Group gap="lg" justify="center">
                    <div>
                      <Text
                        size="xs"
                        c="dimmed"
                        tt="uppercase"
                        fw={700}
                        ta="center"
                      >
                        Sugerido
                      </Text>
                      <SemaforoBadge s={detalle.semaforoIa} />
                    </div>
                    <div>
                      <Text
                        size="xs"
                        c="dimmed"
                        tt="uppercase"
                        fw={700}
                        ta="center"
                      >
                        Validado Final
                      </Text>
                      <SemaforoBadge s={detalle.semaforoFinal} />
                    </div>
                  </Group>
                </Grid.Col>
                <Grid.Col span={4} className="text-right">
                  <Stack gap={2} align="flex-end">
                    <Text size="xs" c="dimmed">
                      Última actualización: {fmt(detalle.fechaActualizacion)}
                    </Text>
                    {detalle.anulado && (
                      <Badge
                        color="dark"
                        variant="filled"
                        size="sm"
                        leftSection={<Ban size={12} />}
                      >
                        ANULADO
                      </Badge>
                    )}
                  </Stack>
                </Grid.Col>
              </Grid>
            </Paper>

            {/* SECCIÓN DE VISTA DE CAMPOS TIPO FICHA DE ORIGEN */}
            <Paper
              p="md"
              radius="md"
              className="border-l-4 border-l-blue-500 bg-blue-50/10 border"
            >
              <Group gap="xs" mb="sm">
                <Info size={16} className="text-blue-600" />
                <Text
                  fw={800}
                  c="blue.8"
                  tt="uppercase"
                  size="xs"
                  style={{ letterSpacing: 0.5 }}
                >
                  Resumen Informativo de la Ficha Activa
                </Text>
              </Group>
              <Grid gutter="xs">
                <Grid.Col span={3}>
                  <FieldRow label="Víctima NNA" value={detalle.nna} />
                </Grid.Col>
                <Grid.Col span={3}>
                  <FieldRow label="Cédula" value={detalle.cedulaNna} />
                </Grid.Col>
                <Grid.Col span={3}>
                  <FieldRow label="Región" value={detalle.region} />
                </Grid.Col>
                <Grid.Col span={3}>
                  <FieldRow label="Delito" value={detalle.delitoConcreto} />
                </Grid.Col>
              </Grid>
              {detalle.justificacionIa && (
                <Box mt="xs" className="bg-white p-2 rounded border">
                  <Text size="xs" c="dimmed">
                    <b>Justificación del Motor de Análisis:</b>{" "}
                    {detalle.justificacionIa}
                  </Text>
                </Box>
              )}
              {detalle.anulado && detalle.motivoAnulacion && (
                <Alert color="dark" mt="sm" p="xs" icon={<Ban size={14} />}>
                  <b>Motivo de anulación registrado:</b>{" "}
                  {detalle.motivoAnulacion}
                </Alert>
              )}
            </Paper>

            {/* FORMULARIO EDITABLE EN PESTAÑAS */}
            <Grid gutter="md">
              <Grid.Col span={{ base: 12, md: 8 }}>
                <Paper withBorder p="md" radius="md" className="shadow-sm">
                  <FichaTabsForm form={editForm} set={setEdit} />
                </Paper>
              </Grid.Col>

              <Grid.Col span={{ base: 12, md: 4 }}>
                <Stack gap="md">
                  <Paper withBorder p="md" radius="md" className="bg-gray-50">
                    <Text
                      fw={700}
                      size="sm"
                      mb="xs"
                      tt="uppercase"
                      c="simdPrimary"
                    >
                      Controles de Priorización
                    </Text>
                    <Select
                      label="Semáforo de Prioridad Final"
                      data={["ROJO", "AMARILLO", "VERDE"]}
                      value={editSemaforoFinal}
                      onChange={(v) => v && setEditSemaforoFinal(v as Semaforo)}
                      allowDeselect={false}
                      disabled={detalle.anulado}
                    />
                  </Paper>

                  <Alert color="blue" icon={<Info size={16} />} variant="light">
                    Modificar los campos alterará directamente la versión del
                    registro en la matriz histórica. Asegúrese de guardar los
                    cambios antes de salir.
                  </Alert>
                </Stack>
              </Grid.Col>
            </Grid>

            {/* BARRA DE ACCIONES DEL EXPEDIENTE */}
            <Group
              justify="flex-end"
              pt="md"
              className="border-t sticky bottom-0 bg-white z-10"
            >
              <Button variant="default" onClick={() => setEditOpen(false)}>
                Cerrar Expediente
              </Button>
              <Button
                color="simdPrimary"
                leftSection={<Check size={16} />}
                loading={editSaving}
                disabled={detalle.anulado}
                onClick={handleEditar}
              >
                Guardar Modificaciones
              </Button>
            </Group>
          </Stack>
        )}
      </Modal>

      {/* ============ MODAL TRANSICIÓN DE ESTADO ============ */}
      <Modal
        opened={estadoOpen}
        onClose={() => setEstadoOpen(false)}
        title={<Text fw={700}>Cambiar Estado de Gestión Administrativa</Text>}
      >
        {estadoCaso && (
          <Stack gap="md">
            <Text size="sm">
              Caso <b>#{estadoCaso.id}</b> · Estado actual del registro:{" "}
              <Badge
                color={estadoColor(estadoCaso.estadoGestion)}
                variant="light"
                radius="sm"
              >
                {estadoCaso.estadoGestion}
              </Badge>
            </Text>
            <Select
              label="Seleccionar estado de destino"
              data={TRANSICIONES[estadoCaso.estadoGestion] ?? []}
              value={estadoDestino}
              onChange={setEstadoDestino}
              placeholder={
                (TRANSICIONES[estadoCaso.estadoGestion] ?? []).length === 0
                  ? "Sin transiciones disponibles (Estado terminal)"
                  : "Seleccione el próximo estado"
              }
            />
            {estadoDestino === "ANULADO" && (
              <Textarea
                label="Motivo de anulación del caso (Obligatorio)"
                value={estadoMotivo}
                onChange={(e) => setEstadoMotivo(e.target.value)}
                placeholder="Escriba la justificación legal o administrativa para anular este registro..."
                autosize
                minRows={2}
              />
            )}
            <Group justify="flex-end" mt="sm">
              <Button variant="default" onClick={() => setEstadoOpen(false)}>
                Cancelar
              </Button>
              <Button
                color="simdPrimary"
                disabled={
                  !estadoDestino ||
                  (estadoDestino === "ANULADO" && !estadoMotivo.trim())
                }
                onClick={handleEstado}
              >
                Confirmar Transición
              </Button>
            </Group>
          </Stack>
        )}
      </Modal>

      {/* ============ MODAL MÉTRICAS DE CONCORDANCIA ============ */}
      <Modal
        opened={metricasOpen}
        onClose={() => setMetricasOpen(false)}
        title={
          <Text fw={700} size="lg">
            Concordancia: Motor de Análisis vs Criterio Humano
          </Text>
        }
      >
        {metricas && (
          <Stack gap="md">
            <Group grow>
              <Paper
                withBorder
                p="sm"
                radius="md"
                ta="center"
                className="bg-gray-50"
              >
                <Text size="xs" c="dimmed" tt="uppercase" fw={700}>
                  Casos Evaluados
                </Text>
                <Text fw={800} size="xl" c="simdPrimary">
                  {metricas.totalCasos}
                </Text>
              </Paper>
              <Paper
                withBorder
                p="sm"
                radius="md"
                ta="center"
                className="bg-gray-50"
              >
                <Text size="xs" c="dimmed" tt="uppercase" fw={700}>
                  Correcciones (Overrides)
                </Text>
                <Text fw={800} size="xl">
                  {metricas.overrides}
                </Text>
              </Paper>
              <Paper
                withBorder
                p="sm"
                radius="md"
                ta="center"
                className="bg-gray-50"
              >
                <Text size="xs" c="dimmed" tt="uppercase" fw={700}>
                  Tasa de Ajuste
                </Text>
                <Text fw={800} size="xl">
                  {(metricas.tasaOverride * 100).toFixed(0)}%
                </Text>
              </Paper>
            </Group>

            <Group grow>
              <Alert
                color="orange"
                variant="light"
                title="Casos Subestimados por el Motor"
              >
                Cantidad de registros: {metricas.subestimacionIa}
              </Alert>
              <Alert
                color="blue"
                variant="light"
                title="Casos Sobreestimados por el Motor"
              >
                Cantidad de registros: {metricas.sobreestimacionIa}
              </Alert>
            </Group>

            <Divider
              label="Matriz de Confusión y Derivaciones Semánticas"
              labelPosition="center"
            />
            <Stack gap={4}>
              {Object.entries(metricas.matrizConfusion).length === 0 ? (
                <Text size="sm" c="dimmed" ta="center" fs="italic">
                  No hay datos suficientes para calcular la concordancia actual.
                </Text>
              ) : (
                Object.entries(metricas.matrizConfusion).map(([k, v]) => (
                  <Group
                    key={k}
                    justify="space-between"
                    className="p-1 border-b"
                  >
                    <Text size="sm" className="font-mono">
                      {k.replace("->", " → ")}
                    </Text>
                    <Badge variant="light" color="simdPrimary">
                      {v}
                    </Badge>
                  </Group>
                ))
              )}
            </Stack>
          </Stack>
        )}
      </Modal>
    </div>
  );
}
