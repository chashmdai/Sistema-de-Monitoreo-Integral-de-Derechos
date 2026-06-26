package cl.smid.sgs.dto.internal;

import cl.smid.sgs.enums.EstadoCalidad;

import java.util.List;

/** Resultado tipado de la evaluación (CTRL-5): propuestas + sin match + calidad + omitidos + consumo. */
public record SgsEvaluacionResponseDTO(
        List<EvaluacionPropuestaItemDTO> evaluadas,
        List<Long> sinMatch,
        EstadoCalidad estadoCalidad,
        List<Long> omitidos,
        OpenAiUsage usage
) {}
