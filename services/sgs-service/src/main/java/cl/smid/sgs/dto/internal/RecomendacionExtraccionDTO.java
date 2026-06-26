package cl.smid.sgs.dto.internal;

import cl.smid.sgs.enums.PlazoRecomendacion;

/** Una recomendación extraída por la Fase A. plazoRaw (texto IA) + plazoEnum (mapeado, GPT-6/decisión #10). */
public record RecomendacionExtraccionDTO(
        String correlativo,
        String dimension,
        String nudoCritico,
        String tipoRecomendacion,
        String verbo,
        String descripcion,
        String plazoRaw,
        PlazoRecomendacion plazoEnum,
        Boolean gv,
        String materiaSugerida,
        String categoriaSugerida
) {}
