package cl.smid.sgs.dto.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Cuerpo de /evaluacion-aplicar: hitos confirmados + meta de auditoría echada del job. */
public record AplicarEvaluacionRequest(
        @NotEmpty @Valid List<SeguimientoAplicarDTO> items,
        AuditMetaDTO auditMeta
) {}
