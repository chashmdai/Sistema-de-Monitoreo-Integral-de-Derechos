package cl.smid.sgs.dto.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Cuerpo de /guardar: oficio + N recomendaciones revisadas + meta de auditoría (CTRL-7). */
public record OficioCreateDTO(
        String nroOficio,
        String region,
        String institucion,
        String residenciaCentro,
        String nivel,
        String pdfHash,
        @NotEmpty @Valid List<RecomendacionCreateDTO> recomendaciones,
        AuditMetaDTO auditMeta
) {}
