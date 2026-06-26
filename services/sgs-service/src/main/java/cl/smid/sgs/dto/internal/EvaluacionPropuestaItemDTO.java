package cl.smid.sgs.dto.internal;

import cl.smid.sgs.enums.EvaluacionCumplimiento;

import java.time.LocalDate;

/**
 * Propuesta de la IA para una candidata (salida de GPT). Lleva confianza + razonamiento + valoración.
 * Las sugerencias de catálogo van como texto; el humano las resuelve a FK al aplicar.
 */
public record EvaluacionPropuestaItemDTO(
        Long id,
        Double confianzaMatch,
        boolean requiereRevisionHumana,
        String razonamiento,
        EvaluacionCumplimiento evaluacionCumplimiento,
        String valoracionRubrica,
        String verbo,
        String materiaSugerida,
        String categoriaSugerida,
        String tipoSeguimientoSugerido,
        String tipoRespuestaSugerido,
        LocalDate fechaSeguimiento,
        LocalDate fechaRespuesta,
        String otroSeguimientoInstitucional
) {}
