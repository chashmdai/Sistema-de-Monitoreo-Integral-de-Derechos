package cl.smid.sgs.service;

import cl.smid.sgs.client.OpenAiClient;
import cl.smid.sgs.config.OpenAiProperties;
import cl.smid.sgs.dto.internal.ExtraccionJobResultDTO;
import cl.smid.sgs.dto.internal.OficioExtraccionDTO;
import cl.smid.sgs.dto.internal.OpenAiResult;
import cl.smid.sgs.dto.internal.RecomendacionExtraccionDTO;
import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.exception.OpenAiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Fase A (MAP): extrae el oficio y sus N recomendaciones (DTO-3, MOD-1). Mapea días -> PlazoRecomendacion (GPT-6). */
@Service
public class ExtraccionService {

    private static final Logger log = LoggerFactory.getLogger(ExtraccionService.class);

    private final OpenAiClient openAi;
    private final OpenAiProperties props;
    private final ObjectMapper om;

    public ExtraccionService(OpenAiClient openAi, OpenAiProperties props, ObjectMapper om) {
        this.openAi = openAi;
        this.props = props;
        this.om = om;
    }

    public ExtraccionJobResultDTO extraer(String textoPdf, String pdfHash) {
        String prompt = construirPrompt(textoPdf);
        OpenAiResult result = openAi.chatJson(props.getModelExtraccion(), prompt, 0.1);

        OficioExtraccionDTO dto;
        try {
            dto = parse(result.content(), pdfHash);
        } catch (Exception e) {
            log.error("Error parseando extracción de Fase A: {}", e.getMessage());
            throw new OpenAiException("La extracción de IA no devolvió un JSON con la estructura esperada.", e);
        }
        return new ExtraccionJobResultDTO(dto, props.getModelExtraccion(), props.getModelExtraccion(), result.usage());
    }

    private OficioExtraccionDTO parse(String json, String pdfHash) throws Exception {
        JsonNode root = om.readTree(json);
        List<RecomendacionExtraccionDTO> recs = new ArrayList<>();
        for (JsonNode r : root.path("recomendaciones")) {
            Integer dias = r.path("plazoDias").isMissingNode() || r.path("plazoDias").isNull()
                    ? null : r.path("plazoDias").asInt();
            String plazoRaw = txt(r, "plazoRaw");
            PlazoRecomendacion plazo = dias != null ? PlazoRecomendacion.fromDias(dias) : null;
            recs.add(new RecomendacionExtraccionDTO(
                    txt(r, "correlativo"), txt(r, "dimension"), txt(r, "nudoCritico"),
                    txt(r, "tipoRecomendacion"), txt(r, "verbo"), txt(r, "descripcion"),
                    plazoRaw, plazo,
                    r.path("gv").asBoolean(false),
                    txt(r, "materiaSugerida"), txt(r, "categoriaSugerida")));
        }
        return new OficioExtraccionDTO(
                txt(root, "nroOficio"), txt(root, "region"), txt(root, "institucion"),
                txt(root, "residenciaCentro"), txt(root, "nivel"), pdfHash, recs);
    }

    private String txt(JsonNode n, String field) {
        JsonNode v = n.path(field);
        return v.isMissingNode() || v.isNull() ? null : v.asText(null);
    }

    private String construirPrompt(String texto) {
        return """
            ERES EL MOTOR ANALÍTICO "SGS" DE LA DEFENSORÍA DE LA NIÑEZ DE CHILE.
            Lees Oficios institucionales y extraes el oficio y TODAS las recomendaciones/solicitudes que contiene.
            Un oficio puede contener varias recomendaciones, cada una con su propio plazo. Extrae todas.

            REGLAS:
            - Extrae solo lo explícito o inferible con alta certeza. Si un dato no existe, usa null.
            - 'plazoRaw': el plazo textual tal como aparece (ej. "7 días", "15 días hábiles", "1 mes").
            - 'plazoDias': el plazo convertido a número entero de días (ej. "1 mes" -> 30). Si no hay plazo, null.
            - 'verbo': el verbo rector de la acción (ej. "Implementar", "Reparar", "Capacitar").
            - 'gv': true solo si el documento tipifica el caso como Grave Violación de derechos; si no, false.
            - 'materiaSugerida' y 'categoriaSugerida': clasificación temática propuesta (texto).
            - 'correlativo': número secuencial de la recomendación dentro del oficio ("1", "2", ...).

            DEVUELVE ÚNICA Y EXCLUSIVAMENTE un JSON válido con esta estructura:
            {
              "nroOficio": "string|null",
              "region": "string|null",
              "institucion": "string|null",
              "residenciaCentro": "string|null",
              "nivel": "string|null",
              "recomendaciones": [
                {
                  "correlativo": "string",
                  "dimension": "string|null",
                  "nudoCritico": "string|null",
                  "tipoRecomendacion": "string|null",
                  "verbo": "string|null",
                  "descripcion": "string|null",
                  "plazoRaw": "string|null",
                  "plazoDias": 0,
                  "gv": false,
                  "materiaSugerida": "string|null",
                  "categoriaSugerida": "string|null"
                }
              ]
            }

            DOCUMENTO A ANALIZAR:
            """ + texto;
    }
}
