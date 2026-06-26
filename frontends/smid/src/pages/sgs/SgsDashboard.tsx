import { useState, useEffect, useCallback } from "react";
import {
  Title,
  Text,
  Button,
  Badge,
  Table,
  Group,
  ActionIcon,
  Drawer,
  ScrollArea,
  TextInput,
  Textarea,
  Grid,
  Modal,
  Select,
  Stack,
  Alert,
  Stepper,
  Checkbox,
  Divider,
  Paper,
  Pagination,
  Accordion,
  Switch,
  Loader,
  Tooltip,
  Center,
  Box,
} from "@mantine/core";
import { Dropzone, PDF_MIME_TYPE } from "@mantine/dropzone";
import {
  FileUp,
  Search,
  Eye,
  FileSpreadsheet,
  Bot,
  Check,
  Info,
  ClipboardCheck,
  ListChecks,
  FileSearch,
  Sparkles,
  ShieldCheck,
  Gavel,
  Trash2,
  RefreshCw,
  Clock,
  FileText,
  Hash,
  Activity,
} from "lucide-react";
import { notifications } from "@mantine/notifications";
import { api } from "../../api/axiosConfig";

// ============================================================
// TIPOS
// ============================================================

interface Catalogo {
  id: number;
  tipo: string;
  codigo: string;
  etiqueta: string;
  activo: boolean;
}

interface SgsResumen {
  oficioId: number;
  recomendacionId: number;
  nroOficio: string;
  region: string;
  institucion: string;
  dimension: string;
  correlativo: string;
  plazo: string | null;
  gv: boolean;
  estado: string;
  ultimaEvaluacion: string | null;
  requiereRevisionHumana: boolean;
  fechaIngreso: string;
}

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

interface AccionOut {
  id: number;
  orden: number;
  descripcion: string;
}

interface SeguimientoDetalle {
  id: number;
  fase: string | null;
  fechaSeguimiento: string | null;
  tipoSeguimiento: string | null;
  tipoRespuesta: string | null;
  fechaRespuesta: string | null;
  otroSeguimientoInstitucional: string | null;
  evaluacionIA: string | null;
  valoracionRubrica: string | null;
  confianza: number | null;
  razonamiento: string | null;
  requiereRevisionHumana: boolean;
  evaluacionFinal: string | null;
  evaluacionFinalAutor: string | null;
  evaluacionFinalFecha: string | null;
  responsableSeguimiento: string | null;
}

interface RecomendacionDetalle {
  id: number;
  correlativo: string;
  dimension: string;
  nudoCritico: string;
  tipoRecomendacion: string | null;
  verbo: string | null;
  descripcion: string | null;
  plazo: string | null;
  plazoRaw: string | null;
  gv: boolean;
  acoge: string | null;
  estado: string;
  materia: string | null;
  categoria: string | null;
  profesionalResponsable: string | null;
  responsableSeguimiento: string | null;
  anulado: boolean;
  acciones: AccionOut[];
  seguimientos: SeguimientoDetalle[];
}

interface OficioDetalle {
  id: number;
  nroOficio: string;
  region: string;
  institucion: string;
  residenciaCentro: string | null;
  nivel: string | null;
  pdfHash: string | null;
  fechaIngreso: string;
  fechaActualizacion: string;
  recomendaciones: RecomendacionDetalle[];
}

interface RecomendacionExtraccion {
  correlativo: string;
  dimension: string;
  nudoCritico: string;
  tipoRecomendacion: string | null;
  verbo: string | null;
  descripcion: string | null;
  plazoRaw: string | null;
  plazoEnum: string | null;
  gv: boolean;
  materiaSugerida: string | null;
  categoriaSugerida: string | null;
}
interface OficioExtraccion {
  nroOficio: string;
  region: string;
  institucion: string;
  residenciaCentro: string | null;
  nivel: string | null;
  pdfHash: string;
  recomendaciones: RecomendacionExtraccion[];
}
interface Usage {
  tokensPrompt: number;
  tokensCompletion: number;
  tokensReasoning: number;
}
interface ExtraccionJobResult {
  extraccion: OficioExtraccion;
  modelo: string;
  modeloSnapshot: string;
  usage: Usage;
}

interface EvaluacionPropuesta {
  id: number;
  confianzaMatch: number | null;
  requiereRevisionHumana: boolean;
  razonamiento: string | null;
  evaluacionCumplimiento: string | null;
  valoracionRubrica: string | null;
  tipoSeguimientoSugerido: string | null;
  tipoRespuestaSugerido: string | null;
  fechaSeguimiento: string | null;
  fechaRespuesta: string | null;
  otroSeguimientoInstitucional: string | null;
}
interface EvaluacionResult {
  evaluadas: EvaluacionPropuesta[];
  sinMatch: number[];
  estadoCalidad: string;
  omitidos: number[];
  usage: Usage;
}

interface JobEstado<P> {
  jobId: string;
  tipo: string;
  status: string;
  payload: P | null;
  error: string | null;
}

// ============================================================
// CONSTANTES Y HELPERS
// ============================================================

const VALORACION_OPTIONS = [
  "Cumplimiento Total",
  "Cumplimiento Parcial Sustancial",
  "Cumplimiento Parcial",
  "Incumplimiento",
  "No hay información",
  "No aplica",
];

const PLAZO_OPTIONS = [
  "Urgente",
  "Corto Plazo",
  "Mediano Plazo",
  "Largo Plazo",
];

const FASE_OPTIONS = [
  { value: "F1_REGISTRO", label: "Fase 1 - Registro" },
  { value: "F2_ANALISIS", label: "Fase 2 - Análisis Preliminar" },
  { value: "F3_EVALUACION_VISITA", label: "Fase 3 - Evaluación y Visita" },
  {
    value: "F4_NUEVAS_RECOMENDACIONES",
    label: "Fase 4 - Nuevas Recomendaciones",
  },
  { value: "ADMINISTRATIVO", label: "Administrativo" },
];

const ESTADO_LABEL: Record<string, string> = {
  PENDIENTE_REGISTRO: "Pendiente registro",
  EN_SEGUIMIENTO: "En seguimiento",
  NO_ACOGIDA: "No acogida",
  EN_INSISTENCIA: "En insistencia",
  CERRADA_CUMPLIDA: "Cerrada (cumplida)",
  CERRADA_INCUMPLIDA: "Cerrada (incumplida)",
  ANULADA: "Anulada",
};

const ESTADO_TRANSICIONES: Record<string, string[]> = {
  PENDIENTE_REGISTRO: ["EN_SEGUIMIENTO", "NO_ACOGIDA"],
  EN_SEGUIMIENTO: ["CERRADA_CUMPLIDA", "CERRADA_INCUMPLIDA", "EN_INSISTENCIA"],
  NO_ACOGIDA: ["EN_INSISTENCIA", "CERRADA_INCUMPLIDA"],
  EN_INSISTENCIA: ["CERRADA_CUMPLIDA", "CERRADA_INCUMPLIDA"],
  CERRADA_CUMPLIDA: [],
  CERRADA_INCUMPLIDA: ["EN_SEGUIMIENTO"],
  ANULADA: [],
};

const getEstadoColor = (e: string) =>
  ({
    PENDIENTE_REGISTRO: "yellow",
    EN_SEGUIMIENTO: "blue",
    NO_ACOGIDA: "orange",
    EN_INSISTENCIA: "grape",
    CERRADA_CUMPLIDA: "teal",
    CERRADA_INCUMPLIDA: "red",
    ANULADA: "dark",
  })[e] || "gray";

const getValoracionColor = (val?: string | null) => {
  if (!val) return "gray";
  return (
    (
      {
        "Cumplimiento Total": "teal",
        "Cumplimiento Parcial Sustancial": "lime",
        "Cumplimiento Parcial": "yellow",
        Incumplimiento: "red",
      } as Record<string, string>
    )[val] || "gray"
  );
};

const getPlazoColor = (p?: string | null) =>
  ({
    Urgente: "red",
    "Corto Plazo": "orange",
    "Mediano Plazo": "blue",
    "Largo Plazo": "gray",
  })[p || ""] || "gray";

const getConfianzaColor = (c: number) =>
  c >= 0.8 ? "teal" : c >= 0.6 ? "yellow" : "red";

const getPlazoGradient = (p?: string | null): string | undefined => {
  if (!p) return undefined;
  const colorMap: Record<string, string> = {
    Urgente: "rgba(239, 68, 68, 0.12)",
    "Corto Plazo": "rgba(249, 115, 22, 0.12)",
    "Mediano Plazo": "rgba(59, 130, 246, 0.12)",
  };
  const color = colorMap[p];
  if (!color) return undefined;
  return `linear-gradient(to right, ${color} 0%, ${color} 15%, transparent 50%)`;
};

const formatDate = (d?: string | null) => {
  if (!d) return null;
  const date = new Date(d);
  return isNaN(date.getTime())
    ? d
    : date.toLocaleDateString("es-CL", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
};

const todayISO = () => new Date().toISOString().slice(0, 10);

const catToSelect = (items: Catalogo[]) =>
  items.map((c) => ({ value: String(c.id), label: c.etiqueta }));

async function pollJob<P>(
  jobId: string,
  intervalMs = 2000,
  maxTries = 60,
): Promise<P> {
  for (let i = 0; i < maxTries; i++) {
    const { data } = await api.get<JobEstado<P>>(`/sgs/jobs/${jobId}`);
    if (data.status === "COMPLETADO" && data.payload) return data.payload;
    if (data.status === "FALLIDO")
      throw new Error(data.error || "El procesamiento falló.");
    await new Promise((r) => setTimeout(r, intervalMs));
  }
  throw new Error("El procesamiento excedió el tiempo de espera.");
}

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

// ============================================================
// DRAWER: INGESTA IA
// ============================================================

const IngestaDrawerContent = ({
  catalogos,
  onClose,
  onOk,
}: {
  catalogos: { materia: Catalogo[]; categoria: Catalogo[] };
  onClose: () => void;
  onOk: () => void;
}) => {
  const [file, setFile] = useState<File | null>(null);
  const [phase, setPhase] = useState<"upload" | "processing" | "review">(
    "upload",
  );
  const [isSaving, setIsSaving] = useState(false);
  const [job, setJob] = useState<ExtraccionJobResult | null>(null);
  const [oficio, setOficio] = useState<OficioExtraccion | null>(null);
  const [recMeta, setRecMeta] = useState<
    Record<
      number,
      {
        materiaId: string | null;
        categoriaId: string | null;
        acoge: string;
        plazo: string | null;
        gv: boolean;
      }
    >
  >({});

  const handleAnalizar = async () => {
    if (!file) return;
    setPhase("processing");
    const fd = new FormData();
    fd.append("file", file);
    try {
      const { data } = await api.post("/sgs/procesar-pdf", fd, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      const result = await pollJob<ExtraccionJobResult>(data.jobId);
      setJob(result);
      setOficio(result.extraccion);
      const meta: typeof recMeta = {};
      result.extraccion.recomendaciones.forEach((r, i) => {
        meta[i] = {
          materiaId: null,
          categoriaId: null,
          acoge: "SI",
          plazo: r.plazoEnum,
          gv: r.gv,
        };
      });
      setRecMeta(meta);
      setPhase("review");
      notifications.show({
        title: "Extracción lista",
        message: `IA detectó ${result.extraccion.recomendaciones.length} recomendaciones.`,
        color: "simdPrimary",
        icon: <Check size={16} />,
      });
    } catch (e: any) {
      notifications.show({
        title: "Error de extracción",
        message: e?.message || "No se pudo procesar el PDF.",
        color: "red",
      });
      setPhase("upload");
    }
  };

  const handleGuardar = async () => {
    if (!oficio || !job) return;
    setIsSaving(true);
    const body = {
      nroOficio: oficio.nroOficio,
      region: oficio.region,
      institucion: oficio.institucion,
      residenciaCentro: oficio.residenciaCentro,
      nivel: oficio.nivel,
      pdfHash: oficio.pdfHash,
      recomendaciones: oficio.recomendaciones.map((r, i) => ({
        correlativo: r.correlativo,
        dimension: r.dimension,
        nudoCritico: r.nudoCritico,
        tipoRecomendacion: r.tipoRecomendacion,
        verbo: r.verbo,
        descripcion: r.descripcion,
        plazo: recMeta[i]?.plazo ?? r.plazoEnum,
        plazoRaw: r.plazoRaw,
        gv: recMeta[i]?.gv ?? r.gv,
        acoge: recMeta[i]?.acoge ?? "SI",
        materiaId: recMeta[i]?.materiaId ? Number(recMeta[i].materiaId) : null,
        categoriaId: recMeta[i]?.categoriaId
          ? Number(recMeta[i].categoriaId)
          : null,
        acciones: [],
      })),
      auditMeta: {
        modelo: job.modelo,
        modeloSnapshot: job.modeloSnapshot,
        documentos: [
          { nombreArchivo: file?.name || "oficio.pdf", sha256: oficio.pdfHash },
        ],
        tokensPrompt: job.usage.tokensPrompt,
        tokensCompletion: job.usage.tokensCompletion,
        tokensReasoning: job.usage.tokensReasoning,
      },
    };
    try {
      const { data } = await api.post("/sgs/guardar", body);
      notifications.show({
        title: "Oficio guardado",
        message: `${data.recomendacionesCreadas} recomendaciones registradas.`,
        color: "teal",
      });
      onOk();
      onClose();
    } catch (e: any) {
      notifications.show({
        title: "Error al guardar",
        message: e?.response?.data?.message || "No se pudo registrar.",
        color: "red",
      });
    } finally {
      setIsSaving(false);
    }
  };

  if (phase === "processing") {
    return (
      <Stack h="100%" align="center" justify="center" gap="md">
        <Loader color="simdPrimary" size="lg" />
        <Text fw={600}>Procesando con IA…</Text>
        <Text size="sm" c="dimmed">
          Extrayendo el oficio y sus recomendaciones. Puede tardar ~30 s.
        </Text>
      </Stack>
    );
  }

  if (phase === "upload") {
    return (
      <Stack h="100%" gap="md">
        <Alert
          color="simdPrimary"
          variant="light"
          icon={<Sparkles size={16} />}
        >
          Sube el oficio en PDF. La IA extraerá región, institución y{" "}
          <strong>todas</strong> las recomendaciones con su plazo.
        </Alert>
        <Dropzone
          onDrop={(f) => setFile(f[0])}
          accept={PDF_MIME_TYPE}
          className={`border-2 border-dashed rounded-xl p-10 transition-all ${file ? "border-simdPrimary bg-simdPrimary/5" : "border-gray-300 hover:border-simdPrimary/50"}`}
        >
          <Center mih={140}>
            <Stack align="center" gap={4}>
              <FileUp
                size={40}
                className={file ? "text-simdPrimary" : "text-gray-400"}
              />
              <Text fw={700} size="lg">
                {file ? file.name : "Arrastra el PDF aquí"}
              </Text>
              <Text size="sm" c="dimmed">
                Click para seleccionar. Máx 10MB, con capa de texto.
              </Text>
            </Stack>
          </Center>
        </Dropzone>
        <Group justify="flex-end" mt="auto">
          <Button variant="default" onClick={onClose}>
            Cancelar
          </Button>
          <Button
            color="simdPrimary"
            leftSection={<Bot size={18} />}
            disabled={!file}
            onClick={handleAnalizar}
          >
            Analizar con IA
          </Button>
        </Group>
      </Stack>
    );
  }

  return (
    <Stack h="100%" gap="md">
      <Alert
        color="simdPrimary"
        variant="light"
        icon={<ShieldCheck size={16} />}
      >
        Revisa los datos y asigna materia/categoría a cada recomendación antes
        de confirmar.
      </Alert>
      <ScrollArea className="flex-1 pr-2">
        <Stack gap="lg">
          <Paper
            withBorder
            p="md"
            radius="md"
            className="shadow-sm border-l-4 border-l-simdPrimary"
          >
            <Grid>
              <Grid.Col span={4}>
                <TextInput
                  label="N° Oficio"
                  value={oficio?.nroOficio || ""}
                  onChange={(e) =>
                    setOficio({ ...oficio!, nroOficio: e.target.value })
                  }
                />
              </Grid.Col>
              <Grid.Col span={4}>
                <TextInput
                  label="Región"
                  value={oficio?.region || ""}
                  onChange={(e) =>
                    setOficio({ ...oficio!, region: e.target.value })
                  }
                />
              </Grid.Col>
              <Grid.Col span={4}>
                <TextInput
                  label="Nivel"
                  value={oficio?.nivel || ""}
                  onChange={(e) =>
                    setOficio({ ...oficio!, nivel: e.target.value })
                  }
                />
              </Grid.Col>
              <Grid.Col span={8}>
                <TextInput
                  label="Institución"
                  value={oficio?.institucion || ""}
                  onChange={(e) =>
                    setOficio({ ...oficio!, institucion: e.target.value })
                  }
                />
              </Grid.Col>
              <Grid.Col span={4}>
                <TextInput
                  label="Residencia / Centro"
                  value={oficio?.residenciaCentro || ""}
                  onChange={(e) =>
                    setOficio({ ...oficio!, residenciaCentro: e.target.value })
                  }
                />
              </Grid.Col>
            </Grid>
          </Paper>

          {oficio?.recomendaciones.map((r, i) => (
            <Paper key={i} withBorder p="md" radius="md" className="shadow-sm">
              <Group justify="space-between" mb="sm">
                <Badge color="dark" variant="light">
                  Recomendación {r.correlativo || i + 1}
                </Badge>
                {recMeta[i]?.plazo && (
                  <Badge
                    color={getPlazoColor(recMeta[i].plazo)}
                    variant="light"
                  >
                    {recMeta[i].plazo}
                  </Badge>
                )}
              </Group>
              <Grid>
                <Grid.Col span={6}>
                  <TextInput
                    label="Dimensión"
                    value={r.dimension || ""}
                    onChange={(e) =>
                      setOficio({
                        ...oficio!,
                        recomendaciones: oficio!.recomendaciones.map((x, j) =>
                          j === i ? { ...x, dimension: e.target.value } : x,
                        ),
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={6}>
                  <TextInput
                    label="Verbo rector"
                    value={r.verbo || ""}
                    onChange={(e) =>
                      setOficio({
                        ...oficio!,
                        recomendaciones: oficio!.recomendaciones.map((x, j) =>
                          j === i ? { ...x, verbo: e.target.value } : x,
                        ),
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={12}>
                  <Textarea
                    label="Nudo crítico"
                    autosize
                    minRows={2}
                    value={r.nudoCritico || ""}
                    onChange={(e) =>
                      setOficio({
                        ...oficio!,
                        recomendaciones: oficio!.recomendaciones.map((x, j) =>
                          j === i ? { ...x, nudoCritico: e.target.value } : x,
                        ),
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={12}>
                  <Textarea
                    label="Descripción"
                    autosize
                    minRows={2}
                    value={r.descripcion || ""}
                    onChange={(e) =>
                      setOficio({
                        ...oficio!,
                        recomendaciones: oficio!.recomendaciones.map((x, j) =>
                          j === i ? { ...x, descripcion: e.target.value } : x,
                        ),
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={3}>
                  <Select
                    label="Plazo"
                    data={PLAZO_OPTIONS}
                    value={recMeta[i]?.plazo || null}
                    onChange={(v) =>
                      setRecMeta({
                        ...recMeta,
                        [i]: { ...recMeta[i], plazo: v },
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={3}>
                  <Select
                    label="¿Acoge?"
                    data={["SI", "NO"]}
                    value={recMeta[i]?.acoge || "SI"}
                    onChange={(v) =>
                      setRecMeta({
                        ...recMeta,
                        [i]: { ...recMeta[i], acoge: v || "SI" },
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={6} className="flex items-end pb-1">
                  <Switch
                    label="Grave Vulneración (GV)"
                    color="red"
                    checked={recMeta[i]?.gv || false}
                    onChange={(e) =>
                      setRecMeta({
                        ...recMeta,
                        [i]: { ...recMeta[i], gv: e.currentTarget.checked },
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={6}>
                  <Select
                    label="Materia"
                    placeholder={
                      r.materiaSugerida
                        ? `IA sugiere: ${r.materiaSugerida}`
                        : "Seleccionar"
                    }
                    data={catToSelect(catalogos.materia)}
                    value={recMeta[i]?.materiaId || null}
                    clearable
                    searchable
                    onChange={(v) =>
                      setRecMeta({
                        ...recMeta,
                        [i]: { ...recMeta[i], materiaId: v },
                      })
                    }
                  />
                </Grid.Col>
                <Grid.Col span={6}>
                  <Select
                    label="Categoría"
                    placeholder={
                      r.categoriaSugerida
                        ? `IA sugiere: ${r.categoriaSugerida}`
                        : "Seleccionar"
                    }
                    data={catToSelect(catalogos.categoria)}
                    value={recMeta[i]?.categoriaId || null}
                    clearable
                    searchable
                    onChange={(v) =>
                      setRecMeta({
                        ...recMeta,
                        [i]: { ...recMeta[i], categoriaId: v },
                      })
                    }
                  />
                </Grid.Col>
              </Grid>
            </Paper>
          ))}
        </Stack>
      </ScrollArea>
      <Group justify="space-between" mt="auto" pt="md" className="border-t">
        <Button variant="default" onClick={() => setPhase("upload")}>
          Atrás
        </Button>
        <Button
          color="simdPrimary"
          leftSection={<Check size={16} />}
          loading={isSaving}
          onClick={handleGuardar}
        >
          Confirmar y guardar
        </Button>
      </Group>
    </Stack>
  );
};

// ============================================================
// DRAWER: EVALUACIÓN DE CUMPLIMIENTO
// ============================================================

interface OverrideItem extends EvaluacionPropuesta {
  evaluacionFinal: string | null;
  fase: string;
  tipoSeguimientoId: string | null;
  tipoRespuestaId: string | null;
}

const EvaluacionDrawerContent = ({
  candidatas,
  catalogos,
  onClose,
  onOk,
}: {
  candidatas: SgsResumen[];
  catalogos: { tipoSeguimiento: Catalogo[]; tipoRespuesta: Catalogo[] };
  onClose: () => void;
  onOk: () => void;
}) => {
  const [step, setStep] = useState(0);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [isProcessing, setIsProcessing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [items, setItems] = useState<OverrideItem[]>([]);
  const [discarded, setDiscarded] = useState<Set<number>>(new Set());
  const [calidad, setCalidad] = useState<string>("");

  const evaluables = candidatas.filter(
    (c) =>
      !["CERRADA_CUMPLIDA", "CERRADA_INCUMPLIDA", "ANULADA"].includes(c.estado),
  );
  const filtered = evaluables.filter(
    (o) =>
      o.nroOficio.toLowerCase().includes(searchQuery.toLowerCase()) ||
      o.dimension.toLowerCase().includes(searchQuery.toLowerCase()),
  );

  const toggleId = (id: number) =>
    setSelectedIds((p) =>
      p.includes(id) ? p.filter((x) => x !== id) : [...p, id],
    );

  const handleEvaluar = async () => {
    if (!file || selectedIds.length === 0) return;
    setIsProcessing(true);
    const fd = new FormData();
    fd.append("file", file);
    try {
      const { data } = await api.post(
        `/sgs/procesar-respuesta?ids=${selectedIds.join(",")}`,
        fd,
        { headers: { "Content-Type": "multipart/form-data" } },
      );
      const res = await pollJob<EvaluacionResult>(data.jobId);
      setCalidad(res.estadoCalidad);
      setItems(
        res.evaluadas.map((e) => ({
          ...e,
          evaluacionFinal: e.evaluacionCumplimiento,
          fase: "F2_ANALISIS",
          tipoSeguimientoId: null,
          tipoRespuestaId: null,
        })),
      );
      setStep(2);
      notifications.show({
        title: "Evaluación lista",
        message: `IA evaluó ${res.evaluadas.length} recomendaciones.`,
        color: "simdPrimary",
        icon: <Check size={16} />,
      });
    } catch (e: any) {
      notifications.show({
        title: "Error",
        message: e?.message || "No se pudo evaluar.",
        color: "red",
      });
    } finally {
      setIsProcessing(false);
    }
  };

  const upd = (id: number, field: keyof OverrideItem, value: any) =>
    setItems(
      items.map((it) => (it.id === id ? { ...it, [field]: value } : it)),
    );
  const toggleDiscard = (id: number) =>
    setDiscarded((p) => {
      const n = new Set(p);
      n.has(id) ? n.delete(id) : n.add(id);
      return n;
    });

  const handleAplicar = async () => {
    const aplicar = items.filter((it) => !discarded.has(it.id));
    if (aplicar.length === 0) {
      notifications.show({
        title: "Atención",
        message: "Todos descartados.",
        color: "yellow",
      });
      return;
    }
    setIsSaving(true);
    const body = {
      items: aplicar.map((it) => ({
        recomendacionId: it.id,
        fase: it.fase,
        fechaSeguimiento: todayISO(),
        tipoSeguimientoId: it.tipoSeguimientoId
          ? Number(it.tipoSeguimientoId)
          : null,
        tipoRespuestaId: it.tipoRespuestaId ? Number(it.tipoRespuestaId) : null,
        fechaRespuesta: it.fechaRespuesta,
        otroSeguimientoInstitucional: it.otroSeguimientoInstitucional,
        evaluacionIA: it.evaluacionCumplimiento,
        valoracionRubrica: it.valoracionRubrica,
        confianza: it.confianzaMatch,
        razonamiento: it.razonamiento,
        evaluacionFinal: it.evaluacionFinal,
        responsableSeguimiento: null,
      })),
      auditMeta: {
        modelo: "gpt-4o",
        modeloSnapshot: "gpt-4o",
        documentos: [
          { nombreArchivo: file?.name || "respuesta.pdf", sha256: "" },
        ],
      },
    };
    try {
      const { data } = await api.post("/sgs/evaluacion-aplicar", body);
      notifications.show({
        title: "Aplicado",
        message: `${data} seguimientos creados.`,
        color: "teal",
      });
      onOk();
      onClose();
    } catch (e: any) {
      notifications.show({
        title: "Error al aplicar",
        message: e?.response?.data?.message || "No se pudo aplicar.",
        color: "red",
      });
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Stack h="100%" gap="md">
      <Stepper
        active={step}
        onStepClick={setStep}
        color="simdPrimary"
        size="sm"
      >
        <Stepper.Step
          label="Seleccionar"
          description="Recomendaciones"
          icon={<ListChecks size={16} />}
          allowStepSelect={step > 0}
        />
        <Stepper.Step
          label="Subir respuesta"
          description="PDF institucional"
          icon={<FileSearch size={16} />}
          allowStepSelect={false}
        />
        <Stepper.Step
          label="Revisar"
          description="Override humano"
          icon={<Gavel size={16} />}
          allowStepSelect={false}
        />
      </Stepper>

      {step === 0 && (
        <Stack className="flex-1" gap="sm">
          <Alert color="simdPrimary" variant="light" icon={<Info size={16} />}>
            Selecciona las recomendaciones a evaluar contra el oficio de
            respuesta.
          </Alert>
          <TextInput
            placeholder="Buscar por N° oficio o dimensión…"
            leftSection={<Search size={14} />}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.currentTarget.value)}
          />
          <Text size="xs" c="dimmed">
            {selectedIds.length} seleccionadas de {evaluables.length}{" "}
            disponibles
          </Text>
          <ScrollArea className="flex-1 border rounded bg-white">
            <Table verticalSpacing="xs">
              <Table.Thead className="bg-gray-50 border-b-2 border-gray-200">
                <Table.Tr>
                  <Table.Th w={40}></Table.Th>
                  <Table.Th>N° Oficio</Table.Th>
                  <Table.Th>Corr.</Table.Th>
                  <Table.Th>Dimensión</Table.Th>
                  <Table.Th>Plazo</Table.Th>
                  <Table.Th>Estado</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {filtered.map((o) => (
                  <Table.Tr
                    key={o.recomendacionId}
                    className="cursor-pointer hover:bg-simdPrimary/5"
                    onClick={() => toggleId(o.recomendacionId)}
                  >
                    <Table.Td>
                      <Checkbox
                        checked={selectedIds.includes(o.recomendacionId)}
                        onChange={() => toggleId(o.recomendacionId)}
                        color="simdPrimary"
                      />
                    </Table.Td>
                    <Table.Td>
                      <Text size="sm" fw={700}>
                        {o.nroOficio}
                      </Text>
                    </Table.Td>
                    <Table.Td>{o.correlativo}</Table.Td>
                    <Table.Td>
                      <Text size="sm">{o.dimension}</Text>
                    </Table.Td>
                    <Table.Td>
                      <Badge
                        size="xs"
                        color={getPlazoColor(o.plazo)}
                        variant="light"
                      >
                        {o.plazo}
                      </Badge>
                    </Table.Td>
                    <Table.Td>
                      <Badge
                        size="xs"
                        color={getEstadoColor(o.estado)}
                        variant="light"
                      >
                        {ESTADO_LABEL[o.estado]}
                      </Badge>
                    </Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </ScrollArea>
          <Group justify="flex-end" mt="auto" pt="md" className="border-t">
            <Button variant="default" onClick={onClose}>
              Cancelar
            </Button>
            <Button
              color="simdPrimary"
              disabled={selectedIds.length === 0}
              onClick={() => setStep(1)}
              rightSection={<FileSearch size={16} />}
            >
              Continuar ({selectedIds.length})
            </Button>
          </Group>
        </Stack>
      )}

      {step === 1 && (
        <Stack className="flex-1">
          <Alert color="simdPrimary" variant="light" icon={<Info size={16} />}>
            Sube el oficio de respuesta. La IA cruzará semánticamente cada
            recomendación con su respuesta.
          </Alert>
          <Badge color="simdPrimary" variant="light" size="lg" w="fit-content">
            {selectedIds.length} recomendaciones a evaluar
          </Badge>
          <Dropzone
            onDrop={(f) => setFile(f[0])}
            accept={PDF_MIME_TYPE}
            className={`border-2 border-dashed rounded-xl p-10 transition-all ${file ? "border-simdPrimary bg-simdPrimary/5" : "border-gray-300 hover:border-simdPrimary/50"}`}
          >
            <Center mih={140}>
              <Stack align="center" gap={4}>
                <FileUp
                  size={40}
                  className={file ? "text-simdPrimary" : "text-gray-400"}
                />
                <Text fw={700} size="lg">
                  {file ? file.name : "Arrastra el PDF de respuesta aquí"}
                </Text>
                <Text size="sm" c="dimmed">
                  Máx 10MB.
                </Text>
              </Stack>
            </Center>
          </Dropzone>
          <Group justify="space-between" mt="auto" pt="md" className="border-t">
            <Button variant="default" onClick={() => setStep(0)}>
              Atrás
            </Button>
            <Button
              color="simdPrimary"
              leftSection={<Bot size={16} />}
              disabled={!file}
              loading={isProcessing}
              onClick={handleEvaluar}
            >
              Evaluar con IA
            </Button>
          </Group>
        </Stack>
      )}

      {step === 2 && (
        <Stack className="flex-1">
          <Group>
            <Badge color="simdPrimary" size="lg" variant="light">
              {items.length} evaluadas
            </Badge>
            <Badge
              color={calidad === "COMPLETA" ? "teal" : "orange"}
              size="lg"
              variant="light"
            >
              Calidad: {calidad}
            </Badge>
            {discarded.size > 0 && (
              <Badge color="gray" size="lg" variant="light">
                {discarded.size} descartadas
              </Badge>
            )}
          </Group>
          <Alert color="simdPrimary" variant="light" icon={<Gavel size={16} />}>
            La propuesta IA se guarda <strong>inmutable</strong>. Lo que elijas
            en "Veredicto final" es el override humano.
          </Alert>
          <ScrollArea className="flex-1 pr-2">
            <Stack gap="md">
              {items.map((it) => {
                const cand = candidatas.find(
                  (c) => c.recomendacionId === it.id,
                );
                const isDisc = discarded.has(it.id);
                return (
                  <Paper
                    key={it.id}
                    withBorder
                    p="sm"
                    radius="md"
                    className={isDisc ? "opacity-40" : "shadow-sm"}
                  >
                    <Group justify="space-between" mb="xs">
                      <Group gap="xs">
                        <Text size="sm" fw={700}>
                          {cand
                            ? `${cand.nroOficio} · Rec. ${cand.correlativo}`
                            : `ID ${it.id}`}
                        </Text>
                        {it.confianzaMatch != null && (
                          <Badge
                            color={getConfianzaColor(it.confianzaMatch)}
                            size="sm"
                            variant="light"
                          >
                            Confianza {(it.confianzaMatch * 100).toFixed(0)}%
                          </Badge>
                        )}
                        <Badge
                          color={getValoracionColor(it.evaluacionCumplimiento)}
                          size="sm"
                        >
                          IA: {it.evaluacionCumplimiento}
                        </Badge>
                        {it.requiereRevisionHumana && (
                          <Badge color="orange" size="sm" variant="light">
                            Revisar
                          </Badge>
                        )}
                      </Group>
                      <Button
                        variant="subtle"
                        size="xs"
                        color={isDisc ? "simdPrimary" : "red"}
                        onClick={() => toggleDiscard(it.id)}
                      >
                        {isDisc ? "Restaurar" : "Descartar"}
                      </Button>
                    </Group>
                    {it.razonamiento && (
                      <Alert color="gray" variant="light" p="xs" mb="xs">
                        <Text size="xs">
                          <strong>Razonamiento IA:</strong> {it.razonamiento}
                        </Text>
                      </Alert>
                    )}
                    {it.valoracionRubrica && (
                      <Text size="xs" c="dimmed" mb="sm" lineClamp={3}>
                        <strong>Valoración:</strong> {it.valoracionRubrica}
                      </Text>
                    )}
                    {!isDisc && (
                      <Grid>
                        <Grid.Col span={6}>
                          <Select
                            size="xs"
                            label="Veredicto final (override)"
                            data={VALORACION_OPTIONS}
                            value={it.evaluacionFinal}
                            onChange={(v) => upd(it.id, "evaluacionFinal", v)}
                          />
                        </Grid.Col>
                        <Grid.Col span={6}>
                          <Select
                            size="xs"
                            label="Fase"
                            data={FASE_OPTIONS}
                            value={it.fase}
                            onChange={(v) =>
                              upd(it.id, "fase", v || "F2_ANALISIS")
                            }
                          />
                        </Grid.Col>
                        <Grid.Col span={6}>
                          <Select
                            size="xs"
                            label="Tipo de seguimiento"
                            data={catToSelect(catalogos.tipoSeguimiento)}
                            value={it.tipoSeguimientoId}
                            clearable
                            onChange={(v) => upd(it.id, "tipoSeguimientoId", v)}
                            placeholder={
                              it.tipoSeguimientoSugerido || "Opcional"
                            }
                          />
                        </Grid.Col>
                        <Grid.Col span={6}>
                          <Select
                            size="xs"
                            label="Tipo de respuesta"
                            data={catToSelect(catalogos.tipoRespuesta)}
                            value={it.tipoRespuestaId}
                            clearable
                            onChange={(v) => upd(it.id, "tipoRespuestaId", v)}
                            placeholder={it.tipoRespuestaSugerido || "Opcional"}
                          />
                        </Grid.Col>
                      </Grid>
                    )}
                  </Paper>
                );
              })}
            </Stack>
          </ScrollArea>
          <Group justify="flex-end" mt="auto" pt="md" className="border-t">
            <Button variant="default" onClick={() => setStep(1)}>
              Atrás
            </Button>
            <Button
              color="simdPrimary"
              leftSection={<Check size={16} />}
              loading={isSaving}
              onClick={handleAplicar}
            >
              Aplicar {items.length - discarded.size} seguimientos
            </Button>
          </Group>
        </Stack>
      )}
    </Stack>
  );
};

// ============================================================
// MODAL: DETALLE DEL OFICIO
// ============================================================

const DetalleModal = ({
  oficioId,
  onClose,
  onChanged,
}: {
  oficioId: number | null;
  onClose: () => void;
  onChanged: () => void;
}) => {
  const [data, setData] = useState<OficioDetalle | null>(null);
  const [loading, setLoading] = useState(false);

  const load = useCallback(async () => {
    if (!oficioId) return;
    setLoading(true);
    try {
      const r = await api.get<OficioDetalle>(`/sgs/oficios/${oficioId}`);
      setData(r.data);
    } catch {
      notifications.show({
        title: "Error",
        message: "No se pudo cargar el detalle.",
        color: "red",
      });
    } finally {
      setLoading(false);
    }
  }, [oficioId]);

  useEffect(() => {
    if (oficioId) load();
  }, [oficioId, load]);

  // Descarga vía axios: el token viaja en el header (un <a href> no lo lleva -> 401 del Gateway)
  const abrirPdf = async (tipo: "documento" | "informe") => {
    if (!data) return;
    try {
      const res = await api.get(`/sgs/oficios/${data.id}/${tipo}`, {
        responseType: "blob",
      });
      const url = URL.createObjectURL(
        new Blob([res.data], { type: "application/pdf" }),
      );
      window.open(url, "_blank");
      setTimeout(() => URL.revokeObjectURL(url), 60000);
    } catch {
      notifications.show({
        title: "Error",
        message: `No se pudo abrir el ${tipo}.`,
        color: "red",
      });
    }
  };

  const cambiarEstado = async (recId: number, nuevoEstado: string) => {
    try {
      await api.post(`/sgs/recomendaciones/${recId}/estado`, { nuevoEstado });
      notifications.show({
        title: "Estado actualizado",
        message: ESTADO_LABEL[nuevoEstado],
        color: "teal",
      });
      load();
      onChanged();
    } catch (e: any) {
      notifications.show({
        title: "Error",
        message: e?.response?.data?.message || "Transición inválida.",
        color: "red",
      });
    }
  };

  const anular = async (recId: number) => {
    const motivo = window.prompt("Motivo de anulación:");
    if (!motivo) return;
    try {
      await api.delete(`/sgs/recomendaciones/${recId}`, { data: { motivo } });
      notifications.show({
        title: "Recomendación anulada",
        message: "Borrado lógico aplicado.",
        color: "gray",
      });
      load();
      onChanged();
    } catch {
      notifications.show({
        title: "Error",
        message: "No se pudo anular.",
        color: "red",
      });
    }
  };

  return (
    <Modal
      opened={oficioId != null}
      onClose={onClose}
      size="xl"
      title={
        <Group gap="xs">
          <FileText size={20} className="text-simdPrimary" />
          <Text fw={700}>Expediente del oficio</Text>
        </Group>
      }
    >
      {loading && (
        <Group justify="center" py="xl">
          <Loader />
        </Group>
      )}
      {data && !loading && (
        <Stack gap="md">
          <Paper withBorder p="sm" radius="md">
            <Grid>
              <Grid.Col span={4}>
                <FieldRow label="N° Oficio" value={data.nroOficio} />
              </Grid.Col>
              <Grid.Col span={4}>
                <FieldRow label="Región" value={data.region} />
              </Grid.Col>
              <Grid.Col span={4}>
                <FieldRow
                  label="Ingreso"
                  value={formatDate(data.fechaIngreso)}
                />
              </Grid.Col>
              <Grid.Col span={6}>
                <FieldRow label="Institución" value={data.institucion} />
              </Grid.Col>
              <Grid.Col span={6}>
                <FieldRow
                  label="Residencia / Centro"
                  value={data.residenciaCentro}
                />
              </Grid.Col>
            </Grid>
            <Group mt="sm" gap="xs">
              {data.pdfHash && (
                <Button
                  size="xs"
                  variant="light"
                  leftSection={<FileText size={14} />}
                  onClick={() => abrirPdf("documento")}
                >
                  Ver PDF original
                </Button>
              )}
              <Button
                size="xs"
                variant="light"
                color="grape"
                leftSection={<FileText size={14} />}
                onClick={() => abrirPdf("informe")}
              >
                Generar informe
              </Button>
            </Group>
          </Paper>

          <Accordion variant="separated" multiple>
            {data.recomendaciones.map((r) => (
              <Accordion.Item key={r.id} value={String(r.id)}>
                <Accordion.Control>
                  <Group gap="xs">
                    <Badge variant="light">Rec. {r.correlativo}</Badge>
                    <Text fw={600} size="sm">
                      {r.dimension}
                    </Text>
                    {r.gv && (
                      <Badge color="red" size="xs">
                        GV
                      </Badge>
                    )}
                    <Badge
                      color={getEstadoColor(r.estado)}
                      size="xs"
                      variant="light"
                    >
                      {ESTADO_LABEL[r.estado]}
                    </Badge>
                    {r.plazo && (
                      <Badge
                        color={getPlazoColor(r.plazo)}
                        size="xs"
                        variant="light"
                      >
                        {r.plazo}
                      </Badge>
                    )}
                    {r.anulado && (
                      <Badge color="gray" size="xs">
                        Anulada
                      </Badge>
                    )}
                  </Group>
                </Accordion.Control>
                <Accordion.Panel>
                  <Stack gap="sm">
                    <Grid>
                      <Grid.Col span={6}>
                        <FieldRow label="Verbo" value={r.verbo} />
                      </Grid.Col>
                      <Grid.Col span={3}>
                        <FieldRow label="Materia" value={r.materia} />
                      </Grid.Col>
                      <Grid.Col span={3}>
                        <FieldRow label="Categoría" value={r.categoria} />
                      </Grid.Col>
                      <Grid.Col span={12}>
                        <FieldRow label="Nudo crítico" value={r.nudoCritico} />
                      </Grid.Col>
                      <Grid.Col span={12}>
                        <FieldRow label="Descripción" value={r.descripcion} />
                      </Grid.Col>
                    </Grid>

                    <Divider
                      label="Historial de seguimiento"
                      labelPosition="left"
                    />
                    {r.seguimientos.length === 0 && (
                      <Text size="sm" c="dimmed" fs="italic">
                        Sin seguimientos aún.
                      </Text>
                    )}
                    {r.seguimientos.map((s) => (
                      <Paper
                        key={s.id}
                        withBorder
                        p="xs"
                        radius="md"
                        className="bg-gray-50/60"
                      >
                        <Group justify="space-between" mb={4}>
                          <Group gap="xs">
                            <Clock size={14} className="text-gray-500" />
                            <Text size="xs" fw={600}>
                              {s.fase}
                            </Text>
                            <Text size="xs" c="dimmed">
                              {formatDate(s.fechaSeguimiento)}
                            </Text>
                          </Group>
                          {s.confianza != null && (
                            <Badge
                              size="xs"
                              color={getConfianzaColor(s.confianza)}
                              variant="light"
                            >
                              IA {(s.confianza * 100).toFixed(0)}%
                            </Badge>
                          )}
                        </Group>
                        <Group gap="xs" mb={4}>
                          <Badge
                            size="xs"
                            variant="dot"
                            color={getValoracionColor(s.evaluacionIA)}
                          >
                            IA: {s.evaluacionIA}
                          </Badge>
                          <Badge
                            size="xs"
                            color={getValoracionColor(s.evaluacionFinal)}
                          >
                            Final: {s.evaluacionFinal}
                          </Badge>
                          {s.evaluacionIA !== s.evaluacionFinal && (
                            <Badge size="xs" color="grape" variant="light">
                              override
                            </Badge>
                          )}
                        </Group>
                        {s.valoracionRubrica && (
                          <Text size="xs" c="dimmed" lineClamp={3}>
                            {s.valoracionRubrica}
                          </Text>
                        )}
                        {s.evaluacionFinalAutor && (
                          <Text size="xs" c="dimmed" mt={2}>
                            Resuelto por {s.evaluacionFinalAutor} ·{" "}
                            {formatDate(s.evaluacionFinalFecha)}
                          </Text>
                        )}
                      </Paper>
                    ))}

                    {!r.anulado && (
                      <Group justify="flex-end" gap="xs" mt="xs">
                        {(ESTADO_TRANSICIONES[r.estado] || []).length > 0 && (
                          <Select
                            size="xs"
                            w={220}
                            placeholder="Cambiar estado…"
                            data={(ESTADO_TRANSICIONES[r.estado] || []).map(
                              (e) => ({ value: e, label: ESTADO_LABEL[e] }),
                            )}
                            onChange={(v) => v && cambiarEstado(r.id, v)}
                          />
                        )}
                        <Tooltip label="Anular (borrado lógico)">
                          <ActionIcon
                            color="red"
                            variant="light"
                            onClick={() => anular(r.id)}
                          >
                            <Trash2 size={16} />
                          </ActionIcon>
                        </Tooltip>
                      </Group>
                    )}
                  </Stack>
                </Accordion.Panel>
              </Accordion.Item>
            ))}
          </Accordion>
        </Stack>
      )}
    </Modal>
  );
};

// ============================================================
// DASHBOARD PRINCIPAL
// ============================================================

const SgsDashboard = () => {
  const [rows, setRows] = useState<SgsResumen[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [total, setTotal] = useState(0);

  const [fEstado, setFEstado] = useState<string | null>(null);
  const [fPlazo, setFPlazo] = useState<string | null>(null);
  const [fGv, setFGv] = useState(false);
  const [fNro, setFNro] = useState("");

  const [catalogos, setCatalogos] = useState<{
    materia: Catalogo[];
    categoria: Catalogo[];
    tipoSeguimiento: Catalogo[];
    tipoRespuesta: Catalogo[];
  }>({ materia: [], categoria: [], tipoSeguimiento: [], tipoRespuesta: [] });

  const [ingestaOpen, setIngestaOpen] = useState(false);
  const [evalOpen, setEvalOpen] = useState(false);
  const [detalleId, setDetalleId] = useState<number | null>(null);

  const fetchRows = useCallback(async () => {
    setLoading(true);
    try {
      const params: Record<string, any> = { page: page - 1, size: 15 };
      if (fEstado) params.estado = fEstado;
      if (fPlazo) params.plazo = fPlazo;
      if (fGv) params.gv = true;
      if (fNro.trim()) params.nroOficio = fNro.trim();
      const { data } = await api.get<Page<SgsResumen>>("/sgs/oficios", {
        params,
      });
      setRows(data.content);
      setTotalPages(data.totalPages || 1);
      setTotal(data.totalElements);
    } catch {
      notifications.show({
        title: "Error",
        message: "No se pudo cargar el tablero.",
        color: "red",
      });
    } finally {
      setLoading(false);
    }
  }, [page, fEstado, fPlazo, fGv, fNro]);

  useEffect(() => {
    fetchRows();
  }, [fetchRows]);

  useEffect(() => {
    (async () => {
      try {
        const [m, c, ts, tr] = await Promise.all([
          api.get<Catalogo[]>("/sgs/catalogos?tipo=MATERIA"),
          api.get<Catalogo[]>("/sgs/catalogos?tipo=CATEGORIA"),
          api.get<Catalogo[]>("/sgs/catalogos?tipo=TIPO_SEGUIMIENTO"),
          api.get<Catalogo[]>("/sgs/catalogos?tipo=TIPO_RESPUESTA"),
        ]);
        setCatalogos({
          materia: m.data,
          categoria: c.data,
          tipoSeguimiento: ts.data,
          tipoRespuesta: tr.data,
        });
      } catch {}
    })();
  }, []);

  const exportarExcel = async () => {
    try {
      const res = await api.get("/sgs/exportar-excel", {
        responseType: "blob",
      });
      const url = URL.createObjectURL(new Blob([res.data]));
      const a = document.createElement("a");
      a.href = url;
      a.download = `seguimiento-sgs-${todayISO()}.xlsx`;
      a.click();
      URL.revokeObjectURL(url);
    } catch {
      notifications.show({
        title: "Error",
        message: "No se pudo exportar.",
        color: "red",
      });
    }
  };

  return (
    <div className="relative isolate p-6 sm:p-10 max-w-[1600px] mx-auto h-full flex flex-col gap-6">
      <div aria-hidden className="aurora aurora--sgs" />
      {/* HEADER INSTITUCIONAL */}
      <div className="relative overflow-hidden glass-panel rounded-2xl px-6 py-5 pl-7 flex flex-col md:flex-row md:items-end justify-between gap-4 before:content-[''] before:absolute before:inset-y-0 before:left-0 before:w-1.5 before:bg-gradient-to-b before:from-simdPrimary before:to-simdAccent">
        <div>
          <Group gap="xs" mb={5}>
            <Badge color="simdPrimary" variant="light" radius="sm">
              Módulo SGS
            </Badge>
            <Text size="xs" c="dimmed" fw={600}>
              SMID | Sistema de Gestión
            </Text>
          </Group>
          <Title order={1} className="font-extrabold tracking-tight">
            Seguimiento de Recomendaciones
          </Title>
          <Group gap="md" mt={6}>
            <Group gap={6}>
              <Hash size={14} className="text-gray-400" />
              <Text size="xs" c="dimmed">
                {total} recomendaciones registradas en el sistema
              </Text>
            </Group>
            <Group gap={6}>
              <Activity size={14} className="text-gray-400" />
              <Text size="xs" c="dimmed">
                Panel de Monitoreo
              </Text>
            </Group>
          </Group>
        </div>
        <Group>
          <Button
            variant="default"
            leftSection={<FileSpreadsheet size={16} />}
            onClick={exportarExcel}
          >
            Exportar Excel
          </Button>
          <Button
            color="simdPrimary"
            variant="light"
            leftSection={<ClipboardCheck size={16} />}
            onClick={() => setEvalOpen(true)}
          >
            Evaluar respuesta
          </Button>
          <Button
            color="simdPrimary"
            leftSection={<Bot size={16} />}
            onClick={() => setIngestaOpen(true)}
          >
            Ingesta Inteligente
          </Button>
        </Group>
      </div>

      {/* CONTENEDOR DE FILTROS MODERNIZADO */}
      <Paper p="sm" radius="md">
        <Group justify="space-between">
          <Group>
            <TextInput
              placeholder="Buscar por N° Oficio…"
              leftSection={<Search size={14} className="text-gray-400" />}
              value={fNro}
              onChange={(e) => {
                setPage(1);
                setFNro(e.currentTarget.value);
              }}
              className="w-64"
            />
            <Select
              placeholder="Estado Administrativo"
              clearable
              w={200}
              value={fEstado}
              data={Object.entries(ESTADO_LABEL).map(([value, label]) => ({
                value,
                label,
              }))}
              onChange={(v) => {
                setPage(1);
                setFEstado(v);
              }}
            />
            <Select
              placeholder="Plazo"
              clearable
              w={150}
              data={PLAZO_OPTIONS}
              value={fPlazo}
              onChange={(v) => {
                setPage(1);
                setFPlazo(v);
              }}
            />
            <Switch
              label="Solo GV"
              checked={fGv}
              color="red"
              onChange={(e) => {
                setPage(1);
                setFGv(e.currentTarget.checked);
              }}
            />
          </Group>
          <ActionIcon variant="default" size="lg" onClick={fetchRows}>
            <RefreshCw size={16} />
          </ActionIcon>
        </Group>
      </Paper>

      {/* TABLA PRINCIPAL CON BORDES Y GRADIENTES ACENTUADOS */}
      <div className="glass-panel rounded-xl border-t-4 border-t-simdPrimary flex-1 flex flex-col overflow-hidden relative">
        <ScrollArea className="flex-1">
          <Table verticalSpacing="md" horizontalSpacing="lg" highlightOnHover>
            <Table.Thead className="bg-gray-50/80">
              <Table.Tr>
                <Table.Th>N° Oficio</Table.Th>
                <Table.Th>Corr.</Table.Th>
                <Table.Th>Dimensión</Table.Th>
                <Table.Th>Institución</Table.Th>
                <Table.Th>Plazo</Table.Th>
                <Table.Th>GV</Table.Th>
                <Table.Th>Estado</Table.Th>
                <Table.Th>Última eval.</Table.Th>
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
              ) : rows.length === 0 ? (
                <Table.Tr>
                  <Table.Td colSpan={9} className="text-center py-10">
                    <Text c="dimmed">
                      Sin recomendaciones registradas. Usa{" "}
                      <strong>Ingesta Inteligente</strong> para cargar un nuevo
                      oficio.
                    </Text>
                  </Table.Td>
                </Table.Tr>
              ) : (
                rows.map((r) => {
                  const rowGradient = getPlazoGradient(r.plazo);
                  return (
                    <Table.Tr
                      key={r.recomendacionId}
                      style={
                        rowGradient ? { background: rowGradient } : undefined
                      }
                    >
                      <Table.Td>
                        <Text size="sm" fw={700}>
                          {r.nroOficio}
                        </Text>
                      </Table.Td>
                      <Table.Td>{r.correlativo}</Table.Td>
                      <Table.Td>
                        <Text size="sm" fw={500}>
                          {r.dimension}
                        </Text>
                      </Table.Td>
                      <Table.Td>
                        <Text size="sm" lineClamp={1} className="max-w-[200px]">
                          {r.institucion}
                        </Text>
                      </Table.Td>
                      <Table.Td>
                        {r.plazo && (
                          <Badge
                            size="sm"
                            color={getPlazoColor(r.plazo)}
                            variant="light"
                          >
                            {r.plazo}
                          </Badge>
                        )}
                      </Table.Td>
                      <Table.Td>
                        {r.gv ? (
                          <Badge size="sm" color="red" variant="filled">
                            GV
                          </Badge>
                        ) : (
                          "—"
                        )}
                      </Table.Td>
                      <Table.Td>
                        <Badge
                          size="sm"
                          color={getEstadoColor(r.estado)}
                          variant="light"
                        >
                          {ESTADO_LABEL[r.estado]}
                        </Badge>
                      </Table.Td>
                      <Table.Td>
                        {r.ultimaEvaluacion ? (
                          <Badge
                            size="sm"
                            color={getValoracionColor(r.ultimaEvaluacion)}
                            variant="filled"
                          >
                            {r.ultimaEvaluacion}
                          </Badge>
                        ) : (
                          <Text size="xs" c="dimmed" fs="italic">
                            Pendiente
                          </Text>
                        )}
                      </Table.Td>
                      <Table.Td className="text-right">
                        <Tooltip label="Ver Expediente Completo">
                          <ActionIcon
                            variant="subtle"
                            color="gray"
                            onClick={() => setDetalleId(r.oficioId)}
                          >
                            <Eye size={18} />
                          </ActionIcon>
                        </Tooltip>
                      </Table.Td>
                    </Table.Tr>
                  );
                })
              )}
            </Table.Tbody>
          </Table>
        </ScrollArea>
      </div>

      {/* PAGINACIÓN */}
      {totalPages > 1 && (
        <Group justify="center" mt="md">
          <Pagination
            value={page}
            onChange={setPage}
            total={totalPages}
            color="simdPrimary"
          />
        </Group>
      )}

      {/* DRAWERS LATERALES (INGESTA Y EVALUACIÓN) */}
      <Drawer
        opened={ingestaOpen}
        onClose={() => setIngestaOpen(false)}
        position="right"
        size="xl"
        title={
          <Group gap="sm">
            <Bot className="text-simdPrimary" size={24} />
            <Text fw={700} size="lg">
              Ingesta Inteligente de Oficios
            </Text>
          </Group>
        }
      >
        <IngestaDrawerContent
          catalogos={catalogos}
          onClose={() => setIngestaOpen(false)}
          onOk={fetchRows}
        />
      </Drawer>

      <Drawer
        opened={evalOpen}
        onClose={() => setEvalOpen(false)}
        position="right"
        size="xl"
        title={
          <Group gap="sm">
            <ClipboardCheck className="text-simdPrimary" size={24} />
            <Text fw={700} size="lg">
              Evaluación de Cumplimiento IA
            </Text>
          </Group>
        }
      >
        {evalOpen && (
          <EvaluacionDrawerContent
            candidatas={rows}
            catalogos={catalogos}
            onClose={() => setEvalOpen(false)}
            onOk={fetchRows}
          />
        )}
      </Drawer>

      {/* MODAL DE DETALLE / EXPEDIENTE */}
      <DetalleModal
        oficioId={detalleId}
        onClose={() => setDetalleId(null)}
        onChanged={fetchRows}
      />
    </div>
  );
};

export default SgsDashboard;
