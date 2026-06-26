package cl.smid.sgs.dto.internal;

import cl.smid.sgs.enums.PlazoRecomendacion;

import java.util.List;

/** Input interno al prompt de evaluación. Se construye por bloque de 5 (MAP/REDUCE, decisión #12). */
public record SgsEvaluacionPromptInputDTO(
        List<CandidataDTO> candidatas,
        String textoPdfRespuesta
) {
    public record CandidataDTO(
            Long id,
            String nudoCritico,
            String descripcion,
            String tipoRecomendacion,
            PlazoRecomendacion plazo
    ) {}
}
