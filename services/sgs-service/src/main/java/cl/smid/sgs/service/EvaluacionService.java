package cl.smid.sgs.service;

import cl.smid.sgs.client.OpenAiClient;
import cl.smid.sgs.config.OpenAiProperties;
import cl.smid.sgs.config.SgsProperties;
import cl.smid.sgs.dto.internal.*;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.enums.EstadoCalidad;
import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.exception.SgsEvaluacionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Fase B (REDUCE): evalúa cumplimiento de las candidatas contra el oficio de respuesta.
 * MAP/REDUCE en bloques (decisión #12) para evitar lost-in-the-middle. Veredicto cualitativo IA (decisión #3).
 */
@Service
public class EvaluacionService {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionService.class);

    private final OpenAiClient openAi;
    private final OpenAiProperties openAiProps;
    private final SgsProperties sgsProps;
    private final ObjectMapper om;

    public EvaluacionService(OpenAiClient openAi, OpenAiProperties openAiProps,
                             SgsProperties sgsProps, ObjectMapper om) {
        this.openAi = openAi;
        this.openAiProps = openAiProps;
        this.sgsProps = sgsProps;
        this.om = om;
    }

    public SgsEvaluacionResponseDTO evaluar(String textoRespuesta, List<Recomendacion> candidatas) {
        if (candidatas.isEmpty()) {
            throw new SgsEvaluacionException("No hay candidatas para evaluar.");
        }
        List<EvaluacionPropuestaItemDTO> evaluadas = new ArrayList<>();
        Set<Long> sinMatch = new LinkedHashSet<>();
        List<Long> omitidos = new ArrayList<>();
        OpenAiUsage usageTotal = OpenAiUsage.empty();

        List<List<Recomendacion>> bloques = particionar(candidatas, sgsProps.getBlockSize());
        int bloquesOk = 0;

        for (List<Recomendacion> bloque : bloques) {
            try {
                BloqueResultado br = evaluarBloque(textoRespuesta, bloque);
                evaluadas.addAll(br.items());
                sinMatch.addAll(br.sinMatch());
                usageTotal = usageTotal.plus(br.usage());
                bloquesOk++;
            } catch (Exception e) {
                log.warn("Bloque de evaluación falló ({} ítems omitidos): {}", bloque.size(), e.getMessage());
                bloque.forEach(r -> omitidos.add(r.getId()));
            }
        }

        EstadoCalidad calidad;
        if (bloquesOk == 0) calidad = EstadoCalidad.FALLIDA;
        else if (bloquesOk < bloques.size()) calidad = EstadoCalidad.PARCIAL;
        else calidad = EstadoCalidad.COMPLETA;

        if (calidad == EstadoCalidad.FALLIDA) {
            throw new SgsEvaluacionException("Todos los bloques de evaluación fallaron. Reintente más tarde.");
        }
        return new SgsEvaluacionResponseDTO(evaluadas, new ArrayList<>(sinMatch), calidad, omitidos, usageTotal);
    }

    private BloqueResultado evaluarBloque(String textoRespuesta, List<Recomendacion> bloque) throws Exception {
        List<SgsEvaluacionPromptInputDTO.CandidataDTO> cands = bloque.stream()
                .map(r -> new SgsEvaluacionPromptInputDTO.CandidataDTO(
                        r.getId(), r.getNudoCritico(), r.getDescripcion(), r.getTipoRecomendacion(), r.getPlazo()))
                .collect(Collectors.toList());

        SgsEvaluacionPromptInputDTO input = new SgsEvaluacionPromptInputDTO(cands, textoRespuesta);
        String prompt = construirPrompt(om.writeValueAsString(input));

        OpenAiResult result = openAi.respondJson(openAiProps.getModelEvaluacion(), prompt);
        Set<Long> idsValidos = bloque.stream().map(Recomendacion::getId).collect(Collectors.toSet());
        return parse(result, idsValidos);
    }

    private BloqueResultado parse(OpenAiResult result, Set<Long> idsValidos) throws Exception {
        JsonNode root = om.readTree(result.content());
        List<EvaluacionPropuestaItemDTO> items = new ArrayList<>();
        List<Long> sinMatch = new ArrayList<>();

        for (JsonNode node : root.path("evaluadas")) {
            Long id = node.path("id").asLong();
            if (!idsValidos.contains(id)) {  // defensa anti-hallucination de IDs (se preserva)
                log.warn("GPT retornó ID {} fuera del bloque. Descartado.", id);
                continue;
            }
            Double confianza = node.path("confianzaMatch").isMissingNode() ? null : node.path("confianzaMatch").asDouble();
            boolean revision = confianza != null && confianza < sgsProps.getConfianzaUmbral();
            items.add(new EvaluacionPropuestaItemDTO(
                    id, confianza, revision,
                    txt(node, "razonamiento"),
                    EvaluacionCumplimiento.from(txt(node, "evaluacionCumplimiento")),
                    txt(node, "valoracionRubrica"),
                    txt(node, "verbo"),
                    txt(node, "materiaSugerida"), txt(node, "categoriaSugerida"),
                    txt(node, "tipoSeguimientoSugerido"), txt(node, "tipoRespuestaSugerido"),
                    parseFecha(txt(node, "fechaSeguimiento")), parseFecha(txt(node, "fechaRespuesta")),
                    txt(node, "otroSeguimientoInstitucional")));
        }
        for (JsonNode node : root.path("sinMatch")) {
            long id = node.asLong();
            if (idsValidos.contains(id)) sinMatch.add(id);
        }
        return new BloqueResultado(items, sinMatch, result.usage());
    }

    private List<List<Recomendacion>> particionar(List<Recomendacion> lista, int size) {
        List<List<Recomendacion>> bloques = new ArrayList<>();
        for (int i = 0; i < lista.size(); i += size) {
            bloques.add(new ArrayList<>(lista.subList(i, Math.min(i + size, lista.size()))));
        }
        return bloques;
    }

    private String txt(JsonNode n, String field) {
        JsonNode v = n.path(field);
        return v.isMissingNode() || v.isNull() ? null : v.asText(null);
    }

    private LocalDate parseFecha(String valor) {
        if (valor == null || valor.isBlank() || "null".equalsIgnoreCase(valor)) return null;
        try { return LocalDate.parse(valor.trim()); }
        catch (Exception e) { log.warn("Fecha no parseable '{}', se asigna null.", valor); return null; }
    }

    private String construirPrompt(String datosSerializados) {
        return """
            ERES EL MOTOR DE EVALUACIÓN DE CUMPLIMIENTO DE LA DEFENSORÍA DE LA NIÑEZ DE CHILE.
            Cruza las recomendaciones emitidas (candidatas) con las respuestas del oficio de respuesta institucional.

            RÚBRICA DE VALORACIÓN (asigna 'evaluacionCumplimiento' con EXACTAMENTE uno de estos labels):
            - "Cumplimiento Total": cumple totalmente lo recomendado.
            - "Cumplimiento Parcial Sustancial": cumple parte sustancial; hay acciones avanzadas.
            - "Cumplimiento Parcial": cumple parte mínima; hay acciones iniciales.
            - "Incumplimiento": no cumple; no inició acciones o indica que no adoptará la recomendación.
            - "No hay información": no hay información suficiente para evaluar.
            - "No aplica": la recomendación no aplica.

            PASOS:
            1. Lee 'textoPdfRespuesta'.
            2. MATCHING SEMÁNTICO (no por número): para cada candidata, identifica la sección que la aborda.
            3. Asigna 'evaluacionCumplimiento' (un label de la rúbrica) según la evidencia y el 'plazo' original.
            4. 'valoracionRubrica': justificación cualitativa en texto de POR QUÉ asignaste ese grado (cita la evidencia).
            5. 'confianzaMatch' (0.0 a 1.0): qué tan clara es la correspondencia. Si es ambigua, baja la confianza.
            6. Si una candidata no tiene correspondencia clara, agrégala en 'sinMatch'.
            7. Sugiere materia/categoria/tipoSeguimiento/tipoRespuesta como texto (el humano los confirmará).

            DEVUELVE ÚNICA Y EXCLUSIVAMENTE un JSON válido:
            {
              "evaluadas": [
                {
                  "id": 0,
                  "confianzaMatch": 0.0,
                  "razonamiento": "string",
                  "evaluacionCumplimiento": "uno de los labels de la rúbrica",
                  "valoracionRubrica": "justificación cualitativa",
                  "verbo": "string|null",
                  "materiaSugerida": "string|null",
                  "categoriaSugerida": "string|null",
                  "tipoSeguimientoSugerido": "string|null",
                  "tipoRespuestaSugerido": "string|null",
                  "fechaSeguimiento": "YYYY-MM-DD|null",
                  "fechaRespuesta": "YYYY-MM-DD|null",
                  "otroSeguimientoInstitucional": "string|null"
                }
              ],
              "sinMatch": [0]
            }

            DATOS A EVALUAR:
            """ + datosSerializados;
    }

    private record BloqueResultado(List<EvaluacionPropuestaItemDTO> items, List<Long> sinMatch, OpenAiUsage usage) {}
}
