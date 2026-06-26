package cl.smid.sgs.dto.internal;

import java.util.List;

/** Salida jerárquica de la Fase A: un oficio con N recomendaciones (DTO-3, MOD-1). pdfHash enlaza el expediente. */
public record OficioExtraccionDTO(
        String nroOficio,
        String region,
        String institucion,
        String residenciaCentro,
        String nivel,
        String pdfHash,
        List<RecomendacionExtraccionDTO> recomendaciones
) {}
